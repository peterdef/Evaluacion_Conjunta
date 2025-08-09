package ec.edu.espe.facturacion.repository;

import ec.edu.espe.facturacion.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    Optional<Factura> findByFacturaId(String facturaId);
    List<Factura> findByCosechaId(String cosechaId);
    List<Factura> findByPagado(Boolean pagado);
}
