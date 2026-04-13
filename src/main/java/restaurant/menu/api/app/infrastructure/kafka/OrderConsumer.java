package restaurant.menu.api.app.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import restaurant.menu.api.app.domain.database.repositories.OrderRepository;

@Component
public class OrderConsumer {

    private final OrderRepository repository;

    public OrderConsumer(OrderRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "orders", groupId = "restaurant-group")
    public void consume(String message) {
        repository.updateOrderStatus(message);
    }
}
