package restaurant.menu.api.app.domain.database.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

@Entity
@Table(name = "order_items")
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menuItems;

    public OrderItems(Orders order, Menu menuItems) {
        this.order = order;
        this.menuItems = menuItems;
    }
}
