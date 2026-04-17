package restaurant.menu.api.app.domain.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import restaurant.menu.api.app.domain.database.entities.Employees;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employees, Long> {
    Optional<Employees> findByUsername(String username);

    boolean existsByUsername(String username);
}
