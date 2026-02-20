package com.CardMaster.service.paa;

import com.CardMaster.dao.paa.CardApplicationRepository;
import com.CardMaster.dao.paa.DocumentRepository;
import com.CardMaster.dto.paa.DocumentDto;
import com.CardMaster.exception.paa.ApplicationNotFoundException;
import com.CardMaster.exception.paa.DocumentNotFoundException;
import com.CardMaster.mapper.paa.EntityMapper;
import com.CardMaster.model.paa.CardApplication;
import com.CardMaster.model.paa.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {
    private final DocumentRepository repo;
    private final CardApplicationRepository appRepo;

    public DocumentService(DocumentRepository repo, CardApplicationRepository appRepo) {
        this.repo = repo;
        this.appRepo = appRepo;
    }

    public DocumentDto uploadDocument(DocumentDto dto) {
        CardApplication app = appRepo.findById(dto.getApplicationId())
                .orElseThrow(() -> new ApplicationNotFoundException(dto.getApplicationId()));
        Document doc = EntityMapper.toDocumentEntity(dto, app);
        return EntityMapper.toDocumentDto(repo.save(doc));
    }

    public DocumentDto getDocument(Long id) {
        return repo.findById(id)
                .map(EntityMapper::toDocumentDto)
                .orElseThrow(() -> new DocumentNotFoundException(id));
    }

    public List<DocumentDto> getDocumentsByApplication(Long appId) {
        return repo.findByApplicationApplicationId(appId).stream()
                .map(EntityMapper::toDocumentDto).toList();
    }

    public DocumentDto updateDocumentStatus(Long id, String status) {
        Document doc = repo.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));
        try {
            doc.setStatus(Document.DocumentStatus.valueOf(status));
        } catch (IllegalArgumentException e) {
            throw new DocumentNotFoundException("Invalid status value: " + status);
        }
        return EntityMapper.toDocumentDto(repo.save(doc));
    }
}
