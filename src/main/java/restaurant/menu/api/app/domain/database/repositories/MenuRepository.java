package restaurant.menu.api.app.domain.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.menu.api.app.domain.database.entities.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
