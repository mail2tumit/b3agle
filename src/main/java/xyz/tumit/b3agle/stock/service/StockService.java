package xyz.tumit.b3agle.stock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.tumit.b3agle.stock.infra.persistence.entity.Stock;
import xyz.tumit.b3agle.stock.infra.persistence.repository.StockRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StockService {

    private final StockRepository stockRepository;

    public Optional<Stock> findByAbbr(String abbr) {
        return stockRepository.findByAbbrIgnoreCase(abbr);
    }

}
