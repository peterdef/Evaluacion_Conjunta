package publicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import publicaciones.model.Cosecha;

import java.util.List;
import java.util.Optional;

@Repository
public interface CosechaRepository extends JpaRepository<Cosecha, Long> {
    Optional<Cosecha> findByCosechaId(String cosechaId);
    List<Cosecha> findByAgricultorAgricultorId(String agricultorId);
    List<Cosecha> findByEstado(String estado);
}
