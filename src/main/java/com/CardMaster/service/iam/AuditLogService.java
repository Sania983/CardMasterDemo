package com.CardMaster.service.iam;

import com.CardMaster.dao.iam.AuditLogRepository;
import com.CardMaster.model.iam.AuditLog;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    // ✅ Constructor injection
    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    /**
     * Records an audit log entry.
     *
     * @param username The user who performed the action
     * @param action   The action type (REGISTER, LOGIN, LOGOUT, etc.)
     * @param resource The resource affected (User, Application, Card, etc.)
     */
    public void log(String username, String action, String resource) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setResource(resource);
        log.setMetadata("Performed by " + username);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);
    }

    /**
     * Retrieves all audit logs from the database.
     *
     * @return List of AuditLog entries
     */
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }
}
