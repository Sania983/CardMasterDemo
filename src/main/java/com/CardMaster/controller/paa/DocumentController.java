package com.CardMaster.controller.paa;

import com.CardMaster.dto.paa.DocumentDto;
import com.CardMaster.service.paa.DocumentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {
    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @PostMapping
    public DocumentDto uploadDocument(@RequestBody DocumentDto dto) {
        return service.uploadDocument(dto);
    }

    @GetMapping("/{id}")
    public DocumentDto getDocument(@PathVariable Long id) {
        return service.getDocument(id);
    }

    @GetMapping("/application/{applicationId}")
    public List<DocumentDto> getDocumentsByApplication(@PathVariable Long applicationId) {
        return service.getDocumentsByApplication(applicationId);
    }

    @PutMapping("/{id}/status")
    public DocumentDto updateDocumentStatus(@PathVariable Long id, @RequestParam String status) {
        return service.updateDocumentStatus(id, status);
    }
}
