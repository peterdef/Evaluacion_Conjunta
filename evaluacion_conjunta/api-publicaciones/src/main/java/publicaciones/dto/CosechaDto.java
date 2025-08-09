package publicaciones.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CosechaDto {
    
    @NotBlank(message = "El agricultor_id es obligatorio")
    private String agricultorId;

    @NotBlank(message = "El producto es obligatorio")
    @Size(max = 50, message = "El producto no puede exceder 50 caracteres")
    private String producto;

    @NotNull(message = "Las toneladas son obligatorias")
    @DecimalMin(value = "0.01", message = "Las toneladas deben ser mayores a 0")
    private BigDecimal toneladas;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 100, message = "La ubicación no puede exceder 100 caracteres")
    private String ubicacion;

    // Explicit getters and setters
    public String getAgricultorId() {
        return agricultorId;
    }

    public void setAgricultorId(String agricultorId) {
        this.agricultorId = agricultorId;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public BigDecimal getToneladas() {
        return toneladas;
    }

    public void setToneladas(BigDecimal toneladas) {
        this.toneladas = toneladas;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
