package ec.edu.espe.inventario.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE_COSECHAS = "cosechas";
    public static final String QUEUE_INVENTARIO = "cola_inventario";
    public static final String ROUTING_KEY_NUEVA = "nueva";

    @Bean
    public TopicExchange exchangeCosechas() {
        return new TopicExchange(EXCHANGE_COSECHAS);
    }

    @Bean
    public Queue queueInventario() {
        return new Queue(QUEUE_INVENTARIO, true);
    }

    @Bean
    public Binding bindingInventario() {
        return BindingBuilder
                .bind(queueInventario())
                .to(exchangeCosechas())
                .with(ROUTING_KEY_NUEVA);
    }
}
