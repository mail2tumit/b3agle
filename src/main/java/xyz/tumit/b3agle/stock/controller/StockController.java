package xyz.tumit.b3agle.stock.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import xyz.tumit.b3agle.stock.infra.persistence.entity.Stock;
import xyz.tumit.b3agle.stock.service.StockService;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class StockController {

    private final StockService stockService;

    @GetMapping("stocks/{abbr}")
    public Optional<Stock> findByAbbr(@PathVariable(name = "abbr") String abbr) {
        return stockService.findByAbbr(abbr);
    }

}
