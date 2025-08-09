package publicaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionDto {
    private String eventId;
    private String eventType;
    private LocalDateTime timestamp;
    private String cosechaId;
    private String producto;
    private String estado;
    private String facturaId;
    private String mensaje;
}
