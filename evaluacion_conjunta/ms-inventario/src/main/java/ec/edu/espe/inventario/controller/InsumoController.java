package ec.edu.espe.inventario.controller;

import ec.edu.espe.inventario.model.Insumo;
import ec.edu.espe.inventario.service.InsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/insumos")
public class InsumoController {

    @Autowired
    private InsumoService insumoService;

    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    @PostMapping
    public Insumo crearInsumo(@RequestBody Insumo insumo) {
        return insumoService.crearInsumo(insumo);
    }

    @GetMapping
    public List<Insumo> listarInsumos() {
        return insumoService.listarInsumos();
    }

    @GetMapping("/{id}")
    public Optional<Insumo> buscarPorId(@PathVariable Long id) {
        return insumoService.buscarPorId(id);
    }

    @GetMapping("/nombre/{nombreInsumo}")
    public Optional<Insumo> buscarPorNombre(@PathVariable String nombreInsumo) {
        return insumoService.buscarPorNombre(nombreInsumo);
    }

    @PutMapping("/stock/{nombreInsumo}")
    public Insumo actualizarStock(@PathVariable String nombreInsumo, @RequestParam Integer cantidad) {
        return insumoService.actualizarStock(nombreInsumo, cantidad);
    }

    @GetMapping("/stock-bajo/{stockMinimo}")
    public List<Insumo> buscarStockBajo(@PathVariable Integer stockMinimo) {
        return insumoService.buscarStockBajo(stockMinimo);
    }
}
