package com.CardMaster.service.paa;

import com.CardMaster.dao.paa.DocumentRepository;
import com.CardMaster.dao.paa.CardApplicationRepository;
import com.CardMaster.dto.paa.DocumentDto;
import com.CardMaster.exceptions.paa.DocumentNotFoundException;
import com.CardMaster.exceptions.paa.ApplicationNotFoundException;
import com.CardMaster.model.paa.CardApplication;
import com.CardMaster.model.paa.Customer;
import com.CardMaster.model.paa.Document;
import com.CardMaster.model.cpl.CardProduct;
import com.CardMaster.security.iam.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentServiceTest {

    @Mock private DocumentRepository documentRepository;
    @Mock private CardApplicationRepository applicationRepository;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks private DocumentService service;

    private CardApplication app;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("Alice");
        customer.setDob(LocalDate.of(1990, 1, 1));
        customer.setEmploymentType(Customer.EmploymentType.Salaried);
        customer.setStatus(Customer.CustomerStatus.Active);

        CardProduct product = new CardProduct();
        product.setProductId(2L);
        product.setName("Platinum");

        app = new CardApplication();
        app.setApplicationId(5L);
        app.setCustomer(customer);
        app.setProduct(product);
        app.setStatus(CardApplication.CardApplicationStatus.Submitted);
    }

    @Test
    void testUploadDocument_Success() {
        DocumentDto dto = new DocumentDto();
        dto.setApplicationId(5L);
        dto.setDocumentType("IdentityProof");
        dto.setStatus("Submitted");
        dto.setFileURI("file://proof.pdf");

        Document doc = new Document();
        doc.setDocumentId(10L);
        doc.setApplication(app);
        doc.setDocumentType(Document.DocumentType.IdentityProof);
        doc.setStatus(Document.DocumentStatus.Submitted);
        doc.setFileURI("file://proof.pdf");

        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(applicationRepository.findById(5L)).thenReturn(Optional.of(app));
        when(documentRepository.save(any(Document.class))).thenReturn(doc);

        DocumentDto result = service.uploadDocument(dto, "Bearer token");
        assertEquals(10L, result.getDocumentId());
        assertEquals("IdentityProof", result.getDocumentType());
    }

    @Test
    void testUploadDocument_ApplicationNotFound() {
        DocumentDto dto = new DocumentDto();
        dto.setApplicationId(99L);

        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(applicationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ApplicationNotFoundException.class,
                () -> service.uploadDocument(dto, "Bearer token"));
    }

    @Test
    void testGetDocument_Success() {
        Document doc = new Document();
        doc.setDocumentId(10L);
        doc.setApplication(app);
        doc.setDocumentType(Document.DocumentType.AddressProof);
        doc.setStatus(Document.DocumentStatus.Verified);
        doc.setFileURI("file://addr.pdf");

        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(documentRepository.findById(10L)).thenReturn(Optional.of(doc));

        DocumentDto result = service.getDocument(10L, "Bearer token");
        assertEquals("AddressProof", result.getDocumentType());
        assertEquals("Verified", result.getStatus());
    }

    @Test
    void testGetDocument_NotFound() {
        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(documentRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(DocumentNotFoundException.class,
                () -> service.getDocument(10L, "Bearer token"));
    }

    @Test
    void testGetDocumentsByApplication_Success() {
        Document doc1 = new Document();
        doc1.setDocumentId(10L);
        doc1.setApplication(app);
        doc1.setDocumentType(Document.DocumentType.IdentityProof);
        doc1.setStatus(Document.DocumentStatus.Verified);

        Document doc2 = new Document();
        doc2.setDocumentId(11L);
        doc2.setApplication(app);
        doc2.setDocumentType(Document.DocumentType.AddressProof);
        doc2.setStatus(Document.DocumentStatus.Submitted);

        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(documentRepository.findByApplicationApplicationId(5L)).thenReturn(List.of(doc1, doc2));

        List<DocumentDto> result = service.getDocumentsByApplication(5L, "Bearer token");
        assertEquals(2, result.size());
    }

    @Test
    void testUpdateDocumentStatus_Verified() {
        Document doc = new Document();
        doc.setDocumentId(10L);
        doc.setApplication(app);
        doc.setStatus(Document.DocumentStatus.Submitted);
        doc.setDocumentType(Document.DocumentType.IdentityProof); // ✅ prevent NPE
        doc.setFileURI("file://proof.pdf");                       // ✅ also good practice

        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(documentRepository.findById(10L)).thenReturn(Optional.of(doc));
        when(documentRepository.save(any(Document.class))).thenReturn(doc);

        DocumentDto result = service.updateDocumentStatus(10L, "Verified", "Bearer token");
        assertEquals("Verified", result.getStatus());
        assertEquals("IdentityProof", result.getDocumentType());
    }


    @Test
    void testUpdateDocumentStatus_CascadeRejected() {
        Document doc = new Document();
        doc.setDocumentId(10L);
        doc.setApplication(app);
        doc.setStatus(Document.DocumentStatus.Submitted);
        doc.setDocumentType(Document.DocumentType.IdentityProof);

        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(documentRepository.findById(10L)).thenReturn(Optional.of(doc));
        when(documentRepository.save(any(Document.class))).thenReturn(doc);
        when(applicationRepository.save(any(CardApplication.class))).thenReturn(app);

        DocumentDto result = service.updateDocumentStatus(10L, "Rejected", "Bearer token");
        assertEquals("Rejected", result.getStatus());
        assertEquals(CardApplication.CardApplicationStatus.Rejected, app.getStatus());
    }


    @Test
    void testUpdateDocumentStatus_Invalid() {
        Document doc = new Document();
        doc.setDocumentId(10L);
        doc.setApplication(app);
        doc.setStatus(Document.DocumentStatus.Submitted);

        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(documentRepository.findById(10L)).thenReturn(Optional.of(doc));

        assertThrows(DocumentNotFoundException.class,
                () -> service.updateDocumentStatus(10L, "INVALID", "Bearer token"));
    }

    @Test
    void testDeleteDocument_Success() {
        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(documentRepository.existsById(10L)).thenReturn(true);

        service.deleteDocument(10L, "Bearer token");
        verify(documentRepository, times(1)).deleteById(10L);
    }

    @Test
    void testDeleteDocument_NotFound() {
        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(documentRepository.existsById(10L)).thenReturn(false);

        assertThrows(DocumentNotFoundException.class,
                () -> service.deleteDocument(10L, "Bearer token"));
    }

    @Test
    void testGetAllDocuments_Success() {
        Document doc1 = new Document();
        doc1.setDocumentId(10L);
        doc1.setApplication(app);
        doc1.setDocumentType(Document.DocumentType.IdentityProof);
        doc1.setStatus(Document.DocumentStatus.Verified);

        Document doc2 = new Document();
        doc2.setDocumentId(11L);
        doc2.setApplication(app);
        doc2.setDocumentType(Document.DocumentType.AddressProof);
        doc2.setStatus(Document.DocumentStatus.Submitted);

        when(jwtUtil.extractUsername(anyString())).thenReturn("user");
        when(documentRepository.findAll()).thenReturn(List.of(doc1, doc2));

        List<DocumentDto> result = service.getAllDocuments("Bearer token");
        assertEquals(2, result.size());
    }
}
