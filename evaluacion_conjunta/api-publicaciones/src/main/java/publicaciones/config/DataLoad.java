package publicaciones.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import publicaciones.model.Agricultor;
import publicaciones.model.Cosecha;
import publicaciones.model.Factura;
import publicaciones.repository.AgricultorRepository;
import publicaciones.repository.CosechaRepository;
import publicaciones.repository.FacturaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DataLoad implements CommandLineRunner {

    @Autowired
    private AgricultorRepository agricultorRepository;

    @Autowired
    private CosechaRepository cosechaRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Cargando datos de prueba...");

        // Crear agricultores de prueba
        Agricultor agricultor1 = new Agricultor();
        agricultor1.setAgricultorId(UUID.randomUUID().toString());
        agricultor1.setNombre("Juan Pérez");
        agricultor1.setFinca("Finca El Paraíso");
        agricultor1.setUbicacion("9.7489°N, 83.7534°W");
        agricultor1.setCorreo("juan.perez@email.com");
        agricultorRepository.save(agricultor1);

        Agricultor agricultor2 = new Agricultor();
        agricultor2.setAgricultorId(UUID.randomUUID().toString());
        agricultor2.setNombre("María González");
        agricultor2.setFinca("Finca La Esperanza");
        agricultor2.setUbicacion("9.8500°N, 83.9000°W");
        agricultor2.setCorreo("maria.gonzalez@email.com");
        agricultorRepository.save(agricultor2);

        // Crear cosechas de prueba
        Cosecha cosecha1 = new Cosecha();
        cosecha1.setAgricultor(agricultor1);
        cosecha1.setProducto("Arroz Oro");
        cosecha1.setToneladas(new BigDecimal("15.5"));
        cosecha1.setEstado("REGISTRADA");
        cosechaRepository.save(cosecha1);

        Cosecha cosecha2 = new Cosecha();
        cosecha2.setAgricultor(agricultor2);
        cosecha2.setProducto("Café Premium");
        cosecha2.setToneladas(new BigDecimal("8.2"));
        cosecha2.setEstado("REGISTRADA");
        cosechaRepository.save(cosecha2);

        // Crear facturas de prueba
        Factura factura1 = new Factura();
        factura1.setCosechaId(cosecha1.getCosechaId());
        factura1.setMontoTotal(new BigDecimal("1860.00"));
        factura1.setPagado(false);
        facturaRepository.save(factura1);

        Factura factura2 = new Factura();
        factura2.setCosechaId(cosecha2.getCosechaId());
        factura2.setMontoTotal(new BigDecimal("2460.00"));
        factura2.setPagado(false);
        facturaRepository.save(factura2);

        System.out.println("Datos de prueba cargados exitosamente!");
    }
}
