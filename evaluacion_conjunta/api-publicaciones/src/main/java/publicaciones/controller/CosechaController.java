package publicaciones.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import publicaciones.dto.CosechaDto;
import publicaciones.dto.ResponseDto;
import publicaciones.model.Cosecha;
import publicaciones.service.CosechaService;

import java.util.List;

@RestController
@RequestMapping("/cosechas")
public class CosechaController {

    @Autowired
    private CosechaService cosechaService;

    @GetMapping("/test")
    public String test(){
        return "OK";
    }

    @PostMapping
    public ResponseDto crearCosecha(@RequestBody @Valid CosechaDto dto) {
        return cosechaService.crearCosecha(dto);
    }

    @GetMapping
    public List<ResponseDto> obtenerCosechas() {
        return cosechaService.listarCosechas();
    }

    @GetMapping("/todos")
    public List<Cosecha> listarCosechas() {
        return cosechaService.cosechas();
    }

    @GetMapping("/{id}")
    public ResponseDto buscarPorId(@PathVariable Long id) {
        return cosechaService.cosechaPorId(id);
    }

    @PutMapping("/{cosechaId}/estado")
    public ResponseDto actualizarEstado(@PathVariable String cosechaId, 
                                      @RequestBody EstadoCosechaRequest request) {
        return cosechaService.actualizarEstadoCosecha(cosechaId, request.getEstado(), request.getFacturaId());
    }

    public static class EstadoCosechaRequest {
        private String estado;
        private String facturaId;

        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
        public String getFacturaId() { return facturaId; }
        public void setFacturaId(String facturaId) { this.facturaId = facturaId; }
    }
}
