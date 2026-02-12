package com.CardMaster.controller.iam;

import com.CardMaster.model.iam.User;
import com.CardMaster.service.iam.UserService;
import com.CardMaster.dto.iam.ResponseStructure;
import com.CardMaster.dto.iam.UserDto;
import com.CardMaster.mapper.iam.UserMapper;
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

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    // GET all users
    @GetMapping
    public ResponseEntity<ResponseStructure<List<UserDto>>> getAllUsers() {
        log.info("Inside getAllUsers Controller");
        List<UserDto> users = userService.getAllUser()
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
        User user = userService.getById(userId);

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
        User user = userService.loginUserByUserId(userd.getUserId(), userd.getName());

        ResponseStructure<String> r = new ResponseStructure<>();
        if (user != null) {
            r.setMsg("Login Successful");
            r.setData("Welcome " + user.getName());
            return ResponseEntity.status(HttpStatus.OK).body(r);
        } else {
            r.setMsg("Invalid Credentials");
            r.setData(null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(r);
        }
    }
}
