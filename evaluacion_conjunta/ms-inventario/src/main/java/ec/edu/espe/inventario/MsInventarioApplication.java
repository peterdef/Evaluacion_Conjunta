package ec.edu.espe.inventario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsInventarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsInventarioApplication.class, args);
    }
}
