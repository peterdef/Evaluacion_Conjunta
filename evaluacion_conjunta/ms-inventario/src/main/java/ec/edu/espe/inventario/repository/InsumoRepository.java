package ec.edu.espe.inventario.repository;

import ec.edu.espe.inventario.model.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Long> {
    Optional<Insumo> findByInsumoId(String insumoId);
    Optional<Insumo> findByNombreInsumo(String nombreInsumo);
    List<Insumo> findByCategoria(String categoria);
    List<Insumo> findByStockLessThan(Integer stock);
}
