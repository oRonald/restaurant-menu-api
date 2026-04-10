package restaurant.menu.api.app.domain.database.entities.enums;

import restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions.RoleNotFoundException;

import java.util.Arrays;
import java.util.List;

public enum EmployeeRole {

    MANAGER,
    CHEF,
    WAITER;

    public static EmployeeRole deString(String role){
        for(EmployeeRole statusAppointment : EmployeeRole.values()){
            if(statusAppointment.name().equalsIgnoreCase(role)){
                return statusAppointment;
            }
        }

        List<String> valid = Arrays.stream(EmployeeRole.values())
                .map(Enum::name)
                .toList();

        throw new RoleNotFoundException("Invalid role: '" + role + "'. The types are: " + valid);
    }
}
