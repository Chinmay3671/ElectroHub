# ElectroHub — Electronics Shopping Mini Project

A clean, beginner-friendly, backend-only electronics shopping RESTful API application developed with **Java 17+** and **Spring Boot**. Built as an Advanced Java college mini-project demonstrating core concepts including **Spring Core (IoC & Constructor Injection)**, **Spring MVC (REST APIs)**, **DTOs (Data Transfer Objects)**, **Spring Data JPA & Hibernate**, and **MySQL Persistence**.

---

## 1. Technology Stack

- **Java**: Version 17+ (JDK 24 compatible)
- **Framework**: Spring Boot 3.2.5
- **Spring Core**: Inversion of Control (IoC) and Constructor-based Dependency Injection
- **Spring MVC**: RESTful Controller layer with HTTP status codes and JSON serialization
- **Spring Data JPA & Hibernate**: Object-Relational Mapping (ORM) and automated database schema generation
- **Database**: MySQL 8.0+
- **Build Tool**: Apache Maven 3.9+
- **Architecture**: Clean 4-Layer Architecture (`Controller` → `Service` → `Repository` → `Database`)
- **DTOs & Validation**: Jakarta Bean Validation (`@Valid`, `@NotBlank`, `@Positive`, `@Min`)
- **API Client**: Postman / cURL

---

## 2. Key Features

### A. Product Management
- Add new electronic components (microcontrollers, development boards, sensors, actuators, etc.).
- Retrieve all products in the catalog.
- Retrieve a single product by its unique ID.
- Each product model contains: `id`, `name`, `category`, `price`, and `description`.
- Automatic database seeding of 6 realistic electronic products on first launch (`DataInitializer`).

### B. Shopping Cart
- Add an electronic product to the cart with quantity.
- If the item is already present in the cart, the quantity is automatically updated.
- View all items currently in the cart with calculated subtotals (`unitPrice * quantity`).
- Remove an individual item from the cart by cart item ID.
- Calculate the cart summary: total items, total units, and grand total.
- Clear entire cart.

### C. External Resource Links
Provides REST endpoints returning verified external URLs for electronics learning, simulation, and project documentation:
- **Embedded C Compiler**: [OnlineGDB Embedded C](https://www.onlinegdb.com/online_c_compiler)
- **Electronics Simulator**: [Wokwi Simulator](https://wokwi.com/)
- **Circuit Simulator**: [Autodesk Tinkercad Circuits](https://www.tinkercad.com/circuits)
- **Component Information**: [Wikipedia Electronic Components](https://www.wikipedia.org/)
- **Project Files & Documentation**: Google Drive shareable URLs configured in `application.properties`.

---

## 3. Project Structure

```
d:\mini-project/
├── pom.xml                                  # Maven dependencies and build configuration
├── postman/
│   └── ElectroHub_Postman_Collection.json   # Ready-to-import Postman API collection
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── electrohub/
│   │   │           ├── ElectroHubApplication.java       # Spring Boot main runner
│   │   │           ├── config/
│   │   │           │   ├── DataInitializer.java        # Seeds initial sample electronics
│   │   │           │   └── ResourceConfig.java         # External URLs & Google Drive config
│   │   │           ├── controller/
│   │   │           │   ├── CartController.java         # /api/cart endpoints
│   │   │           │   ├── ProductController.java      # /api/products endpoints
│   │   │           │   └── ResourceController.java     # /api/resources endpoints
│   │   │           ├── dto/
│   │   │           │   ├── ApiResponse.java            # Standard generic response wrapper
│   │   │           │   ├── CartItemRequestDto.java     # Add to cart payload
│   │   │           │   ├── CartItemResponseDto.java    # Cart item output with subtotal
│   │   │           │   ├── CartTotalResponseDto.java   # Cart total and grand summary
│   │   │           │   ├── ProductRequestDto.java      # Add product payload with validation
│   │   │           │   ├── ProductResponseDto.java     # Product output
│   │   │           │   └── ResourceLinkDto.java        # Resource link output
│   │   │           ├── entity/
│   │   │           │   ├── CartItem.java               # JPA entity for cart_items table
│   │   │           │   └── Product.java                # JPA entity for products table
│   │   │           ├── exception/
│   │   │           │   ├── BadRequestException.java    # 400 Bad Request exception
│   │   │           │   ├── ErrorResponseDto.java       # Standardized error JSON model
│   │   │           │   ├── GlobalExceptionHandler.java # @RestControllerAdvice handler
│   │   │           │   └── ResourceNotFoundException.java # 404 Not Found exception
│   │   │           ├── repository/
│   │   │           │   ├── CartItemRepository.java     # Spring Data JPA for cart_items
│   │   │           │   └── ProductRepository.java      # Spring Data JPA for products
│   │   │           └── service/
│   │   │               ├── CartService.java            # Shopping cart business rules
│   │   │               ├── ProductService.java         # Product management business rules
│   │   │               └── ResourceService.java        # External URLs resolution
│   │   └── resources/
│   │       ├── application.properties                  # MySQL and application settings
│   │       └── schema.sql                              # Reference SQL table definitions
│   └── test/
│       └── java/
│           └── com/
│               └── electrohub/
│                   └── ElectroHubApplicationTests.java # Context loading test
└── README.md
```

---

## 4. Layered Architecture Explanation

1. **Controller Layer (`controller`)**: Handles HTTP requests, performs input validation via `@Valid`, and delegates business tasks to the service layer. Returns `ResponseEntity` with appropriate HTTP status codes (`200 OK`, `201 CREATED`, `404 NOT FOUND`).
2. **Service Layer (`service`)**: Contains core application business logic, transactional boundaries (`@Transactional`), calculation of subtotals/totals, and entity-to-DTO conversion.
3. **Repository Layer (`repository`)**: Extends `JpaRepository<T, ID>` from Spring Data JPA to provide CRUD operations, pagination, and custom finder methods without writing boilerplate SQL.
4. **DTO Layer (`dto`)**: Decouples the internal database schema from the external client-facing API responses.
5. **Constructor-based Dependency Injection**: All services, controllers, and initializers use explicit constructors with `@Autowired` to clearly demonstrate Spring Core IoC principles.

---

## 5. MySQL Database Setup

1. Make sure MySQL Server is running on port `3306`.
2. Connect to MySQL and create the database:
   ```sql
   CREATE DATABASE IF NOT EXISTS electrohub_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
3. Update `src/main/resources/application.properties` with your MySQL credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/electrohub_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=YOUR_MYSQL_PASSWORD
   ```
4. Spring Data JPA automatically creates and updates the tables (`products`, `cart_items`) when the application starts.

---

## 6. How to Build and Run

### Run directly with Maven:
```powershell
# Navigate to the project directory
cd d:\mini-project

# Run with Spring Boot Maven plugin
mvn spring-boot:run
```

### Or build the JAR package and run:
```powershell
mvn clean package -DskipTests
java -jar target/electrohub-1.0.0.jar
```

The application starts on: **`http://localhost:8081`**

---

## 7. REST API Endpoints & Testing Examples

### A. Products API

| Method | Endpoint | Description | Status Code |
|---|---|---|---|
| `POST` | `/api/products` | Add an electronic product | `201 Created` |
| `GET` | `/api/products` | Retrieve all electronic products | `200 OK` |
| `GET` | `/api/products/{id}` | Retrieve a product by ID | `200 OK` / `404 Not Found` |

#### 1. Add Product (`POST /api/products`)
**Request Header**: `Content-Type: application/json`  
**Request Body**:
```json
{
  "name": "Arduino Nano Every",
  "category": "Microcontrollers",
  "price": 14.50,
  "description": "Compact 5V Arduino board based on the ATMega4809 processor with 48KB Flash memory."
}
```
**Response (`201 Created`)**:
```json
{
  "id": 7,
  "name": "Arduino Nano Every",
  "category": "Microcontrollers",
  "price": 14.5,
  "description": "Compact 5V Arduino board based on the ATMega4809 processor with 48KB Flash memory."
}
```

#### 2. Get All Products (`GET /api/products`)
**Response (`200 OK`)**:
```json
[
  {
    "id": 1,
    "name": "Arduino Uno R3",
    "category": "Microcontrollers",
    "price": 25.99,
    "description": "ATmega328P microcontroller development board with 14 digital I/O pins and 6 analog inputs."
  },
  {
    "id": 2,
    "name": "Raspberry Pi 4 Model B (4GB)",
    "category": "Single Board Computers",
    "price": 75.0,
    "description": "Quad-core 64-bit ARM Cortex-A72 processor, 4GB LPDDR4 RAM, dual 4K micro-HDMI display outputs."
  }
]
```

#### 3. Get Product by ID (`GET /api/products/1`)
**Response (`200 OK`)**:
```json
{
  "id": 1,
  "name": "Arduino Uno R3",
  "category": "Microcontrollers",
  "price": 25.99,
  "description": "ATmega328P microcontroller development board with 14 digital I/O pins and 6 analog inputs."
}
```

---

### B. Shopping Cart API

| Method | Endpoint | Description | Status Code |
|---|---|---|---|
| `POST` | `/api/cart/items` | Add product to cart | `201 Created` |
| `GET` | `/api/cart/items` | View all cart items | `200 OK` |
| `DELETE` | `/api/cart/items/{id}` | Remove a cart item by ID | `200 OK` |
| `GET` | `/api/cart/total` | Calculate cart grand total | `200 OK` |
| `DELETE` | `/api/cart/items` | Clear entire shopping cart | `200 OK` |

#### 1. Add Item to Cart (`POST /api/cart/items`)
**Request Body**:
```json
{
  "productId": 1,
  "quantity": 2
}
```
**Response (`201 Created`)**:
```json
{
  "id": 1,
  "productId": 1,
  "productName": "Arduino Uno R3",
  "category": "Microcontrollers",
  "unitPrice": 25.99,
  "quantity": 2,
  "subtotal": 51.98
}
```

#### 2. View All Cart Items (`GET /api/cart/items`)
**Response (`200 OK`)**:
```json
[
  {
    "id": 1,
    "productId": 1,
    "productName": "Arduino Uno R3",
    "category": "Microcontrollers",
    "unitPrice": 25.99,
    "quantity": 2,
    "subtotal": 51.98
  },
  {
    "id": 2,
    "productId": 3,
    "productName": "ESP32 NodeMCU Module",
    "category": "IoT & Wireless",
    "unitPrice": 8.5,
    "quantity": 3,
    "subtotal": 25.5
  }
]
```

#### 3. Calculate Cart Total (`GET /api/cart/total`)
**Response (`200 OK`)**:
```json
{
  "totalQuantity": 5,
  "totalItems": 2,
  "grandTotal": 77.48,
  "items": [
    {
      "id": 1,
      "productId": 1,
      "productName": "Arduino Uno R3",
      "category": "Microcontrollers",
      "unitPrice": 25.99,
      "quantity": 2,
      "subtotal": 51.98
    },
    {
      "id": 2,
      "productId": 3,
      "productName": "ESP32 NodeMCU Module",
      "category": "IoT & Wireless",
      "unitPrice": 8.5,
      "quantity": 3,
      "subtotal": 25.5
    }
  ]
}
```

#### 4. Remove Item from Cart (`DELETE /api/cart/items/1`)
**Response (`200 OK`)**:
```json
{
  "success": true,
  "message": "Item with ID 1 was removed from the cart.",
  "data": null,
  "timestamp": "2026-10-12T20:30:00"
}
```

---

### C. External Resource Links API

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/resources` | Returns all external resource links |
| `GET` | `/api/resources/c-compiler` | Embedded C online compiler & debugger |
| `GET` | `/api/resources/simulation` | Electronics simulator (Wokwi) |
| `GET` | `/api/resources/circuit-simulation` | Circuit simulator (Tinkercad Circuits) |
| `GET` | `/api/resources/component-info` | Component info & encyclopedia (Wikipedia) |
| `GET` | `/api/resources/project-files` | Google Drive shareable project source link |
| `GET` | `/api/resources/documentation` | Google Drive shareable documentation link |

#### Example Response (`GET /api/resources/simulation`):
```json
{
  "key": "simulation",
  "title": "Electronics Simulation Platform (Wokwi)",
  "description": "Full-featured browser simulator for Arduino, ESP32, and Raspberry Pi Pico electronics projects.",
  "url": "https://wokwi.com/"
}
```

#### Example Response (`GET /api/resources/project-files`):
```json
{
  "key": "project-files",
  "title": "ElectroHub Project Source Code & Assets (Google Drive)",
  "description": "Google Drive shareable folder containing project source archives, database scripts, and diagrams.",
  "url": "https://drive.google.com/drive/folders/sample-electrohub-project-source"
}
```

---

## 8. Postman Testing Guide

1. Open **Postman**.
2. Click **Import** in the top left.
3. Select the file located at: `d:\mini-project\postman\ElectroHub_Postman_Collection.json`.
4. The collection **ElectroHub — Electronics Shopping Mini Project** will be loaded with all folders and pre-configured endpoints.
5. Make sure the application is running (`mvn spring-boot:run`), then execute requests sequentially.

---

## 9. Key Demonstration & Viva Points

1. **Why Layered Architecture?**
   - Keeps concerns separated: Controllers only handle HTTP and validation; Services handle business rules; Repositories handle database persistence.
2. **Why Constructor Injection instead of `@Autowired` on fields?**
   - Immutability (dependencies can be `final`).
   - Prevents `NullPointerException` at runtime.
   - Makes unit testing straightforward by allowing mock injection without Spring reflection.
3. **Why DTOs instead of exposing JPA Entities directly?**
   - Security: Avoids exposing internal database schema and unwanted fields.
   - Decoupling: Changes in database tables don't break external API contracts.
   - Custom formatting: DTOs allow calculated fields like `subtotal` without persisting them as columns.
4. **How are External Links Managed?**
   - Configured cleanly via `application.properties` and injected via `@Value` in `ResourceConfig`, demonstrating centralized configuration management.
#   E l e c t r o H u b  
 