package com.CardMaster.service.iam;

import com.CardMaster.dao.iam.AuditLogRepository;
import com.CardMaster.dao.iam.UserRepository;
import com.CardMaster.exception.iam.InvalidCredentialsException;
import com.CardMaster.exception.iam.UserNotFoundException;
import com.CardMaster.model.iam.AuditLog;
import com.CardMaster.model.iam.User;
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

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public User getUserById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        return user;
    }

    // Register new user
    public User registerUser(User user) {
        User savedUser = userRepository.save(user);
        logAction(savedUser, "REGISTER", "User Registration");
        return savedUser;
    }

    // Login user by ID and password
    public User loginUser(Long userId, String name) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }

        if (!user.getName().equals(name)) {
            logAction(user, "LOGIN_FAILED", "Invalid credentials");
            throw new InvalidCredentialsException();
        }

        logAction(user, "LOGIN", "User Login");
        return user;
    }

    // Logout user
    public void logoutUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }

        logAction(user, "LOGOUT", "User Logout");
    }

    // Helper method to save audit logs
    private void logAction(User user, String action, String resource) {
        AuditLog log = new AuditLog();
        log.setUser(user);
        log.setAction(action);
        log.setResource(resource);
        log.setMetadata("Performed by " + user.getName());
        auditLogRepository.save(log);
    }
}
