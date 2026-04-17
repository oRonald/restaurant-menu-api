package restaurant.menu.api.app.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import restaurant.menu.api.app.controller.interfaces.IPublicController;
import restaurant.menu.api.app.domain.dto.ActiveOrders;
import restaurant.menu.api.app.domain.dto.ItemsDetails;
import restaurant.menu.api.app.domain.dto.OrderDetails;
import restaurant.menu.api.app.domain.dto.OrderRequest;
import restaurant.menu.api.app.services.PublicService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public")
@Validated
public class PublicController implements IPublicController {

    private final PublicService service;

    public PublicController(PublicService service) {
        this.service = service;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderDetails> createOrder(@RequestBody @Valid OrderRequest request) {
        var orderDetails = service.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDetails);
    }

    @GetMapping("/menu")
    public ResponseEntity<List<ItemsDetails>> getAllMenuItems(){
        return ResponseEntity.ok(service.getAllMenuItems());
    }

    @GetMapping("/orders/{tableNumber}/{customer}")
    public ResponseEntity<ActiveOrders> getOrderByTableNumber(@PathVariable Integer tableNumber, @PathVariable String customer){
        return ResponseEntity.ok(service.getOrderByTableNumber(tableNumber, customer));
    }
}
