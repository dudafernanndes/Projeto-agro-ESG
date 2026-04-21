package br.com.vits.orc.vits_agrochain.controller;

import br.com.vits.orc.vits_agrochain.dto.LotRequest;
import br.com.vits.orc.vits_agrochain.dto.LotResponse;
import br.com.vits.orc.vits_agrochain.model.Lot;
import br.com.vits.orc.vits_agrochain.repository.UserRepository;
import br.com.vits.orc.vits_agrochain.service.LotService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lotes")
public class LotController {

    private final LotService lotService;
    private final UserRepository userRepository;

    public LotController(LotService lotService, UserRepository userRepository) {
        this.lotService = lotService;
        this.userRepository = userRepository;
    }

    private LotResponse convertToResponse(Lot lot) {
        Double totalProduction = lot.getTotalProduction() != null ? lot.getTotalProduction() : 0.0;
        Double salePrice = lot.getSalePrice() != null ? lot.getSalePrice() : 0.0;
        Double totalCost = lot.getTotalCost() != null ? lot.getTotalCost() : 0.0;
        Double estimatedRevenue = totalProduction * salePrice;
        Double estimatedProfit = estimatedRevenue - totalCost;
        
        return new LotResponse(
                lot.getLotId(),
                "Lote " + lot.getLotNumber(), 
                lot.getCulture() != null ? lot.getCulture().getCultureName() : "N/A",
                totalProduction,
                totalCost,
                estimatedRevenue,
                estimatedProfit,
                lot.getLotStatus() != null && lot.getLotStatus() == 1 ? "finalizado" : "ativo"
        );
    }

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public void createLot(@RequestBody @Valid LotRequest request) {
        var user = userRepository.findAll().stream().findFirst()
            .orElseThrow(() -> new RuntimeException("Nenhum usuário encontrado para vincular ao lote"));

        Lot lot = Lot.builder()
            .nomeLote(request.lotName())
            .cultura(request.culture())
            .producaoTotal(request.totalProduction() == null ? null : java.math.BigDecimal.valueOf(request.totalProduction()))
            .custoTotal(request.totalCost() == null ? null : java.math.BigDecimal.valueOf(request.totalCost()))
            .precoVenda(request.salePrice() == null ? null : java.math.BigDecimal.valueOf(request.salePrice()))
            .status("ativo")
            .usuario(user)
            .build();
        
        lotService.createLot(lot);
    }

    @GetMapping
    public List<LotResponse> getAll() {
        return lotService.listAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public List<LotResponse> getById(@PathVariable Long id) {
        Lot lot = lotService.findById(id);
        return List.of(convertToResponse(lot));
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteLot(@PathVariable Long id) {
        lotService.deleteLot(id);
        return Map.of("mensagem", "Lote removido com sucesso");
    }
}
