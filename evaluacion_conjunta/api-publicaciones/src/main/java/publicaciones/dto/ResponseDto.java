package publicaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private Boolean success;
    private String mensaje;
    private Object dato;

    public ResponseDto(Boolean success, String mensaje) {
        this.success = success;
        this.mensaje = mensaje;
    }

    public ResponseDto(String mensaje, Object dato) {
        this.success = true;
        this.mensaje = mensaje;
        this.dato = dato;
    }
}
