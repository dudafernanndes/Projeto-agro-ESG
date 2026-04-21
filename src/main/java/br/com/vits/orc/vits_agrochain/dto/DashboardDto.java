package br.com.vits.orc.vits_agrochain.dto;

public record DashboardDto(
    Double lucroTotal,
    String culturaMaisRentavel,
    String loteMaisRentavel,
    Double precoMedio
) {}
