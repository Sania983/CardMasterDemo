package com.CardMaster.controller.iam;

import com.CardMaster.model.iam.User;
import com.CardMaster.security.iam.JwtUtil;
import com.CardMaster.service.iam.UserService;
import com.CardMaster.dto.iam.ResponseStructure;
import com.CardMaster.dto.iam.UserDto;
import com.CardMaster.mapper.iam.UserMapper;
import com.CardMaster.service.iam.AuditLogService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")   // Base path is now /users
public class RegisterController {

    private static final Logger log = LogManager.getLogger(RegisterController.class);

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuditLogService auditLogService;

    // ✅ Constructor injection for all dependencies
    public RegisterController(UserService userService, JwtUtil jwtUtil, AuditLogService auditLogService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.auditLogService = auditLogService;
    }

    // GET all users
    @GetMapping
    public ResponseEntity<ResponseStructure<List<UserDto>>> getAllUsers() {
        log.info("Inside getAllUsers Controller");
        List<UserDto> users = userService.getAllUsers()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());

        ResponseStructure<List<UserDto>> res = new ResponseStructure<>();
        res.setMsg("Users Retrieved Successfully");
        res.setData(users);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // GET user by userId
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseStructure<UserDto>> getUserById(@PathVariable Long userId) {
        log.info("Inside getUserById Controller");
        User user = userService.getUserById(userId);

        ResponseStructure<UserDto> res = new ResponseStructure<>();
        if (user != null) {
            res.setMsg("User Retrieved Successfully");
            res.setData(UserMapper.toDto(user));
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } else {
            res.setMsg("User Not Found");
            res.setData(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }

    // POST register new user
    @PostMapping("/register")
    public ResponseEntity<ResponseStructure<UserDto>> register(@RequestBody User user) {
        log.info("Inside register user Controller");
        User saved = userService.registerUser(user);

        ResponseStructure<UserDto> resp = new ResponseStructure<>();
        resp.setMsg("User created successfully");
        resp.setData(UserMapper.toDto(saved));
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    // POST login
    @PostMapping("/login")
    public ResponseEntity<ResponseStructure<String>> login(@RequestBody User userd) {
        // ✅ Use password for login, not name
        String token = userService.loginUser(userd.getUserId(), userd.getName());
        ResponseStructure<String> r = new ResponseStructure<>();
        r.setMsg("Login Successful");
        r.setData("Bearer " + token);
        return ResponseEntity.status(HttpStatus.OK).body(r);
    }

    // POST logout
    @PostMapping("/logout")
    public ResponseEntity<ResponseStructure<String>> logout(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token.substring(7));
        auditLogService.log(username, "LOGOUT", "User Logout");

        ResponseStructure<String> r = new ResponseStructure<>();
        r.setMsg("Logout Successful");
        r.setData("Goodbye " + username);
        return ResponseEntity.status(HttpStatus.OK).body(r);
    }
}
