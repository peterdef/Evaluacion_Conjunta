package publicaciones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import publicaciones.dto.AgricultorDto;
import publicaciones.dto.ResponseDto;
import publicaciones.model.Agricultor;
import publicaciones.repository.AgricultorRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AgricultorService {

    @Autowired
    private AgricultorRepository agricultorRepository;

    public ResponseDto crearAgricultor(AgricultorDto dto) {
        try {
            // Verificar si ya existe un agricultor con el mismo agricultorId
            if (agricultorRepository.findByAgricultorId(dto.getAgricultorId()).isPresent()) {
                return new ResponseDto(false, "Ya existe un agricultor con el ID: " + dto.getAgricultorId());
            }

            // Verificar si ya existe un agricultor con el mismo correo
            if (agricultorRepository.findByCorreo(dto.getCorreo()).isPresent()) {
                return new ResponseDto(false, "Ya existe un agricultor con el correo: " + dto.getCorreo());
            }

            Agricultor agricultor = new Agricultor();
            agricultor.setAgricultorId(dto.getAgricultorId());
            agricultor.setNombre(dto.getNombre());
            agricultor.setFinca(dto.getFinca());
            agricultor.setUbicacion(dto.getUbicacion());
            agricultor.setCorreo(dto.getCorreo());

            agricultorRepository.save(agricultor);
            return new ResponseDto(true, "Agricultor creado exitosamente");

        } catch (Exception e) {
            return new ResponseDto(false, "Error al crear agricultor: " + e.getMessage());
        }
    }

    public List<ResponseDto> listarAgricultores() {
        try {
            List<Agricultor> agricultores = agricultorRepository.findAll();
            return agricultores.stream()
                    .map(agricultor -> new ResponseDto(true, 
                        "Agricultor: " + agricultor.getNombre() + 
                        " - Finca: " + agricultor.getFinca() + 
                        " - Ubicación: " + agricultor.getUbicacion()))
                    .toList();
        } catch (Exception e) {
            return List.of(new ResponseDto(false, "Error al listar agricultores: " + e.getMessage()));
        }
    }

    public List<Agricultor> agricultores() {
        return agricultorRepository.findAll();
    }

    public ResponseDto agricultorPorId(Long id) {
        try {
            Optional<Agricultor> agricultor = agricultorRepository.findById(id);
            if (agricultor.isPresent()) {
                Agricultor a = agricultor.get();
                return new ResponseDto(true, 
                    "Agricultor encontrado: " + a.getNombre() + 
                    " - Finca: " + a.getFinca() + 
                    " - Ubicación: " + a.getUbicacion());
            } else {
                return new ResponseDto(false, "Agricultor no encontrado con ID: " + id);
            }
        } catch (Exception e) {
            return new ResponseDto(false, "Error al buscar agricultor: " + e.getMessage());
        }
    }

    public ResponseDto actualizarAgricultor(Long id, AgricultorDto dto) {
        try {
            Optional<Agricultor> agricultorOpt = agricultorRepository.findById(id);
            if (agricultorOpt.isPresent()) {
                Agricultor agricultor = agricultorOpt.get();
                agricultor.setAgricultorId(dto.getAgricultorId());
                agricultor.setNombre(dto.getNombre());
                agricultor.setFinca(dto.getFinca());
                agricultor.setUbicacion(dto.getUbicacion());
                agricultor.setCorreo(dto.getCorreo());

                agricultorRepository.save(agricultor);
                return new ResponseDto(true, "Agricultor actualizado exitosamente");
            } else {
                return new ResponseDto(false, "Agricultor no encontrado con ID: " + id);
            }
        } catch (Exception e) {
            return new ResponseDto(false, "Error al actualizar agricultor: " + e.getMessage());
        }
    }
}
