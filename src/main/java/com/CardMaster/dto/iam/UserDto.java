package com.CardMaster.dto.iam;

import com.CardMaster.Enum.iam.UserEnum;

public class UserDto {
    private Long userId;
    private String name;
    private UserEnum role;
    private String email;
    private String phone;

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public UserEnum getRole() { return role; }
    public void setRole(UserEnum role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
