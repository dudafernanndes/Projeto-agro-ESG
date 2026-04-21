package br.com.vits.orc.vits_agrochain.dto;

public record SimulationLotDto(
    String cultura,
    Double producaoEstimada,
    Double custoEstimado,
    Double precoEstimado
) {}
