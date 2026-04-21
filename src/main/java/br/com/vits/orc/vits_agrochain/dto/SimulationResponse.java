package br.com.vits.orc.vits_agrochain.dto;

import java.util.Map;

public record SimulationResponse(
    Map<String, Double> comparacaoLucro,
    Map<String, Double> comparacaoReceita,
    String melhorOpcao
) {}
