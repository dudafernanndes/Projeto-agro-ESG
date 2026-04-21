package br.com.vits.orc.vits_agrochain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record CultivationEventDto(
    Long id,
    
    @JsonProperty("loteId")
    Long lotId,
    
    @NotBlank
    @JsonProperty("tipoEvento")
    String eventType,
    
    @JsonProperty("descricao")
    String description,
    
    @JsonProperty("dataPlantio")
    LocalDateTime plantingDate,
    
    @JsonProperty("dataColheitaEstimada")
    LocalDateTime estimatedHarvestDate
) {}
