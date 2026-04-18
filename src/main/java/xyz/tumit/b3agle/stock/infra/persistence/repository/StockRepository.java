package xyz.tumit.b3agle.stock.infra.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.tumit.b3agle.stock.infra.persistence.entity.Stock;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, String> {
    Optional<Stock> findByAbbrIgnoreCase(String abbr);
}
