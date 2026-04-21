package br.com.vits.orc.vits_agrochain.repository;

import br.com.vits.orc.vits_agrochain.model.CultivationEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CultivationEventRepository extends JpaRepository<CultivationEvent, Long> {
    List<CultivationEvent> findByLoteIdOrderByDataPlantioAsc(Long lotId);
}
