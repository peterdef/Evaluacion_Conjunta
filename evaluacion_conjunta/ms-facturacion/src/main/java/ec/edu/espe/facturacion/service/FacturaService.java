package ec.edu.espe.facturacion.service;

import ec.edu.espe.facturacion.model.Factura;
import ec.edu.espe.facturacion.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final Map<String, BigDecimal> PRECIOS = new HashMap<>();
    
    static {
        PRECIOS.put("Arroz Oro", new BigDecimal("120"));
        PRECIOS.put("Café Premium", new BigDecimal("300"));
        PRECIOS.put("Maíz", new BigDecimal("80"));
        PRECIOS.put("Trigo", new BigDecimal("100"));
    }

    public Factura crearFactura(String cosechaId, String producto, BigDecimal toneladas) {
        BigDecimal precioBase = PRECIOS.getOrDefault(producto, new BigDecimal("100"));
        BigDecimal montoTotal = toneladas.multiply(precioBase);

        Factura factura = new Factura();
        factura.setCosechaId(cosechaId);
        factura.setMontoTotal(montoTotal);
        factura.setPagado(false);

        Factura facturaGuardada = facturaRepository.save(factura);

        // Actualizar estado en el microservicio central
        actualizarEstadoCosecha(cosechaId, "FACTURADA", facturaGuardada.getFacturaId());

        return facturaGuardada;
    }

    public List<Factura> listarFacturas() {
        return facturaRepository.findAll();
    }

    public Optional<Factura> buscarPorId(Long id) {
        return facturaRepository.findById(id);
    }

    public Optional<Factura> buscarPorFacturaId(String facturaId) {
        return facturaRepository.findByFacturaId(facturaId);
    }

    public List<Factura> buscarPorCosechaId(String cosechaId) {
        return facturaRepository.findByCosechaId(cosechaId);
    }

    public List<Factura> buscarPorEstadoPago(Boolean pagado) {
        return facturaRepository.findByPagado(pagado);
    }

    private void actualizarEstadoCosecha(String cosechaId, String estado, String facturaId) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("estado", estado);
            request.put("facturaId", facturaId);

            String url = "http://localhost:8080/cosechas/" + cosechaId + "/estado";
            restTemplate.put(url, request);
            
            System.out.println("Estado de cosecha actualizado: " + cosechaId + " -> " + estado);
        } catch (Exception e) {
            System.err.println("Error actualizando estado de cosecha: " + e.getMessage());
        }
    }
}
