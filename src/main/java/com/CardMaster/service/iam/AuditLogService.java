package com.CardMaster.service.iam;

import com.CardMaster.dao.iam.AuditLogRepository;
import com.CardMaster.model.iam.AuditLog;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }
}
