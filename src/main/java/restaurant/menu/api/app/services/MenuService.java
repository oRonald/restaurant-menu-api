package restaurant.menu.api.app.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurant.menu.api.app.domain.database.entities.Menu;
import restaurant.menu.api.app.domain.database.repositories.MenuRepository;
import restaurant.menu.api.app.domain.dto.AddMenuItemRequest;
import restaurant.menu.api.app.domain.dto.ItemsDetails;
import restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions.MenuItemException;
import restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions.MenuItemNotFoundException;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository repository;

    @Transactional
    public ItemsDetails disableMenuItem(Long itemId) {
        var menuItem = repository.findById(itemId).orElseThrow(() -> new MenuItemNotFoundException("Item não encontrado"));
        if(!menuItem.getActive()){
            throw new MenuItemException("Este item já esta desabilitado");
        }
        menuItem.setActive(false);
        repository.save(menuItem);
        return new ItemsDetails(menuItem);
    }

    @Transactional
    public ItemsDetails addMenuItem(AddMenuItemRequest request) {
        System.out.println(request.name());
        if(repository.existsByName(request.name())){
            throw new MenuItemException("Já existe um item com esse nome");
        }
        var item = new Menu(request);
        repository.save(item);
        return new ItemsDetails(item);
    }
}
