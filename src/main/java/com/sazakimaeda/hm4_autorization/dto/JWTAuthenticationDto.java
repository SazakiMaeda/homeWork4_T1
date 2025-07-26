package com.sazakimaeda.hm4_autorization.dto;

import lombok.Data;

@Data
public class JWTAuthenticationDto {
    private String token;
    private String refreshToken;
}
