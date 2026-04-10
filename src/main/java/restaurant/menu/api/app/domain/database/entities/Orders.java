package restaurant.menu.api.app.domain.database.entities;

import jakarta.persistence.*;
import restaurant.menu.api.app.domain.database.entities.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderId;

    @Column(nullable = false)
    private Integer tableNumber;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(insertable = false)
    private LocalDateTime lastUpdated;

    @Column(precision = 10, scale = 2)
    private BigDecimal tip;
}
