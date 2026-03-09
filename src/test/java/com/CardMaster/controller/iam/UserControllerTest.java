package com.CardMaster.controller.iam;

import com.CardMaster.Enum.iam.UserEnum;
import com.CardMaster.model.iam.User;
import com.CardMaster.security.iam.JwtUtil;
import com.CardMaster.service.iam.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void testGetAllUsers() throws Exception {
        User user = new User(1L, "Priya", UserEnum.CUSTOMER,
                "priya@example.com", "9876543210", "secret123");

        when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Users Retrieved Successfully"));
    }

    @Test
    void testGetUserById() throws Exception {
        User user = new User(1L, "Priya", UserEnum.CUSTOMER,
                "priya@example.com", "9876543210", "secret123");

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("User Retrieved Successfully"))
                .andExpect(jsonPath("$.data.name").value("Priya"));
    }

    @Test
    void testRegisterUser() throws Exception {
        User user = new User(1L, "Priya", UserEnum.CUSTOMER,
                "priya@example.com", "9876543210", "secret123");

        when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Priya\",\"email\":\"priya@example.com\",\"phone\":\"9876543210\",\"password\":\"secret123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").value("User created successfully"))
                .andExpect(jsonPath("$.data.name").value("Priya"));
    }

    @Test
    void testLoginUser() throws Exception {
        when(userService.loginUser(1L, "secret123")).thenReturn("mockToken");

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"password\":\"secret123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Login Successful"))
                .andExpect(jsonPath("$.data").value("Bearer mockToken"));
    }

    @Test
    void testLogoutUser() throws Exception {
        when(jwtUtil.extractUserId("mockToken")).thenReturn(1L);

        mockMvc.perform(post("/users/logout")
                        .header("Authorization", "Bearer mockToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Logout Successful"))
                .andExpect(jsonPath("$.data").value("Goodbye 1"));
    }
}
