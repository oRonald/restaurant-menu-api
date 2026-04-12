package restaurant.menu.api.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restaurant.menu.api.app.domain.dto.OrderDetails;
import restaurant.menu.api.app.domain.dto.OrderRequest;
import restaurant.menu.api.app.services.PublicService;

import java.net.URI;

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
}
