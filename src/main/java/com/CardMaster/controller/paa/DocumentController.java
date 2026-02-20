package com.CardMaster.controller.paa;
import com.CardMaster.dto.paa.DocumentDto;
import com.CardMaster.dto.paa.ResponseStructure;
import com.CardMaster.service.paa.DocumentService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

    private static final Logger log = LogManager.getLogger(DocumentController.class);
    private final DocumentService service;

    @PostMapping
    public ResponseEntity<ResponseStructure<DocumentDto>> upload(@RequestBody DocumentDto dto) {
        log.info("Uploading document for application {} "+ dto.getApplicationId());
        DocumentDto saved = service.uploadDocument(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseStructure<>("Document uploaded successfully", saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<DocumentDto>> getById(@PathVariable Long id) {
        log.info("Fetching document {} "+id);
        DocumentDto doc = service.getDocument(id);
        return ResponseEntity.ok(new ResponseStructure<>("Document retrieved successfully", doc));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<ResponseStructure<List<DocumentDto>>> getByApplication(@PathVariable Long applicationId) {
        log.info("Fetching documents for application {}", applicationId);
        List<DocumentDto> docs = service.getDocumentsByApplication(applicationId);
        return ResponseEntity.ok(new ResponseStructure<>("Documents retrieved successfully", docs));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ResponseStructure<DocumentDto>> updateStatus(@PathVariable Long id,
                                                                       @RequestParam String status) {
        log.info("Updating document {} status to {}", id, status);
        DocumentDto updated = service.updateDocumentStatus(id, status);
        return ResponseEntity.ok(new ResponseStructure<>("Document status updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<Void>> deleteDocument(@PathVariable Long id) {
        log.info("Deleting document {}", id);
        service.deleteDocument(id);
        return ResponseEntity.ok(new ResponseStructure<>("Document deleted successfully", null));
    }

    @GetMapping
    public ResponseEntity<ResponseStructure<List<DocumentDto>>> getAllDocuments() {
        log.info("Fetching all documents");
        List<DocumentDto> docs = service.getAllDocuments();
        return ResponseEntity.ok(new ResponseStructure<>("All documents retrieved successfully", docs));
    }
}
