package br.com.vits.orc.vits_agrochain.controller;

import br.com.vits.orc.vits_agrochain.dto.*;
import br.com.vits.orc.vits_agrochain.service.AnalyticsService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/cotacoes")
    public List<MarketQuoteDto> getQuotes() {
        return analyticsService.getMarketQuotes();
    }

    @PostMapping("/comparativo-lotes")
    public List<LotComparisonResponse> compareLots(@RequestBody @Valid LotComparisonRequest request) {
        return analyticsService.compareLots(request.lotIds());
    }

    @PostMapping("/comparativo-culturas")
    public List<CultureComparisonResponse> compareCultures(@RequestBody @Valid CultureComparisonRequest request) {
        return analyticsService.compareCultures(request.culturas());
    }

    @GetMapping("/custo-receita")
    public List<CostRevenueDto> costRevenue() {
        return analyticsService.getCostRevenue();
    }

    @GetMapping("/tendencia-lucro")
    public List<TrendDto> profitTrend() {
        return analyticsService.getProfitTrend();
    }
}
