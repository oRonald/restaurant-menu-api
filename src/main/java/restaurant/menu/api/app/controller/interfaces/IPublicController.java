package restaurant.menu.api.app.controller.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import restaurant.menu.api.app.domain.dto.ActiveOrders;
import restaurant.menu.api.app.domain.dto.ItemsDetails;
import restaurant.menu.api.app.domain.dto.OrderDetails;
import restaurant.menu.api.app.domain.dto.OrderRequest;
import restaurant.menu.api.app.infrastructure.exceptionHandling.ExceptionTemplate;
import restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions.DishNotFoundException;

import java.util.List;

public interface IPublicController {

    @Operation(
            summary = "Create Order",
            description = "Allows you to create a new order, specifying the desired items and the associated table."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = OrderDetails.class),
                    examples = @ExampleObject(
                            name = "Successful order",
                            summary = "Example of a successful order creation",
                            value = """
                            {
                                "mesa": 12,
                                "cliente": "João Silva",
                                "item": "Pizza Margherita",
                                "quantidade": 2,
                                "total": 89.90,
                                "gorjeta": 8.99,
                                "taxaServico": 4.50,
                                "mensagem": "O valor total é somado junto com gorjeta, taxa de serviço e impostos"
                            }
                            """
                    )
            )),
            @ApiResponse(responseCode = "404", description = "Menu item not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionTemplate.class),
                    examples = @ExampleObject(
                            name = "Dish not found",
                            summary = "Example of a wrong name of a dish",
                            value = """
                                    {
                                    "message": "The requested dish was not found on the menu. Please check the dish name and try again.",
                                    "status": 404,
                                    "statusError": "NOT_FOUND",
                                    "timestamp": "2026-04-16T20:10:00"
                                    }
                                    """
                    )
            )),
            @ApiResponse(responseCode = "400", description = "Invalid request",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MethodArgumentNotValidException.class),
                    examples = @ExampleObject(
                            name = "Wrong request from user",
                            summary = "Example of a wrong request from user",
                            value = """
                                    [
                                    "Atributo: 'menuItem' não deve estar em branco"
                                    ]
                                    """
                    )
            )),
            @ApiResponse(responseCode = "409", description = "Existing order for the table",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionTemplate.class),
                    examples = @ExampleObject(
                            name = "Order exists for the table",
                            summary = "An order already exists for the informed table",
                            value = """
                                    {
                                    "message": "It is not possible to place another order at a table that already has an order in progress. Please wait until the order is delivered or canceled.",
                                    "status": 409,
                                    "statusError": "CONFLICT",
                                    "timestamp": "2026-04-16T20:10:00"
                                    }
                                    """
                    )
            ))
    })
    ResponseEntity<OrderDetails> createOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Order payload",
                    required = true
            )
            @RequestBody @Valid OrderRequest request);

    @Operation(
            summary = "Get All Menu Items",
            description = "Retrieves a list of all available menu items, including their details such as name, price, and description."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu items retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ItemsDetails.class),
                    examples = {
                            @ExampleObject(
                            name = "List of menu items",
                            summary = "Returns a list of all the menu items",
                            value = """
                                  [
                                    {
                                    "nome": "Hamburguer Artesanal",
                                    "descricao": "Hamburguer Artesanal feito com ingredientes totalmente naturais",
                                    "categoria": "Lanches",
                                    "valor": 38.50
                                    },
                                    {
                                    "nome": "Batata Frita Especial",
                                    "descricao": "Batatas fritas crocantes com ketchup e queijo cheddar",
                                    "categoria": "Entradas",
                                    "valor": 25
                                    }
                                  ]
                                    """
                    ),
                    @ExampleObject(
                            name = "Empty list if there's no items",
                            summary = "Example of an empty list of menu items",
                            value = "[]"
                    )}
            ))
    })
    ResponseEntity<List<ItemsDetails>> getAllMenuItems();

    @Operation(
            summary = "Get Order by Table Number",
            description = "Retrieves the active order associated with a specific table number and customer name."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ActiveOrders.class),
                    examples = @ExampleObject(
                                    name = "Active Order",
                                    summary = "Active order for the table and customer informed",
                                    value = """
                                            {
                                            "cliente": "João Silva",
                                            "numeroPedido": "123",
                                            "item": "Pizza Margherita",
                                            "mesa": 5,
                                            "total": 89.90,
                                            "status": "Em Progresso",
                                            "quantidade": 2,
                                            "ultimaAtualizacao": "17/04/2026 00:00:00",
                                            "gorjeta": 8.99
                                            }
                                        """
                        )
            )),
            @ApiResponse(responseCode = "404", description = "Order not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionTemplate.class),
                    examples = @ExampleObject(
                            name = "Order not found",
                            summary = "Order active not found for table and customer",
                            value = """
                                    {
                                    "message": "No order in progress was found for table 5 and customer John Doe. Please check the information and try again.",
                                    "status": 404,
                                    "statusError": "NOT_FOUND",
                                    "timestamp": "2026-04-16T20:10:00"
                                    }
                                    """
                    )
            ))
    })
    ResponseEntity<ActiveOrders> getOrderByTableNumber(
            @Parameter(
                    description = "The number of the table for which the order is being retrieved",
                    required = true,
                    example = "5"
            ) @PathVariable Integer tableNumber,
            @Parameter(
                    description = "The name of the customer associated with the order",
                    required = true,
                    example = "John Doe"
            ) @PathVariable String customer);
}
