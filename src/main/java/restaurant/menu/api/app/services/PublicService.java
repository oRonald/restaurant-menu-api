package restaurant.menu.api.app.services;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import restaurant.menu.api.app.domain.database.entities.Menu;
import restaurant.menu.api.app.domain.database.entities.OrderItems;
import restaurant.menu.api.app.domain.database.entities.Orders;
import restaurant.menu.api.app.domain.database.entities.enums.OrderStatus;
import restaurant.menu.api.app.domain.database.repositories.MenuRepository;
import restaurant.menu.api.app.domain.database.repositories.OrderRepository;
import restaurant.menu.api.app.domain.dto.OrderDetails;
import restaurant.menu.api.app.domain.dto.OrderRequest;
import restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions.ExistingTableOrderException;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class PublicService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public OrderDetails createOrder(@Valid OrderRequest request){
        Menu menu = menuRepository.findById(request.menuItem()).orElseThrow(() -> new IllegalArgumentException("O item do menu com ID " + request.menuItem() + " não existe."));

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

        orderRepository.save(order);
        return new OrderDetails(order, serviceCharge);
    }
}
