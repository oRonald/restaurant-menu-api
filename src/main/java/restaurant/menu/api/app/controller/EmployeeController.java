package restaurant.menu.api.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restaurant.menu.api.app.domain.dto.RegisterEmployeeRequest;
import restaurant.menu.api.app.services.EmployeeService;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping("/admin/register")
    public ResponseEntity<Void> registerNewEmployee(@RequestBody RegisterEmployeeRequest request) {
        service.registerNewEmployee(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
