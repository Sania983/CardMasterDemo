package com.CardMaster.service.iam;

import com.CardMaster.dao.iam.AuditLogRepository;
import com.CardMaster.dao.iam.UserRepository;
import com.CardMaster.model.iam.AuditLog;
import com.CardMaster.model.iam.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditLogService(AuditLogRepository auditLogRepository, UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }
    public List<AuditLog> getAllLogs() { return auditLogRepository.findAll(); }

    // Log usin
    // g a User entity
    public void log(User user, String action, String resource) {
        AuditLog log = new AuditLog();
        log.setUser(user);
        log.setAction(action);
        log.setResource(resource);
        log.setMetadata("Performed by " + user.getName());
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }

    // Convenience method: log by email (resolves User first)
    public void log(String email, String action, String resource) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        log(user, action, resource);
    }
}
