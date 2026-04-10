package restaurant.menu.api.app.services;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import restaurant.menu.api.app.domain.database.entities.Employees;
import restaurant.menu.api.app.domain.database.entities.enums.EmployeeRole;
import restaurant.menu.api.app.domain.database.repositories.EmployeeRepository;
import restaurant.menu.api.app.domain.dto.RegisterEmployeeRequest;
import restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions.RoleNotFoundException;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;
    private final PasswordEncoder encoder;

    @Transactional
    public void registerNewEmployee(@Valid RegisterEmployeeRequest request) {
        EmployeeRole.deString(request.role());

        Employees employee = new Employees(request, encoder.encode(request.password()));
        repository.save(employee);
    }
}
