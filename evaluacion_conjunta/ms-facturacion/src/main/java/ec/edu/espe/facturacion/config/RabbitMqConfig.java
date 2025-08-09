package ec.edu.espe.facturacion.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE_COSECHAS = "cosechas";
    public static final String QUEUE_FACTURACION = "cola_facturacion";
    public static final String ROUTING_KEY_NUEVA = "nueva";

    @Bean
    public TopicExchange exchangeCosechas() {
        return new TopicExchange(EXCHANGE_COSECHAS);
    }

    @Bean
    public Queue queueFacturacion() {
        return new Queue(QUEUE_FACTURACION, true);
    }

    @Bean
    public Binding bindingFacturacion() {
        return BindingBuilder
                .bind(queueFacturacion())
                .to(exchangeCosechas())
                .with(ROUTING_KEY_NUEVA);
    }
}
