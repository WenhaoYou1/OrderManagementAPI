# Order Management API

A **Java Spring Boot** order management microservice that provides complete CRUD operations and state management for orders. The project adopts a microservices architecture and supports high-concurrency order processing with multi-state management.

## 🛠️ Technology Stack

- Java & Spring Boot (Web, JPA, AMQP)
- PostgreSQL & Hibernate
- RabbitMQ
- Maven, Lombok, Logback
- Docker

## 📊 System Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend App  │    │  Other Services │    │  Admin Tools    │
│   (React/Vue)   │    │  (User/Payment) │    │   (Postman)     │
└─────────┬───────┘    └─────────┬───────┘    └─────────┬───────┘
          │                      │                      │
          └──────────────────────┼──────────────────────┘
                                 │
                    ┌─────────────┴─────────────┐
                    │    Order Service (9004)   │
                    │   Order Service API       │
                    └─────────────┬─────────────┘
                                  │
                    ┌─────────────┴─────────────┐
                    │      Business Logic       │
                    │   Service Layer           │
                    └─────────────┬─────────────┘
                                  │
                    ┌─────────────┴─────────────┐
                    │      Data Access Layer    │
                    │   DAO/Repository Layer    │
                    └─────────────┬─────────────┘
                                  │
          ┌───────────────────────┼───────────────────────┐
          │                       │                       │
    ┌─────▼─────┐         ┌───────▼──────┐         ┌─────▼─────┐
    │PostgreSQL │         │   RabbitMQ   │         │ Log Files │
    │  (5433)   │         │   (5672)     │         │  (logs/)  │
    └───────────┘         └──────────────┘         └───────────┘
```

### Order Management APIs

#### Create Order

```http
POST /orders/create
Content-Type: application/json

{
  "userId": "user123",
  "taxYear": 2024,
  "balance": 299.99,
  "orderState": "PENDING",
  "paymentState": "UNPAID"
}
```

#### Query Orders

```http
GET /orders/getAll                    # Get all orders
GET /orders/getById/{id}              # Get order by ID
GET /orders/getByUser/{userId}        # Get orders by user ID
GET /orders/getByTaxYear/{taxYear}    # Get orders by tax year
GET /orders/getByState/{state}        # Get orders by order state
GET /orders/getByPaymentState/{state} # Get orders by payment state
```

#### Update Orders

```http
PUT /orders/updateState/{id}?state=PENDING        # Update order state
PUT /orders/updateBalance/{id}?balance=399.99     # Update order amount
PUT /orders/updatePaymentState/{id}?state=PAID    # Update payment state
PUT /orders/updateTaxYear/{id}?year=2024          # Update tax year
```

#### Delete Orders

```http
DELETE /orders/deleteById/{id}        # Delete specific order
DELETE /orders/deleteByUser/{userId}  # Delete all user orders
DELETE /orders/deleteByTaxYear/{year} # Delete orders by tax year
```

### Status Enums

#### Order States (OrderState)

- `PENDING` - Pending
- `PROCESSING` - Processing
- `COMPLETED` - Completed
- `CANCELLED` - Cancelled

#### Payment States (PayState)

- `UNPAID` - Unpaid
- `PAID` - Paid
- `REFUNDED` - Refunded

## 🚀 Quick Start

### Prerequisites

Ensure your system has the following software installed:

- **Java 17** or higher
- **Docker** and **Docker Compose**
- **Maven** (optional, project includes Maven Wrapper)

### Step 1: Clone the Project

```bash
git clone git@github.com:WenhaoYou1/OrderManagementAPI.git
cd order-service
```

### Step 2: Set Execute Permissions

```bash
chmod +x mvnw
```

### Step 3: Build the Project

```bash
./mvnw clean install
```

### Step 4: Start Dependencies

#### Start PostgreSQL Database

```bash
docker run --name pg-taxfile \
  -e POSTGRES_DB=taxfile_db \
  -e POSTGRES_USER=mengtaxapi \
  -e POSTGRES_PASSWORD=_86BrzBbsS2k_ \
  -p 5433:5432 \
  -d postgres:14
```

#### Start RabbitMQ Message Queue

```bash
docker run -d --hostname rabbitmq-host --name rabbitmq \
  -p 5672:5672 -p 15672:15672 \
  -e RABBITMQ_DEFAULT_USER=guest \
  -e RABBITMQ_DEFAULT_PASS=guest \
  rabbitmq:3-management
```

### Step 5: Start the Application

```bash
./mvnw spring-boot:run
```

### Step 6: Verify Services

After the service starts, visit the following addresses to verify:

- **Application Health Check**: http://localhost:9004/actuator/health
- **RabbitMQ Management Interface**: http://localhost:15672 (username/password: guest/guest)

### Step 7: Test APIs

Run the test script:

```bash
chmod +x test-api.sh
./test-api.sh
```

## 🙏 Acknowledgement

Big shoutout to [Cursor](https://cursor.com/) for being the vibe coding tool I didn’t know I needed. Not only did it help polish this README, but it also saved me at least 40% of my coding time. It’s can be an essential part of my workflow in the future.
