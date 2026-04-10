package restaurant.menu.api.app.domain.database.entities;

import jakarta.persistence.*;
import restaurant.menu.api.app.domain.database.entities.enums.EmployeeRole;

import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
public class Employees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeeRole role;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
