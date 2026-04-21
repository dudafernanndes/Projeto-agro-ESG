package br.com.vits.orc.vits_agrochain.service;

import br.com.vits.orc.vits_agrochain.dto.ExistingLotDto;
import br.com.vits.orc.vits_agrochain.dto.SimulationLotDto;
import br.com.vits.orc.vits_agrochain.dto.SimulationResponse;
import br.com.vits.orc.vits_agrochain.model.Lot;
import br.com.vits.orc.vits_agrochain.repository.LotRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SimuladorService {

    private final LotRepository lotRepository;

    public SimuladorService(LotRepository lotRepository) {
        this.lotRepository = lotRepository;
    }

    public List<ExistingLotDto> getExistingLots() {
        List<Lot> lots = lotRepository.findAll();
        return lots.stream()
                .map(l -> new ExistingLotDto(
                        l.getLotId(),
                        "Lote " + l.getLotNumber(),
                        l.getCulture() != null ? l.getCulture().getCultureName() : "N/A"
                ))
                .collect(Collectors.toList());
    }

    public SimulationResponse simulateOptions(Map<String, SimulationLotDto> scenarios) {
        Map<String, Double> comparacaoLucro = new HashMap<>();
        Map<String, Double> comparacaoReceita = new HashMap<>();
        
        String melhorOpcao = null;
        double maxLucro = Double.NEGATIVE_INFINITY;

        for (Map.Entry<String, SimulationLotDto> entry : scenarios.entrySet()) {
            String key = entry.getKey();
            SimulationLotDto data = entry.getValue();

            double receita = data.producaoEstimada() * data.precoEstimado();
            double lucro = receita - data.custoEstimado();

            comparacaoReceita.put(key, receita);
            comparacaoLucro.put(key, lucro);

            if (lucro > maxLucro) {
                maxLucro = lucro;
                melhorOpcao = key;
            }
        }

        return new SimulationResponse(comparacaoLucro, comparacaoReceita, melhorOpcao);
    }
}
