package restaurant.menu.api.app.domain.database.repositories;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.menu.api.app.domain.database.entities.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Orders findByTableNumber(Integer tableNumber);
}
