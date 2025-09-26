package com.matheus.security.controller.dto;

public record LoginResponse(String accessToken, Long expiresIn) {

}
