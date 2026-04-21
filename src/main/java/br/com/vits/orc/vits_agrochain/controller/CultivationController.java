package br.com.vits.orc.vits_agrochain.controller;

import br.com.vits.orc.vits_agrochain.dto.CultivationEventDto;
import br.com.vits.orc.vits_agrochain.dto.FinalizeCultivationResponse;
import br.com.vits.orc.vits_agrochain.dto.LotRequest;
import br.com.vits.orc.vits_agrochain.service.CultivationEventService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lotes")
public class CultivationController {

    private final CultivationEventService cultivationEventService;

    public CultivationController(CultivationEventService cultivationEventService) {
        this.cultivationEventService = cultivationEventService;
    }

    @GetMapping("/{id}/eventos")
    public List<CultivationEventDto> getCultivationEvents(@PathVariable Long id) {
        return cultivationEventService.getEventsByLot(id);
    }

    @PostMapping("/{id}/eventos")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public void addCultivationEvent(
            @PathVariable Long id,
            @RequestBody @Valid CultivationEventDto requestDto) {
        
        cultivationEventService.addEvent(id, requestDto);
    }

    @PostMapping("/{id}/finalizar-cultivo")
    public FinalizeCultivationResponse finalizeCultivation(
            @PathVariable Long id,
            @RequestBody @Valid LotRequest finalDataRequest) {
        
        return cultivationEventService.finalizeCultivation(id, finalDataRequest);
    }
}
