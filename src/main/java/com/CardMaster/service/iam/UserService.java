package com.CardMaster.service.iam;

import com.CardMaster.dao.iam.UserRepository;
import com.CardMaster.dao.iam.AuditLogRepository;
import com.CardMaster.dto.iam.UserDto;
import com.CardMaster.exceptions.iam.InvalidCredentialsException;
import com.CardMaster.exceptions.iam.UserNotFoundException;
import com.CardMaster.mapper.iam.UserMapper;
import com.CardMaster.model.iam.AuditLog;
import com.CardMaster.model.iam.User;
import com.CardMaster.security.iam.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;
    private final JwtUtil jwtUtil;

    // Get all users
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    // Get user by ID
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    // Register new user
    public User registerUser(User user) {
        User savedUser = userRepository.save(user);
        logAction(savedUser, "REGISTER", "User Registration");
        return savedUser;
    }

    // Login user by ID + password
    public String loginUser(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!user.getPassword().equals(password)) {
            logAction(user, "LOGIN_FAILED", "Invalid credentials");
            throw new InvalidCredentialsException();
        }

        logAction(user, "LOGIN", "User Login");
        return jwtUtil.generateToken(user.getUserId().toString());
    }

    // Logout user
    public void logoutUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
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
