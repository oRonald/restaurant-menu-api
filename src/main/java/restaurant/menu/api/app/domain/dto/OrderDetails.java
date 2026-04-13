package restaurant.menu.api.app.domain.dto;

import restaurant.menu.api.app.domain.database.entities.Orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDetails(
            Integer mesa,
            String item,
            Integer quantidade,
            BigDecimal total,
            BigDecimal gorjeta,
            BigDecimal taxaServico,
            String mensagem
) {
    public OrderDetails(Orders order, BigDecimal serviceCharge){
        this(order.getTableNumber(), order.getOrderItems().getMenuItems().getName(), order.getQuantity(), order.getTotal(),
                order.getTip(), serviceCharge, "O valor total é calculado junto com a gorjeta, taxa de serviço e impostos");
    }
}
