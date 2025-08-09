package ec.edu.espe.notificaciones.listener;

import ec.edu.espe.notificaciones.entity.Notificacion;
import ec.edu.espe.notificaciones.service.NotificacionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificacionListener {

    @Autowired
    private NotificacionService notificacionService;

    @RabbitListener(queues = "cola_notificaciones")
    public void procesarNotificacion(String mensaje) {
        try {
            System.out.println("Procesando notificación: " + mensaje);
            
            Notificacion notificacion = new Notificacion();
            notificacion.setMensaje(mensaje);
            notificacion.setTipo("AGRICULTURA");
            notificacion.setFechaCreacion(LocalDateTime.now());
            
            notificacionService.guardarNotificacion(notificacion);
            
            System.out.println("Notificación guardada exitosamente");
            
        } catch (Exception e) {
            System.err.println("Error procesando notificación: " + e.getMessage());
        }
    }
}
