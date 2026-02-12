package com.CardMaster.service;

import com.CardMaster.dao.AuditLogRepository;
import com.CardMaster.dao.UserRepository;
import com.CardMaster.model.AuditLog;
import com.CardMaster.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    public UserService(UserRepository userRepository, AuditLogRepository auditLogRepository) {
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public User getById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User registerUser(User user) {
        User saved = userRepository.save(user);
        logAction(saved, "REGISTER", "User Registration");
        return saved;
    }

    public User loginUserByUserId(Long userId, String name) {
        return userRepository.findById(userId)
                .filter(u -> u.getName().equals(name)) // replace with password hashing
                .map(u -> {
                    logAction(u, "LOGIN", "User Login");
                    return u;
                })
                .orElse(null);
    }

    private void logAction(User user, String action, String resource) {
        AuditLog log = new AuditLog();
        log.setUser(user);
        log.setAction(action);
        log.setResource(resource);
        log.setMetadata("Performed by " + user.getName());
        auditLogRepository.save(log);
    }
}
