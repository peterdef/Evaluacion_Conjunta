package publicaciones.model;

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

    // Explicit getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(String facturaId) {
        this.facturaId = facturaId;
    }

    public String getCosechaId() {
        return cosechaId;
    }

    public void setCosechaId(String cosechaId) {
        this.cosechaId = cosechaId;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Boolean getPagado() {
        return pagado;
    }

    public void setPagado(Boolean pagado) {
        this.pagado = pagado;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getCodigoQr() {
        return codigoQr;
    }

    public void setCodigoQr(String codigoQr) {
        this.codigoQr = codigoQr;
    }
}
