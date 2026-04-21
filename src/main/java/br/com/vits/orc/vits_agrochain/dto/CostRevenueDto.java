package br.com.vits.orc.vits_agrochain.dto;

public record CostRevenueDto(
    Long loteId,
    Double custoTotal,
    Double receitaTotal,
    Double lucroTotal
) {}
