package br.com.vits.orc.vits_agrochain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.vits.orc.vits_agrochain.model.Lot;
import br.com.vits.orc.vits_agrochain.repository.LotRepository;
import java.util.List;

@Service
public class LotService {
    
    private final LotRepository lotRepository;
    
    public LotService(LotRepository lotRepository) {
        this.lotRepository = lotRepository;
    }

    public Lot createLot(Lot lot) {
        return lotRepository.save(lot);
    }

    public List<Lot> listAll() {
        return lotRepository.findAll();
    }

    public Page<Lot> listAllPaginated(Pageable pageable) {
        return lotRepository.findAll(pageable);
    }

    public Lot findById(Long id) {
        return lotRepository.findById(id).orElseThrow(() -> new RuntimeException("Lote não encontrado"));
    }

    public void deleteLot(Long id) {
        lotRepository.deleteById(id);
    }
}
