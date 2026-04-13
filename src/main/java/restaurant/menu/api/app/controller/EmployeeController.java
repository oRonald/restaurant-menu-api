package restaurant.menu.api.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import restaurant.menu.api.app.domain.dto.ActiveOrders;
import restaurant.menu.api.app.domain.dto.LoginEmployee;
import restaurant.menu.api.app.domain.dto.RegisterEmployeeRequest;
import restaurant.menu.api.app.domain.dto.TokenJWT;
import restaurant.menu.api.app.services.EmployeeService;
import restaurant.menu.api.app.services.OrdersService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService service;
    private final OrdersService ordersService;

    public EmployeeController(EmployeeService service, OrdersService ordersService) {
        this.service = service;
        this.ordersService = ordersService;
    }

    @PostMapping("/admin/register")
    public ResponseEntity<Void> registerNewEmployee(@RequestBody RegisterEmployeeRequest request) {
        service.registerNewEmployee(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenJWT> loginEmployee(@RequestBody LoginEmployee login){
        var token = service.loginEmployee(login);
        return ResponseEntity.ok(new TokenJWT(token));
    }

    @GetMapping("/orders/active")
    public ResponseEntity<List<ActiveOrders>> getAllActiveOrders(){
        return ResponseEntity.ok(ordersService.findAllOrderActive());
    }

    @PatchMapping("/orders/{orderId}/ready")
    @PreAuthorize("hasAnyRole('CHEF', 'MANAGER')")
    public ResponseEntity<Void> changeOrderStatusToReady(@PathVariable String orderId){
        ordersService.changeOrderStatusToReady(orderId);
        return ResponseEntity.noContent().build();
    }
}
