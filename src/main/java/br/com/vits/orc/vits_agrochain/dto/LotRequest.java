package br.com.vits.orc.vits_agrochain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record LotRequest(
    @NotBlank(message = "Nome do lote é obrigatório")
    @JsonProperty("nomeLote")
    String lotName,
    
    @NotBlank(message = "Tipo de cultura é obrigatória")
    @JsonProperty("cultura")
    String culture,
    
    @NotNull(message = "Produção total é obrigatória")
    @PositiveOrZero(message = "Produção não pode ser negativa")
    @JsonProperty("producaoTotal")
    Double totalProduction,
    
    @NotNull(message = "Custo total é obrigatório")
    @PositiveOrZero(message = "Custo não pode ser negativo")
    @JsonProperty("custoTotal")
    Double totalCost,
    
    @NotNull(message = "Preço de venda é obrigatório")
    @PositiveOrZero(message = "O preço de venda não pode ser negativo")
    @JsonProperty("precoVenda")
    Double salePrice
) {}
