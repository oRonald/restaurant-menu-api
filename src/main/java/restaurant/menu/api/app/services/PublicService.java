package restaurant.menu.api.app.services;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import restaurant.menu.api.app.domain.database.entities.Menu;
import restaurant.menu.api.app.domain.database.entities.OrderItems;
import restaurant.menu.api.app.domain.database.entities.Orders;
import restaurant.menu.api.app.domain.database.entities.enums.OrderStatus;
import restaurant.menu.api.app.domain.database.repositories.MenuRepository;
import restaurant.menu.api.app.domain.database.repositories.OrderRepository;
import restaurant.menu.api.app.domain.dto.ActiveOrders;
import restaurant.menu.api.app.domain.dto.ItemsDetails;
import restaurant.menu.api.app.domain.dto.OrderDetails;
import restaurant.menu.api.app.domain.dto.OrderRequest;
import restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions.DishNotFoundException;
import restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions.ExistingTableOrderException;
import restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions.OrderNotFoundException;
import restaurant.menu.api.app.infrastructure.kafka.OrderProducer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderProducer orderProducer;

    @Transactional
    public OrderDetails createOrder(OrderRequest request){
        Menu menu = menuRepository.searchByName(request.menuItem()).orElseThrow(() -> new DishNotFoundException("O prato solicitado não foi encontrado no menu. Por favor, verifique o nome do prato e tente novamente."));
        Orders orderExisting = orderRepository.findByTableNumber(request.tableNumber());

        if(orderExisting != null && orderExisting.getStatus() != OrderStatus.DELIVERED && orderExisting.getStatus() != OrderStatus.CANCELLED){
            throw new ExistingTableOrderException("Não é possível realizar outro pedido em uma mesa que está com um pedido em andamento. Por favor, espere até que o pedido seja entregue ou cancelado.");
        }

        Orders order = new Orders(request);

        BigDecimal tip = (request.tip() != null) ? request.tip() : BigDecimal.ZERO;
        BigDecimal serviceCharge = new BigDecimal("2");

        BigDecimal priceWithTax = menu.getPrice();

        if (menu.getPrice().compareTo(new BigDecimal("30")) > 0) {
            BigDecimal taxRate = new BigDecimal("0.30");

            BigDecimal taxValue = menu.getPrice()
                    .multiply(taxRate)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

            priceWithTax = priceWithTax.add(taxValue);
        } else if (menu.getPrice().compareTo(new BigDecimal("50")) > 0){
            BigDecimal taxRate = new BigDecimal("0.45");

            BigDecimal taxValue = menu.getPrice()
                    .multiply(taxRate)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

            priceWithTax = priceWithTax.add(taxValue);
        }

        order.addTotalPrice(priceWithTax, tip, request.quantity(), serviceCharge);

        OrderItems orderItems = new OrderItems(order, menu);
        order.setOrderItems(orderItems);

        if(order.getTip() == null){
            order.setTip(BigDecimal.ZERO);
        }

        String orderId = orderRepository.save(order).getOrderId();
        orderProducer.publish(orderId);

        return new OrderDetails(order, serviceCharge);
    }

    @Cacheable(value = "menu", key = "'all'")
    public List<ItemsDetails> getAllMenuItems(){
        List<Menu> menuItems = menuRepository.findAll();
        return menuItems.stream().map(ItemsDetails::new).toList();
    }

    public ActiveOrders getOrderByTableNumber(Integer tableNumber, String customer){
        Orders order = orderRepository.findByTableNumberAndCustomerNameContainingAndStatus(tableNumber, customer, OrderStatus.IN_PROGRESS);
        if(order == null){
            throw new OrderNotFoundException("Não foi encontrado um pedido em andamento para a mesa " + tableNumber + " e cliente " + customer + ". Por favor, verifique as informações e tente novamente.");
        }
        return new ActiveOrders(order);
    }
}
