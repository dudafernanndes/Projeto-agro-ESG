package br.com.vits.orc.vits_agrochain.controller;

import br.com.vits.orc.vits_agrochain.dto.ExistingLotDto;
import br.com.vits.orc.vits_agrochain.dto.SimulationLotDto;
import br.com.vits.orc.vits_agrochain.dto.SimulationResponse;
import br.com.vits.orc.vits_agrochain.service.SimuladorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/simulacoes")
public class SimuladorController {

    private final SimuladorService simuladorService;

    public SimuladorController(SimuladorService simuladorService) {
        this.simuladorService = simuladorService;
    }

    @PostMapping
    public SimulationResponse simulateOptions(@RequestBody Map<String, SimulationLotDto> scenarios) {
        return simuladorService.simulateOptions(scenarios);
    }

    @GetMapping("/lotes-existentes")
    public List<ExistingLotDto> getExistingLots() {
        return simuladorService.getExistingLots();
    }
}
