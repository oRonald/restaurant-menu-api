# Restaurant Management API
Uma API REST para gestão de restaurantes, desenvolvida com Java 17 e Spring Boot. 
O sistema gerencia o ciclo de pedidos, o cardápio e o controle de acesso baseado em funções, 
com processamento assíncrono de pedidos via Apache Kafka e cache de cardápio via Redis.

## Tech Stack

| Camadas | Tecnologia |
| :--- | :--- |
| Linguagem | Java 17 |
| Framework | Spring Boot |
| Segurança | Spring Security + JWT + Roles |
| Persistencia | Spring Data JPA + MySQL |
| Cache | Redis |
| Mensageria | Apache Kafka |

## Visão geral da Arquitetura
<img width="1181" height="901" alt="restaurant-architecture drawio" src="https://github.com/user-attachments/assets/8ccb7943-cd50-4f17-83a9-99e97f31007f" />

### Order Processing Flow
Quando um novo pedido é criado, o seguinte fluxo assíncrono é acionado:

```
POST /orders
     │
     ▼
OrderService.create()
     │  salva o pedido com status PENDING (PENDENTE)
     │
     ▼
KafkaProducer.send("orders-topic", orderId)
     │
     ▼
KafkaConsumer.consume(orderId)
     │  busca pedido do banco de dados
     │  valida estado
     │
     ▼
order.status = IN_PROGRESS (EM PROGRESSO)
     │
     ▼
OrderRepository.save()
```
Isso desacopla a resposta HTTP da transição de status — o cliente recebe uma 
confirmação rápida enquanto a atualização de status ocorre de forma assíncrona em segundo plano.

### Modelo de Segurança
A autenticação utiliza tokens JWT sem estado. A autorização é baseada em funções:

| Role | Acesso |
| :--- | :--- |
| WAITER | Consegue ver todos os pedidos ativos. Declara pedidos como Entregue (DELIVERED) |
| CHEF | Consegue ver todos os pedidos ativos. Declara pedidos como Pronto (READY) |
| MANAGER | Possui acesso total à aplicação.|

## Estratégia de Cache

Os dados do cardápio são armazenados em cache no Redis para reduzir a 
carga do banco de dados em dados com alta frequência de leitura e baixa frequência de gravação.

- População de Cache: na primeira requisição GET `/public/menu` (padrão cache-aside)
- TTL: 30 minutos de duração

<strong>O Redis não é usado para armazenamento de sessão — a API é totalmente sem estado. (Stateless)</strong>

## Decisões Técnicas

<strong>Por que o Kafka para atualização assíncrona de status?</strong>

A transição do status do pedido (PENDENTE → EM PROGRESSO) foi intencionalmente 
removida do ciclo de requisição HTTP. 
Isso permite que a API responda imediatamente após a persistência do pedido, 
enquanto o consumidor lida com a validação e a mudança de estado de forma independente. 
Também facilita a adição posterior de outros consumidores (por exemplo, serviço de notificação, 
sistema de exibição na cozinha) sem modificar a lógica de criação do pedido.

<strong>Por que Monolito?</strong>

O domínio é coeso. Um monolito reduz significativamente a complexidade operacional — 
unidade de implantação única, sem redes entre serviços, desenvolvimento local mais simples. 
O Kafka é usado seletivamente para o fluxo específico que realmente se beneficia do 
processamento assíncrono, e não como uma camada de comunicação geral.

<strong>Por que o Redis apenas para o Cardápio?</strong>

Os dados do cardápio têm uma alta taxa de leitura em relação à gravação e são compartilhados 
entre todos os usuários. 
Os pedidos são específicos do usuário e mutáveis ​​— armazená-los em cache introduziria um 
risco de consistência sem ganho significativo de desempenho.

## API Endpoints

### Auth
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/employees/login` | Public | Returns JWT token |
| POST | `/employees/admin/register` | Secret Master Key | Register new employees |
 
### Menu
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/public/menu` | Public | List all menu items |
| POST | `/employees/menu-item` | JWT and: MANAGER | Create menu item |
| DELETE | `/employees/menu/{id}/disable` | JWT and: MANAGER | Set menu item as disabled |
 
### Orders
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/public/orders` | Public | Create new order |
| GET | `/public/orders/{tableNumber}/{customer}` | Public | Get order by table number and customer |
| GET | `/employees/orders/active` | JWT Only | List all active orders |
| PATCH | `/employees/orders/{orderId}/ready` | JWT and: CHEF, MANAGER | Update order status to ready |
| DELETE | `/employees/orders/{orderId}/cancel` | JWT and: MANAGER | Update order status to cancelled |
| PATCH | `/employees/orders/{orderId}/delivered` | JWT and: WAITER, MANAGER | Update order status to delivered |
```
src/
└── main/
    └── java/
        └── restaurant.Cardápio.api.app/
            ├── controller/               # REST controllers
            ├── domain/                   # Core domain
            │   ├── dto/                  # Request/Response DTOs
            │   └── database/
            │       ├── entities/         # JPA entities (Order, CardápioItem, OrderItem, Employee)
            │       └── repositories/     # JPA repositories
            ├── service/                  # Business logic
            ├── security/                 # Spring Security config
            │   └── filters/              # JWT and security filters
            └── infrastructure/
                ├── docs/
                │   └── openapi/          # Swagger / OpenAPI config
                ├── exceptionHandling/    # Global exception handler
                │   └── exceptions/       # Custom exception classes
                ├── kafka/                # Kafka producer, consumer and config
                └── redis/                # Redis config and cache setup
```           
