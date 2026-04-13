package restaurant.menu.api.app.services;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.internal.util.OsUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import restaurant.menu.api.app.domain.database.entities.Employees;
import restaurant.menu.api.app.domain.database.entities.enums.EmployeeRole;
import restaurant.menu.api.app.domain.database.repositories.EmployeeRepository;
import restaurant.menu.api.app.domain.dto.LoginEmployee;
import restaurant.menu.api.app.domain.dto.RegisterEmployeeRequest;
import restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions.RoleNotFoundException;
import restaurant.menu.api.app.security.JWTImplementation;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;
    private final PasswordEncoder encoder;
    private final JWTImplementation jwt;
    private final AuthenticationManager manager;

    @Transactional
    public void registerNewEmployee(@Valid RegisterEmployeeRequest request) {
        EmployeeRole.deString(request.role());

        Employees employee = new Employees(request, encoder.encode(request.password()));
        repository.save(employee);
    }

    public String loginEmployee(@Valid LoginEmployee login) {
        var authToken = new UsernamePasswordAuthenticationToken(
                login.username(),
                login.password()
        );

        var authentication = manager.authenticate(authToken);

        var employee = (Employees) authentication.getPrincipal();

        return jwt.generateToken(employee);
    }
}
