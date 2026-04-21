package br.com.vits.orc.vits_agrochain.dto;

public record LotComparisonResponse(
    Long loteId,
    String lote,
    Double producao,
    Double custo,
    Double receita,
    Double lucro
) {}
