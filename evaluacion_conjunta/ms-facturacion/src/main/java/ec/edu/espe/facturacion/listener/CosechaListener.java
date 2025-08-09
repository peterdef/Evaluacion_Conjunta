package ec.edu.espe.facturacion.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.facturacion.service.FacturaService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class CosechaListener {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = "cola_facturacion")
    public void procesarNuevaCosecha(String mensaje) {
        try {
            Map<String, Object> evento = objectMapper.readValue(mensaje, Map.class);
            Map<String, Object> payload = (Map<String, Object>) evento.get("payload");
            
            String producto = (String) payload.get("producto");
            Double toneladas = (Double) payload.get("toneladas");
            String cosechaId = (String) payload.get("cosecha_id");
            
            System.out.println("Generando factura para cosecha: " + producto + " - " + toneladas + " toneladas");
            
            // Crear factura
            facturaService.crearFactura(cosechaId, producto, BigDecimal.valueOf(toneladas));
            
            System.out.println("Factura generada para cosecha: " + cosechaId);
            
        } catch (Exception e) {
            System.err.println("Error procesando evento de cosecha para facturación: " + e.getMessage());
        }
    }
}
