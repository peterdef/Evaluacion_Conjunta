package publicaciones.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "cosechas")
@Setter
@Getter
public class Cosecha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cosecha_id", unique = true, nullable = false)
    private String cosechaId;

    @ManyToOne
    @JoinColumn(name = "agricultor_id", nullable = false)
    private Agricultor agricultor;

    @Column(name = "producto", nullable = false, length = 50)
    private String producto;

    @Column(name = "toneladas", nullable = false, precision = 10, scale = 2)
    private BigDecimal toneladas;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "REGISTRADA";

    @Column(name = "factura_id")
    private String facturaId;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        cosechaId = UUID.randomUUID().toString();
        creadoEn = LocalDateTime.now();
    }
}
