package restaurant.menu.api.app.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import restaurant.menu.api.app.domain.database.entities.Orders;
import restaurant.menu.api.app.domain.database.entities.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ActiveOrders(
        String numeroPedido,
        String item,
        Integer mesa,
        String total,
        String status,
        Integer quantidade,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime ultimaAtualizacao,
        String gorjeta

) {
    public ActiveOrders(Orders order){
        this(
                order.getOrderId(),
                order.getOrderItems().getMenuItems().getName(),
                order.getTableNumber(),
                "R$" + order.getTotal(),
                order.getStatus().getLabel(),
                order.getQuantity(),
                order.getLastUpdated(),
                "R$" + order.getTip()
        );
    }
}
