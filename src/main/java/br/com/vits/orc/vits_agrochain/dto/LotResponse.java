package br.com.vits.orc.vits_agrochain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record LotResponse(
    Long id,
    
    @JsonProperty("lote")
    String lotName,
    
    @JsonProperty("cultura")
    String culture,
    
    @JsonProperty("producao")
    Double totalProduction,
    
    @JsonProperty("custo")
    Double totalCost,
    
    @JsonProperty("receita")
    Double estimatedRevenue,
    
    @JsonProperty("lucroEstimado")
    Double estimatedProfit,
    
    @JsonProperty("status")
    String status
) {}
