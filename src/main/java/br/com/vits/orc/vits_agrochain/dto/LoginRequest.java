package br.com.vits.orc.vits_agrochain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginRequest(
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    String email,
    
    @NotBlank(message = "Senha é obrigatória")
    @JsonProperty("senha")
    String password
) {}
