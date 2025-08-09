package ec.edu.espe.facturacion.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "facturas")
@Setter
@Getter
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "factura_id", unique = true, nullable = false)
    private String facturaId;

    @Column(name = "cosecha_id", nullable = false)
    private String cosechaId;

    @Column(name = "monto_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Column(name = "pagado", nullable = false)
    private Boolean pagado = false;

    @Column(name = "fecha_emision")
    private LocalDateTime fechaEmision;

    @Column(name = "metodo_pago", length = 30)
    private String metodoPago;

    @Column(name = "codigo_qr", columnDefinition = "TEXT")
    private String codigoQr;

    @PrePersist
    protected void onCreate() {
        facturaId = UUID.randomUUID().toString();
        fechaEmision = LocalDateTime.now();
    }
}
