package br.com.vits.orc.vits_agrochain.dto;

public record TokenResponse(
    String token,
    Long id,
    String name,
    String email,
    String userType
) {}
