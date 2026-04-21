package br.com.vits.orc.vits_agrochain.controller;

import br.com.vits.orc.vits_agrochain.dto.DashboardDto;
import br.com.vits.orc.vits_agrochain.service.AnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final AnalyticsService analyticsService;

    public DashboardController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    public DashboardDto getDashboard() {
        return analyticsService.getDashboard();
    }
}
