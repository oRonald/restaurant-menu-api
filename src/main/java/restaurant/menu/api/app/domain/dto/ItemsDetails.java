package restaurant.menu.api.app.domain.dto;

import restaurant.menu.api.app.domain.database.entities.Menu;

public record ItemsDetails(
        String nome,
        String descricao,
        String categoria,
        String valor
) {
    public ItemsDetails(Menu menu){
        this(menu.getName(), menu.getDescription(), menu.getCategory(), "R$" + menu.getPrice().toString());
    }
}
