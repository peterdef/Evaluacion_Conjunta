package publicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import publicaciones.model.Agricultor;

import java.util.Optional;

@Repository
public interface AgricultorRepository extends JpaRepository<Agricultor, Long> {
    Optional<Agricultor> findByAgricultorId(String agricultorId);
    Optional<Agricultor> findByCorreo(String correo);
}
