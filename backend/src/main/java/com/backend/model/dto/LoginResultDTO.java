package com.backend.model.dto;

import lombok.Data;

@Data
public class LoginResultDTO {
    private String token;
    private UserDTO user;
}
