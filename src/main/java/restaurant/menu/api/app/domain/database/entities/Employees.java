package restaurant.menu.api.app.domain.database.entities;

import jakarta.persistence.*;
import restaurant.menu.api.app.domain.database.entities.enums.EmployeeRole;
import restaurant.menu.api.app.domain.dto.RegisterEmployeeRequest;

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

    public Employees(RegisterEmployeeRequest request, String hashedPassword) {
        this.username = request.username();
        this.password = hashedPassword;
        this.role = EmployeeRole.valueOf(request.role());
    }
}
