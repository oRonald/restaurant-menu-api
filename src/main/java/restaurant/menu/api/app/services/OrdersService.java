package restaurant.menu.api.app.services;

import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurant.menu.api.app.domain.database.entities.Orders;
import restaurant.menu.api.app.domain.database.entities.enums.OrderStatus;
import restaurant.menu.api.app.domain.database.repositories.OrderRepository;
import restaurant.menu.api.app.domain.dto.ActiveOrders;
import restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions.CancellationOrderNotPossibleException;
import restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions.OrderNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrderRepository orderRepository;

    public List<ActiveOrders> findAllOrderActive(){
        return orderRepository.findAllByStatusInProgress().stream().map(ActiveOrders::new).toList();
    }

    @Transactional
    public void changeOrderStatusToReady(String orderId){
        orderRepository.updateStatusByOrderId(OrderStatus.READY, orderId);
    }

    @Transactional
    public void cancelOrder(String orderId){
        if(!orderRepository.existsByOrderId(orderId)){
            throw new OrderNotFoundException("Pedido com ID " + orderId + " não encontrado.");
        }

        Orders order = orderRepository.findByOrderId(orderId);
        if(order.getStatus().equals(OrderStatus.READY) | order.getStatus().equals(OrderStatus.DELIVERED)){
            throw new CancellationOrderNotPossibleException("Não é possível cancelar um pedido que já está pronto ou entregue.");
        }
        orderRepository.updateOrderStatusToCancelled(orderId);
    }
}
