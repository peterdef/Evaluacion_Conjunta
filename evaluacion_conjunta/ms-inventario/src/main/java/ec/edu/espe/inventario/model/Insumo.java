package ec.edu.espe.inventario.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "insumos")
@Setter
@Getter
public class Insumo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "insumo_id", unique = true, nullable = false)
    private String insumoId;

    @Column(name = "nombre_insumo", unique = true, nullable = false, length = 100)
    private String nombreInsumo;

    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    @Column(name = "unidad_medida", length = 10)
    private String unidadMedida = "kg";

    @Column(name = "categoria", nullable = false, length = 30)
    private String categoria;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @PrePersist
    protected void onCreate() {
        insumoId = UUID.randomUUID().toString();
        ultimaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        ultimaActualizacion = LocalDateTime.now();
    }
}
