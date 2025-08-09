package publicaciones.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import publicaciones.dto.NotificacionDto;
import publicaciones.model.Cosecha;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class NotificacionProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void publicarNuevaCosecha(Cosecha cosecha) {
        try {
            Map<String, Object> evento = new HashMap<>();
            evento.put("event_id", UUID.randomUUID().toString());
            evento.put("event_type", "nueva_cosecha");
            evento.put("timestamp", LocalDateTime.now().toString());
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("cosecha_id", cosecha.getCosechaId());
            payload.put("producto", cosecha.getProducto());
            payload.put("toneladas", cosecha.getToneladas());
            payload.put("agricultor_id", cosecha.getAgricultor().getAgricultorId());
            payload.put("requiere_insumos", obtenerInsumosRequeridos(cosecha.getProducto()));
            
            evento.put("payload", payload);

            // Publicar en la cola de inventario
            rabbitTemplate.convertAndSend("cosechas", "nueva", evento);
            
            // También publicar en la cola de facturación
            rabbitTemplate.convertAndSend("cosechas", "nueva", evento);
            
        } catch (Exception e) {
            System.err.println("Error al publicar evento de nueva cosecha: " + e.getMessage());
        }
    }

    private String[] obtenerInsumosRequeridos(String producto) {
        switch (producto.toLowerCase()) {
            case "arroz oro":
            case "arroz":
                return new String[]{"Semilla Arroz L-23", "Fertilizante N-PK"};
            case "café premium":
            case "café":
                return new String[]{"Semilla Café Premium", "Fertilizante Café"};
            default:
                return new String[]{"Semilla Genérica", "Fertilizante Genérico"};
        }
    }

    public void enviarNotificacion(String mensaje, String tipo) {
        try {
            NotificacionDto notificacion = new NotificacionDto();
            notificacion.setEventId(UUID.randomUUID().toString());
            notificacion.setEventType("notificacion");
            notificacion.setTimestamp(LocalDateTime.now());
            notificacion.setMensaje(mensaje);
            
            rabbitTemplate.convertAndSend("notificaciones", notificacion);
        } catch (Exception e) {
            System.err.println("Error al enviar notificación: " + e.getMessage());
        }
    }
}
