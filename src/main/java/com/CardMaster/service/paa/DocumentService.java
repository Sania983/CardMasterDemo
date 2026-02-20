package com.CardMaster.service.paa;

import com.CardMaster.dao.paa.CardApplicationRepository;
import com.CardMaster.dao.paa.DocumentRepository;
import com.CardMaster.dto.paa.DocumentDto;
import com.CardMaster.exception.paa.ApplicationNotFoundException;
import com.CardMaster.exception.paa.DocumentNotFoundException;
import com.CardMaster.mapper.paa.EntityMapper;
import com.CardMaster.model.paa.CardApplication;
import com.CardMaster.model.paa.Document;
import com.CardMaster.model.paa.Document.DocumentStatus;
import com.CardMaster.security.iam.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository repo;
    private final CardApplicationRepository appRepo;
    private final JwtUtil jwtUtil;

    // --- Upload Document ---
    public DocumentDto uploadDocument(DocumentDto dto, String token) {
        jwtUtil.extractUsername(token.substring(7)); // validate token

        CardApplication app = appRepo.findById(dto.getApplicationId())
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found with id: " + dto.getApplicationId()));

        Document doc = EntityMapper.toDocumentEntity(dto, app);
        doc.setStatus(DocumentStatus.Submitted); // always mark as submitted on upload

        Document saved = repo.save(doc);
        return EntityMapper.toDocumentDto(saved);
    }

    // --- Get Document by ID ---
    public DocumentDto getDocument(Long id, String token) {
        jwtUtil.extractUsername(token.substring(7)); // validate token

        Document doc = repo.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with id: " + id));
        return EntityMapper.toDocumentDto(doc);
    }

    // --- Get Documents by Application ---
    public List<DocumentDto> getDocumentsByApplication(Long appId, String token) {
        // Step 1: Validate JWT
        jwtUtil.extractUsername(token.substring(7));

        // Step 2: Get all documents from the repository
        List<Document> allDocs = repo.findAll();

        // Step 3: Filter documents that belong to the given applicationId
        List<Document> filteredDocs = new ArrayList<>();
        for (Document doc : allDocs) {
            if (doc.getApplication() != null && doc.getApplication().getApplicationId().equals(appId)) {
                filteredDocs.add(doc);
            }
        }

        // Step 4: Convert filtered documents to DTOs
        List<DocumentDto> dtos = new ArrayList<>();
        for (Document doc : filteredDocs) {
            dtos.add(EntityMapper.toDocumentDto(doc));
        }

        return dtos;
    }


    // --- Update Document Status ---
    public DocumentDto updateDocumentStatus(Long id, String status, String token) {
        jwtUtil.extractUsername(token.substring(7)); // validate token

        Document doc = repo.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with id: " + id));

        try {
            doc.setStatus(DocumentStatus.valueOf(status));
        } catch (IllegalArgumentException e) {
            throw new DocumentNotFoundException("Invalid status value: " + status);
        }

        Document updated = repo.save(doc);
        return EntityMapper.toDocumentDto(updated);
    }

    // --- Delete Document ---
    public void deleteDocument(Long id, String token) {
        jwtUtil.extractUsername(token.substring(7)); // validate token

        if (!repo.existsById(id)) {
            throw new DocumentNotFoundException("Document not found with id: " + id);
        }
        repo.deleteById(id);
    }

    // --- Get All Documents ---
    public List<DocumentDto> getAllDocuments(String token) {
        jwtUtil.extractUsername(token.substring(7)); // validate token

        List<Document> docs = repo.findAll();
        List<DocumentDto> dtos = new ArrayList<>();
        for (Document doc : docs) {
            dtos.add(EntityMapper.toDocumentDto(doc));
        }
        return dtos;
    }
}
