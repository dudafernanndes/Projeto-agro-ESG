package br.com.vits.orc.vits_agrochain.service;

import br.com.vits.orc.vits_agrochain.dto.*;
import br.com.vits.orc.vits_agrochain.model.Lot;
import br.com.vits.orc.vits_agrochain.repository.LotRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final LotRepository lotRepository;

    public AnalyticsService(LotRepository lotRepository) {
        this.lotRepository = lotRepository;
    }

    public List<MarketQuoteDto> getMarketQuotes() {
        //Dados exemplo
        return List.of(
                new MarketQuoteDto("Soja", 145.50),
                new MarketQuoteDto("Milho", 68.00)
        );
    }

    public List<LotComparisonResponse> compareLots(List<Long> lotIds) {
        List<Lot> lots = lotRepository.findAllById(lotIds);
        return lots.stream().map(lot -> {
            Double prod = lot.getTotalProduction() != null ? lot.getTotalProduction() : 0.0;
            Double price = lot.getSalePrice() != null ? lot.getSalePrice() : 0.0;
            Double cost = lot.getTotalCost() != null ? lot.getTotalCost() : 0.0;
            Double revenue = prod * price;
            Double profit = revenue - cost;
            return new LotComparisonResponse(lot.getLotId(), "Lote " + lot.getLotNumber(), prod, cost, revenue, profit);
        }).collect(Collectors.toList());
    }

    public List<CultureComparisonResponse> compareCultures(List<String> culturas) {
        List<Lot> all = lotRepository.findAll();
        Map<String, List<Lot>> grouped = all.stream()
                .filter(l -> l.getCulture() != null)
                .collect(Collectors.groupingBy(l -> l.getCulture().getCultureName()));

        return grouped.entrySet().stream()
                .filter(e -> culturas == null || culturas.isEmpty() || culturas.contains(e.getKey()))
                .map(e -> {
                    double totalProd = e.getValue().stream().mapToDouble(l -> l.getTotalProduction() != null ? l.getTotalProduction() : 0.0).sum();
                    double totalProfitSum = e.getValue().stream().mapToDouble(l -> {
                        double p = l.getTotalProduction() != null ? l.getTotalProduction() : 0.0;
                        double pr = l.getSalePrice() != null ? l.getSalePrice() : 0.0;
                        double c = l.getTotalCost() != null ? l.getTotalCost() : 0.0;
                        return (p * pr) - c;
                    }).sum();
                    double avgProfit = e.getValue().isEmpty() ? 0.0 : totalProfitSum / e.getValue().size();
                    return new CultureComparisonResponse(e.getKey(), avgProfit, totalProd);
                }).collect(Collectors.toList());
    }

    public List<CostRevenueDto> getCostRevenue() {
        List<Lot> all = lotRepository.findAll();
        return all.stream().map(l -> {
            double cost = l.getTotalCost() != null ? l.getTotalCost() : 0.0;
            double revenue = (l.getTotalProduction() != null ? l.getTotalProduction() : 0.0) * (l.getSalePrice() != null ? l.getSalePrice() : 0.0);
            double profit = revenue - cost;
            return new CostRevenueDto(l.getLotId(), cost, revenue, profit);
        }).collect(Collectors.toList());
    }

    public List<TrendDto> getProfitTrend() {
        // Implementação simplificada: calcula tendência por cultura como média de lucro e crescimento mock (0.0)
        List<CultureComparisonResponse> cultures = compareCultures(Collections.emptyList());
        return cultures.stream().map(c -> new TrendDto(c.cultura(), c.lucroMedio(), 0.0)).collect(Collectors.toList());
    }

    public DashboardDto getDashboard() {
        List<Lot> all = lotRepository.findAll();
        double totalProfit = all.stream().mapToDouble(l -> {
            double revenue = (l.getTotalProduction() != null ? l.getTotalProduction() : 0.0) * (l.getSalePrice() != null ? l.getSalePrice() : 0.0);
            double cost = l.getTotalCost() != null ? l.getTotalCost() : 0.0;
            return revenue - cost;
        }).sum();

        // Cultura mais rentável (maior lucro médio)
        Map<String, Double> avgProfitByCulture = all.stream()
                .filter(l -> l.getCulture() != null)
                .collect(Collectors.groupingBy(l -> l.getCulture().getCultureName(), Collectors.averagingDouble(l -> {
                    double revenue = (l.getTotalProduction() != null ? l.getTotalProduction() : 0.0) * (l.getSalePrice() != null ? l.getSalePrice() : 0.0);
                    double cost = l.getTotalCost() != null ? l.getTotalCost() : 0.0;
                    return revenue - cost;
                })));

        String topCulture = avgProfitByCulture.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        // Lote mais rentável
        String topLot = all.stream()
                .max(Comparator.comparingDouble(l -> {
                    double revenue = (l.getTotalProduction() != null ? l.getTotalProduction() : 0.0) * (l.getSalePrice() != null ? l.getSalePrice() : 0.0);
                    double cost = l.getTotalCost() != null ? l.getTotalCost() : 0.0;
                    return revenue - cost;
                }))
                .map(l -> "Lote " + l.getLotNumber())
                .orElse("N/A");

        double avgPrice = all.stream()
                .mapToDouble(l -> l.getSalePrice() != null ? l.getSalePrice() : 0.0)
                .average().orElse(0.0);

        return new DashboardDto(totalProfit, topCulture, topLot, avgPrice);
    }
}
