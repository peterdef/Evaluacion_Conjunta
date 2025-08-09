package ec.edu.espe.inventario.service;

import ec.edu.espe.inventario.model.Insumo;
import ec.edu.espe.inventario.repository.InsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InsumoService {

    @Autowired
    private InsumoRepository insumoRepository;

    public Insumo crearInsumo(Insumo insumo) {
        return insumoRepository.save(insumo);
    }

    public List<Insumo> listarInsumos() {
        return insumoRepository.findAll();
    }

    public Optional<Insumo> buscarPorId(Long id) {
        return insumoRepository.findById(id);
    }

    public Optional<Insumo> buscarPorNombre(String nombreInsumo) {
        return insumoRepository.findByNombreInsumo(nombreInsumo);
    }

    public Insumo actualizarStock(String nombreInsumo, Integer cantidad) {
        Optional<Insumo> insumoOpt = insumoRepository.findByNombreInsumo(nombreInsumo);
        if (insumoOpt.isPresent()) {
            Insumo insumo = insumoOpt.get();
            insumo.setStock(insumo.getStock() - cantidad);
            return insumoRepository.save(insumo);
        }
        throw new RuntimeException("Insumo no encontrado: " + nombreInsumo);
    }

    public List<Insumo> buscarStockBajo(Integer stockMinimo) {
        return insumoRepository.findByStockLessThan(stockMinimo);
    }
}
