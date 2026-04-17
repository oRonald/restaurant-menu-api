package restaurant.menu.api.app.domain.database.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.menu.api.app.domain.database.entities.enums.OrderStatus;
import restaurant.menu.api.app.domain.dto.OrderRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderId;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private Integer tableNumber;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private Integer quantity;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(insertable = false)
    private LocalDateTime lastUpdated;

    @Column(precision = 10, scale = 2)
    private BigDecimal tip;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrderItems orderItems;

    public Orders(OrderRequest request){
        this.tableNumber = request.tableNumber();
        this.customerName = request.customerName();
        this.orderId = "ORD-" + String.format("%03d", tableNumber);
        this.status = OrderStatus.PENDING;
        this.quantity = request.quantity();
        this.tip = request.tip();
    }

    public void addTotalPrice(BigDecimal price, BigDecimal tip, int quantity, BigDecimal serviceCharge) {
        this.total = price.multiply(BigDecimal.valueOf(quantity)).add(tip).add(serviceCharge);
    }

}
