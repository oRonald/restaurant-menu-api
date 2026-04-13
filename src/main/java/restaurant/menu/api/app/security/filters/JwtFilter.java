package restaurant.menu.api.app.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import restaurant.menu.api.app.domain.database.repositories.EmployeeRepository;
import restaurant.menu.api.app.security.JWTImplementation;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private JWTImplementation jwtImplementation;

    private EmployeeRepository employeeRepository;

    public JwtFilter(JWTImplementation jwtImplementation, EmployeeRepository employeeRepository) {
        this.jwtImplementation = jwtImplementation;
        this.employeeRepository = employeeRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = recoveryToken(request);
        if(token != null){
            var subject = jwtImplementation.getSubject(token);
            var employee = employeeRepository.findByUsername(subject);
            var authentication = new UsernamePasswordAuthenticationToken(employee, null, employee.get().getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String recoveryToken(HttpServletRequest request) {
        var header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.replace("Bearer ", "");
        }
         return null;
    }
}
