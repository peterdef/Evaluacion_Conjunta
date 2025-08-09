package ec.edu.espe.inventario.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.inventario.service.InsumoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CosechaListener {

    @Autowired
    private InsumoService insumoService;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = "cola_inventario")
    public void procesarNuevaCosecha(String mensaje) {
        try {
            Map<String, Object> evento = objectMapper.readValue(mensaje, Map.class);
            Map<String, Object> payload = (Map<String, Object>) evento.get("payload");
            
            String producto = (String) payload.get("producto");
            Double toneladas = (Double) payload.get("toneladas");
            String cosechaId = (String) payload.get("cosecha_id");
            
            System.out.println("Procesando nueva cosecha: " + producto + " - " + toneladas + " toneladas");
            
            // Calcular insumos requeridos
            ajustarInventario(producto, toneladas);
            
            System.out.println("Inventario ajustado para cosecha: " + cosechaId);
            
        } catch (Exception e) {
            System.err.println("Error procesando evento de cosecha: " + e.getMessage());
        }
    }

    private void ajustarInventario(String producto, Double toneladas) {
        try {
            switch (producto.toLowerCase()) {
                case "arroz oro":
                case "arroz":
                    // 5kg semilla por tonelada + 2kg fertilizante por tonelada
                    int semillaArroz = (int) (toneladas * 5);
                    int fertilizanteArroz = (int) (toneladas * 2);
                    
                    insumoService.actualizarStock("Semilla Arroz L-23", semillaArroz);
                    insumoService.actualizarStock("Fertilizante N-PK", fertilizanteArroz);
                    break;
                    
                case "café premium":
                case "café":
                    // 3kg semilla por tonelada + 1.5kg fertilizante por tonelada
                    int semillaCafe = (int) (toneladas * 3);
                    int fertilizanteCafe = (int) (toneladas * 1.5);
                    
                    insumoService.actualizarStock("Semilla Café Premium", semillaCafe);
                    insumoService.actualizarStock("Fertilizante Café", fertilizanteCafe);
                    break;
                    
                default:
                    // Insumos genéricos
                    int semillaGenerica = (int) (toneladas * 4);
                    int fertilizanteGenerico = (int) (toneladas * 2);
                    
                    insumoService.actualizarStock("Semilla Genérica", semillaGenerica);
                    insumoService.actualizarStock("Fertilizante Genérico", fertilizanteGenerico);
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error ajustando inventario: " + e.getMessage());
        }
    }
}
