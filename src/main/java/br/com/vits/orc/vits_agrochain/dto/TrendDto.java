package br.com.vits.orc.vits_agrochain.dto;

public record TrendDto(
    String cultura,
    Double tendenciaLucro,
    Double crescimento
) {}
