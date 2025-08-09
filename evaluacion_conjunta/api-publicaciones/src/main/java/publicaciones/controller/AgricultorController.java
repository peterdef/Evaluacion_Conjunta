package publicaciones.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import publicaciones.dto.AgricultorDto;
import publicaciones.dto.ResponseDto;
import publicaciones.model.Agricultor;
import publicaciones.service.AgricultorService;

import java.util.List;

@RestController
@RequestMapping("/agricultores")
public class AgricultorController {

    @Autowired
    private AgricultorService agricultorService;

    @GetMapping("/test")
    public String test(){
        return "OK";
    }

    @PostMapping
    public ResponseDto crearAgricultor(@RequestBody @Valid AgricultorDto dto) {
        return agricultorService.crearAgricultor(dto);
    }

    @GetMapping
    public List<ResponseDto> obtenerAgricultores() {
        return agricultorService.listarAgricultores();
    }

    @GetMapping("/todos")
    public List<Agricultor> listarAgricultores() {
        return agricultorService.agricultores();
    }

    @GetMapping("/{id}")
    public ResponseDto buscarPorId(@PathVariable Long id) {
        return agricultorService.agricultorPorId(id);
    }

    @PutMapping("/{id}")
    public ResponseDto actualizarAgricultor(@PathVariable Long id, @RequestBody @Valid AgricultorDto dto) {
        return agricultorService.actualizarAgricultor(id, dto);
    }
}
