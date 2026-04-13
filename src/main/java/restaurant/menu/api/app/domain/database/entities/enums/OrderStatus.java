package restaurant.menu.api.app.domain.database.entities.enums;

public enum OrderStatus {

    PENDING("Pendente"),
    IN_PROGRESS("Em andamento"),
    READY("Pronto"),
    DELIVERED("Entregue"),
    CANCELLED("Cancelado");

    private final String label;

    OrderStatus(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}
