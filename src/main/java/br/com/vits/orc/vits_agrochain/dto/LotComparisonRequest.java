package br.com.vits.orc.vits_agrochain.dto;

import java.util.List;

public record LotComparisonRequest(
    List<Long> lotIds
) {}
