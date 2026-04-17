package restaurant.menu.api.app.controller.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import restaurant.menu.api.app.domain.dto.*;
import restaurant.menu.api.app.infrastructure.exceptionHandling.ExceptionTemplate;

import java.util.List;

public interface IEmployeeController {

    @Operation(
            summary = "Register a new employee",
            description = "Allows to register a new employee in the system using a secret master key admin. Roles that can be assigned are: MANAGER, CHEF, WAITER."
    )
    @Parameter(
            name = "X-API-KEY",
            description = "Secret key required for employee registration",
            required = true,
            in = ParameterIn.HEADER
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employee registered successfully",
            content = @Content(
                    mediaType = "application/json"
            )),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing master key",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Unauthorized",
                            summary = "Invalid or missing master key provided for registration",
                            value = "Unauthorized: Invalid API Key"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "Role not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionTemplate.class),
                    examples = @ExampleObject(
                                    name = "Role Not Found",
                                    summary = "Invalid role provided",
                                    value = """
                                    {
                                    "message": "Invalid role: 'EMPLOYEE'. The types are: [MANAGER, CHEF, WAITER]",
                                    "status": 404,
                                    "statusError": "NOT_FOUND",
                                    "timestamp": "2026-04-16T20:10:00"
                                    }
                                    """)
            )),
            @ApiResponse(
                    responseCode = "409",
                    description = "Employee already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionTemplate.class),
                    examples = @ExampleObject(
                            name = "Employee Already Exists",
                            summary = "An employee with the provided username already exists",
                            value = """
                                    {
                                    "message": "An employee with this username already exists: john_doe",
                                    "status": 409,
                                    "statusError": "CONFLICT",
                                    "timestamp": "2026-04-16T20:10:00"
                                    }
                                    """
                    ))),
            @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MethodArgumentNotValidException.class),
                    examples = @ExampleObject(
                            name = "Validation Error",
                            summary = "Input validation failed for the request body",
                            value = """
                                    [
                                    "Atributo: 'username' não deve estar em branco"
                                    ]
                                    """
                    )
            ))
    })
    ResponseEntity<Void> registerNewEmployee(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Employee registration details",
                    required = true
            ) @RequestBody @Valid RegisterEmployeeRequest request);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Add a new menu item",
            description = "Allows a manager to add a new item to the restaurant menu."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu item added successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ItemsDetails.class),
                    examples = @ExampleObject(
                            name = "Menu Item Details",
                            summary = "Details of the new menu item",
                            value = """
                                    {
                                      "nome": "Feijoada",
                                      "descricao": "Feijoada completa com arroz, farofa e couve",
                                      "preco": 30,
                                      "categoria": "Prato Principal"
                                    }
                                    """
                    )
            )),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User does not have permission to add menu items",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Employee not allowed",
                                    summary = "Employee does not have the required role to perform this action",
                                    value = """
                                            {
                                                "error": "Negado",
                                                "message": "Sem permissão de acesso!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Menu item exists",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionTemplate.class),
                    examples = @ExampleObject(
                            name = "Menu Item Exists",
                            summary = "A menu item already exists",
                            value = """
                                    {
                                    "message": "Já existe um item com esse nome: Feijoada",
                                    "status": 400,
                                    "statusError": "BAD_REQUEST",
                                    "timestamp": "2026-04-16T20:10:00"
                                    }
                                    """
                    )
            ))
    })
    ResponseEntity<ItemsDetails> addMenuItem(@RequestBody @Valid AddMenuItemRequest request);

    @Operation(
            summary = "Employee login",
            description = "Allows an employee to log in and receive a JWT token for authentication."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TokenJWT.class),
                    examples = @ExampleObject(
                            name = "Login Response",
                            summary = "JWT token returned upon successful login",
                            value = """
                                    {
                                     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJBUEkgUmVzdGF1cmFudCIsInN1YiI6InJvbmFsZFdhaXRlciIsInJvbGUiOiJXQUlURVIiLCJleHAiOjE3NzY0Mzg5Njh9.MgE-XvwRRcM9d87pxO8gI9C9Veb9TU3hucD_ePBoZFg"
                                    }
                                    """
                    )
            )),
            @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MethodArgumentNotValidException.class),
                    examples = @ExampleObject(
                            name = "Validation Error",
                            summary = "Input validation failed for the login request",
                            value = """
                                    [
                                    "Atributo: 'username' não deve estar em branco"
                                    ]
                                    """
                    )
            )),
            @ApiResponse(responseCode = "401", description = "Unauthorized login",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Unauthorized",
                            summary = "Invalid credentials provided for login",
                            value = """
                                    {
                                        "message": "Bad credentials",
                                        "status": 401,
                                        "statusError": "UNAUTHORIZED",
                                        "timestamp": "2026-04-17T11:36:21.2680631"
                                    }
                                    """
                    )
            ))
    })
    ResponseEntity<TokenJWT> loginEmployee(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Employee login credentials",
                    required = true
            ) @RequestBody @Valid LoginEmployee login);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Get all active orders",
            description = "Allows employees to view all active orders in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Active orders retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ActiveOrders.class),
                    examples = {
                            @ExampleObject(
                                    name = "Active Orders",
                                    summary = "List of active orders",
                                    value = """
                                    [
                                    {
                                     "cliente": "Ronald de Oliveira",
                                     "numeroPedido": "123",
                                     "item": "Feijoada",
                                     "mesa": 5,
                                     "total": "R$30.00",
                                     "status": "Em Progresso",
                                     "quantidade": 1,
                                     "ultimaAtualizacao": "16/04/2026 20:10:00",
                                     "gorjeta": "R$5.00"
                                    }
                                    ]
                                    """
                            ),
                            @ExampleObject(
                                    name = "No Active Orders",
                                    summary = "Response when there are no active orders",
                                    value = "[]"
                            )
                    }
            ))
    })
    ResponseEntity<List<ActiveOrders>> getAllActiveOrders();

    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Change order status to ready",
            description = "Allows chefs and managers to change the status of an order to 'ready' when it is prepared."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order status changed to ready successfully"),
            @ApiResponse(responseCode = "404", description = "Not Found - Order with the specified ID does not exist",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionTemplate.class),
                    examples = @ExampleObject(
                            name = "Order Not Found",
                            summary = "No order found",
                            value = """
                                    {
                                        "message": "Pedido com ID 123 não encontrado.",
                                        "status": 404,
                                        "statusError": "NOT_FOUND",
                                        "timestamp": "2026-04-17T11:36:21.2680631"
                                    }
                                    """
                    )
            ))
    })
    ResponseEntity<Void> changeOrderStatusToReady(
            @Parameter(
                    description = "ID of the order to change status",
                    required = true,
                    example = "123"
            ) @PathVariable String orderId);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Cancel an order",
            description = "Allows managers to cancel an order that is not yet ready or delivered."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Not Found - Order with the specified ID does not exist",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionTemplate.class),
                    examples = @ExampleObject(
                            name = "Order Not Found",
                            summary = "No order found",
                            value = """
                                    {
                                        "message": "Pedido com ID 123 não encontrado.",
                                        "status": 404,
                                        "statusError": "NOT_FOUND",
                                        "timestamp": "2026-04-17T11:36:21.2680631"
                                    }
                                    """
                    )
            )),
            @ApiResponse(responseCode = "400", description = "Bad Request - Order cannot be cancelled because its status is ready or delivered",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionTemplate.class),
                    examples = @ExampleObject(
                            name = "Cancellation Not Possible",
                            summary = "Order cannot be cancelled due to its current status",
                            value = """
                                    {
                                        "message": "Não é possível cancelar um pedido que já está pronto ou entregue.",
                                        "status": 400,
                                        "statusError": "BAD_REQUEST",
                                        "timestamp": "2026-04-17T11:36:21.2680631"
                                    }
                                    """
                    )
            ))
    })
    ResponseEntity<Void> cancelOrder(
           @Parameter(
                   description = "ID of the order to cancel",
                   required = true,
                   example = "123"
           ) @PathVariable String orderId);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Mark an order as delivered",
            description = "Allows waiters and managers to mark an order as delivered when it has been served to the customer.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order marked as delivered successfully"),
            @ApiResponse(responseCode = "404", description = "Not Found - Order with the specified ID does not exist",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionTemplate.class),
                    examples = @ExampleObject(
                            name = "Order Not Found",
                            description = "No order found with the specified ID",
                            value = """
                                    {
                                        "message": "Pedido com ID 123 não encontrado.",
                                        "status": 404,
                                        "statusError": "NOT_FOUND",
                                        "timestamp": "2026-04-17T11:36:21.2680631"
                                    }
                                    """
                    )
            )),
            @ApiResponse(responseCode = "400", description = "Bad Request - Order cannot be marked as delivered because it is not ready, already delivered, or cancelled",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionTemplate.class),
                    examples = @ExampleObject(
                            name = "Delivery Not Possible",
                            description = "Order cannot be marked as delivered due to its current status",
                            value = """
                                    {
                                        "message": "O pedido 123 já foi entregue ou não está pronto ou foi cancelado.",
                                        "status": 400,
                                        "statusError": "BAD_REQUEST",
                                        "timestamp": "2026-04-17T11:36:21.2680631"
                                    }
                                    """
                    )
            ))
    })
    ResponseEntity<Void> deliveredOrder(
            @Parameter(
                    description = "ID of the order to mark as delivered",
                    required = true,
                    example = "123"
            ) @PathVariable String orderId);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Disable a menu item",
            description = "Allows managers to disable a menu item, making it unavailable for ordering."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu item disabled successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ItemsDetails.class),
                    examples = @ExampleObject(
                            name = "Disabled Menu Item Details",
                            summary = "Details of the disabled menu item",
                            value = """
                                    {
                                      "nome": "Feijoada",
                                      "descricao": "Feijoada completa com arroz, farofa e couve",
                                      "preco": 30,
                                      "categoria": "Prato Principal",
                                      "active": false
                                    }
                                    """
                    )
            )),
            @ApiResponse(responseCode = "404", description = "Not Found - Menu item with the specified ID does not exist",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionTemplate.class),
                    examples = @ExampleObject(
                            name = "Menu Item Not Found",
                            summary = "No menu item found with the specified ID",
                            value = """
                                    {
                                        "message": "Item de menu com ID 1 não encontrado.",
                                        "status": 404,
                                        "statusError": "NOT_FOUND",
                                        "timestamp": "2026-04-17T11:36:21.2680631"
                                    }
                                    """
                    )
            ))
    })
    ResponseEntity<ItemsDetails> disableMenuItem(
            @Parameter(
                    description = "ID of the menu item to disable",
                    required = true,
                    example = "1"
            ) @PathVariable Long itemId);
}
