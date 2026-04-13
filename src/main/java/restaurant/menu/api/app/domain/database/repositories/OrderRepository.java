package restaurant.menu.api.app.domain.database.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import restaurant.menu.api.app.domain.database.entities.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Orders findByTableNumber(Integer tableNumber);

    @Transactional
    @Modifying
    @Query("UPDATE Orders o SET o.status = 'IN_PROGRESS' WHERE o.orderId = :orderId")
    void updateOrderStatus(String orderId);
}
