package br.com.vits.orc.vits_agrochain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record FinalizeCultivationResponse(
    @JsonProperty("hashEventos")
    String transactionHash,
    
    @JsonProperty("qrCode")
    String qrCodeBase64,
    
    @JsonProperty("arquivoPdf")
    String pdfBase64,

    @JsonProperty("arquivoTxt")
    String txtBase64
) {}
