package restaurant.menu.api.app.domain.database.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import restaurant.menu.api.app.domain.dto.AddMenuItemRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor

@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "menuItems")
    private List<OrderItems> orderItems;

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Menu(AddMenuItemRequest item){
        this.name = item.name();
        this.description = item.description();
        this.price = item.price();
        this.category = item.category();
        this.orderItems = new ArrayList<>();
    }
}
