package ec.edu.espe.facturacion.controller;

import ec.edu.espe.facturacion.model.Factura;
import ec.edu.espe.facturacion.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    @PostMapping
    public Factura crearFactura(@RequestBody CrearFacturaRequest request) {
        return facturaService.crearFactura(request.getCosechaId(), request.getProducto(), request.getToneladas());
    }

    @GetMapping
    public List<Factura> listarFacturas() {
        return facturaService.listarFacturas();
    }

    @GetMapping("/{id}")
    public Optional<Factura> buscarPorId(@PathVariable Long id) {
        return facturaService.buscarPorId(id);
    }

    @GetMapping("/factura/{facturaId}")
    public Optional<Factura> buscarPorFacturaId(@PathVariable String facturaId) {
        return facturaService.buscarPorFacturaId(facturaId);
    }

    @GetMapping("/cosecha/{cosechaId}")
    public List<Factura> buscarPorCosechaId(@PathVariable String cosechaId) {
        return facturaService.buscarPorCosechaId(cosechaId);
    }

    @GetMapping("/estado/{pagado}")
    public List<Factura> buscarPorEstadoPago(@PathVariable Boolean pagado) {
        return facturaService.buscarPorEstadoPago(pagado);
    }

    public static class CrearFacturaRequest {
        private String cosechaId;
        private String producto;
        private BigDecimal toneladas;

        public String getCosechaId() { return cosechaId; }
        public void setCosechaId(String cosechaId) { this.cosechaId = cosechaId; }
        public String getProducto() { return producto; }
        public void setProducto(String producto) { this.producto = producto; }
        public BigDecimal getToneladas() { return toneladas; }
        public void setToneladas(BigDecimal toneladas) { this.toneladas = toneladas; }
    }
}
