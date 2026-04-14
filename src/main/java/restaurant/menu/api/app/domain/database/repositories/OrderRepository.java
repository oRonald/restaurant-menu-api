package restaurant.menu.api.app.domain.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import restaurant.menu.api.app.domain.database.entities.Orders;
import restaurant.menu.api.app.domain.database.entities.enums.OrderStatus;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Orders findByTableNumber(Integer tableNumber);

    @Transactional
    @Modifying
    @Query("UPDATE Orders o SET o.status = 'IN_PROGRESS' WHERE o.orderId = :orderId")
    void updateOrderStatus(String orderId);

    @Transactional
    @Modifying
    @Query("UPDATE Orders o SET o.status = :status WHERE o.orderId = :orderId AND o.status = 'IN_PROGRESS'")
    void updateStatusByOrderId(OrderStatus status, String orderId);

    @Query("SELECT o FROM Orders o JOIN FETCH o.orderItems WHERE o.status = 'IN_PROGRESS'")
    List<Orders> findAllByStatusInProgress();

    Orders findByTableNumberAndCustomerNameContainingAndStatus(Integer tableNumber, String customer, OrderStatus status);

    @Transactional
    @Modifying
    @Query("UPDATE Orders o SET o.status = 'CANCELLED' WHERE o.orderId = :orderId AND o.status != 'READY' AND o.status != 'DELIVERED'")
    void updateOrderStatusToCancelled(String orderId);

    boolean existsByOrderId(String orderId);

    Orders findByOrderId(String orderId);
}
