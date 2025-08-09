package publicaciones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import publicaciones.dto.CosechaDto;
import publicaciones.dto.ResponseDto;
import publicaciones.model.Agricultor;
import publicaciones.model.Cosecha;
import publicaciones.producer.NotificacionProducer;
import publicaciones.repository.AgricultorRepository;
import publicaciones.repository.CosechaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CosechaService {

    @Autowired
    private CosechaRepository cosechaRepository;

    @Autowired
    private AgricultorRepository agricultorRepository;

    @Autowired
    private NotificacionProducer notificacionProducer;

    public ResponseDto crearCosecha(CosechaDto dto) {
        try {
            // Verificar que el agricultor existe
            Optional<Agricultor> agricultorOpt = agricultorRepository.findByAgricultorId(dto.getAgricultorId());
            if (agricultorOpt.isEmpty()) {
                return new ResponseDto(false, "Agricultor no encontrado con ID: " + dto.getAgricultorId());
            }

            Cosecha cosecha = new Cosecha();
            cosecha.setAgricultor(agricultorOpt.get());
            cosecha.setProducto(dto.getProducto());
            cosecha.setToneladas(dto.getToneladas());
            cosecha.setEstado("REGISTRADA");

            Cosecha cosechaGuardada = cosechaRepository.save(cosecha);

            // Publicar evento de nueva cosecha
            notificacionProducer.publicarNuevaCosecha(cosechaGuardada);

            return new ResponseDto(true, "Cosecha registrada exitosamente con ID: " + cosechaGuardada.getCosechaId());

        } catch (Exception e) {
            return new ResponseDto(false, "Error al crear cosecha: " + e.getMessage());
        }
    }

    public List<ResponseDto> listarCosechas() {
        try {
            List<Cosecha> cosechas = cosechaRepository.findAll();
            return cosechas.stream()
                    .map(cosecha -> new ResponseDto(true, 
                        "Cosecha: " + cosecha.getProducto() + 
                        " - Toneladas: " + cosecha.getToneladas() + 
                        " - Estado: " + cosecha.getEstado() +
                        " - Agricultor: " + cosecha.getAgricultor().getNombre()))
                    .toList();
        } catch (Exception e) {
            return List.of(new ResponseDto(false, "Error al listar cosechas: " + e.getMessage()));
        }
    }

    public List<Cosecha> cosechas() {
        return cosechaRepository.findAll();
    }

    public ResponseDto cosechaPorId(Long id) {
        try {
            Optional<Cosecha> cosecha = cosechaRepository.findById(id);
            if (cosecha.isPresent()) {
                Cosecha c = cosecha.get();
                return new ResponseDto(true, 
                    "Cosecha encontrada: " + c.getProducto() + 
                    " - Toneladas: " + c.getToneladas() + 
                    " - Estado: " + c.getEstado() +
                    " - Agricultor: " + c.getAgricultor().getNombre());
            } else {
                return new ResponseDto(false, "Cosecha no encontrada con ID: " + id);
            }
        } catch (Exception e) {
            return new ResponseDto(false, "Error al buscar cosecha: " + e.getMessage());
        }
    }

    public ResponseDto actualizarEstadoCosecha(String cosechaId, String nuevoEstado, String facturaId) {
        try {
            Optional<Cosecha> cosechaOpt = cosechaRepository.findByCosechaId(cosechaId);
            if (cosechaOpt.isPresent()) {
                Cosecha cosecha = cosechaOpt.get();
                cosecha.setEstado(nuevoEstado);
                if (facturaId != null) {
                    cosecha.setFacturaId(facturaId);
                }
                cosechaRepository.save(cosecha);
                return new ResponseDto(true, "Estado de cosecha actualizado exitosamente");
            } else {
                return new ResponseDto(false, "Cosecha no encontrada con ID: " + cosechaId);
            }
        } catch (Exception e) {
            return new ResponseDto(false, "Error al actualizar estado de cosecha: " + e.getMessage());
        }
    }
}
