package restaurant.menu.api.app.controller;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restaurant.menu.api.app.domain.dto.ActiveOrders;
import restaurant.menu.api.app.domain.dto.ItemsDetails;
import restaurant.menu.api.app.domain.dto.OrderDetails;
import restaurant.menu.api.app.domain.dto.OrderRequest;
import restaurant.menu.api.app.services.PublicService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {

    private final PublicService service;

    public PublicController(PublicService service) {
        this.service = service;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderDetails> createOrder(@RequestBody OrderRequest request) {
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
