package restaurant.menu.api.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import restaurant.menu.api.app.domain.dto.*;
import restaurant.menu.api.app.services.EmployeeService;
import restaurant.menu.api.app.services.MenuService;
import restaurant.menu.api.app.services.OrdersService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService service;
    private final OrdersService ordersService;
    private final MenuService menuService;

    public EmployeeController(EmployeeService service, OrdersService ordersService, MenuService menuService) {
        this.service = service;
        this.ordersService = ordersService;
        this.menuService = menuService;
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

    @DeleteMapping("/orders/{orderId}/cancel")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> cancelOrder(@PathVariable String orderId){
        ordersService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/orders/{orderId}/delivered")
    @PreAuthorize("hasAnyRole('WAITER', 'MANAGER')")
    public ResponseEntity<Void> deliveredOrder(@PathVariable String orderId){
        ordersService.deliveredOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/menu/{itemId}/disable")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ItemsDetails> disableMenuItem(@PathVariable Long itemId){
        var item = menuService.disableMenuItem(itemId);
        return ResponseEntity.ok(item);
    }
}
