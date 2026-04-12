package restaurant.menu.api.app.domain.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import restaurant.menu.api.app.domain.database.entities.Menu;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT m FROM Menu m WHERE m.name LIKE %:name% AND m.active = true")
    Optional<Menu> searchByName(String name);
}
