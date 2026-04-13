package restaurant.menu.api.app.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import restaurant.menu.api.app.domain.database.repositories.OrderRepository;
import restaurant.menu.api.app.domain.dto.ActiveOrders;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrderRepository orderRepository;

    public List<ActiveOrders> findAllOrderActive(){
        return orderRepository.findAllByStatusInProgress().stream().map(ActiveOrders::new).toList();
    }
}
