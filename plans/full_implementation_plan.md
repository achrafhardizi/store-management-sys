# Implementation Plan - Secure Microservices with Spring Boot, React, & Keycloak

## 1. 🔍 Analysis & Context
*   **Objective:** Transform a skeleton project into a fully functional, secure microservices application (Product & Order management) with PostgreSQL databases, compliance with DevSecOps requirements (OWASP, Logging), and a React frontend.
*   **Affected Files:**
    *   **Infrastructure:** `docker-compose.yml`, `Dockerfile`(s).
    *   **Configuration:** `pom.xml` (dependencies), `application.yml` (DB & Auth).
    *   **Backend Code:** `ProductController`, `OrderController`, `SecurityConfig`, new Entities/Repositories.
    *   **Frontend Code:** `App.js` (API consumption).
*   **Key Dependencies:**
    *   **Spring Boot:** Data JPA, Web, Security, OAuth2 Resource Server.
    *   **Spring Cloud:** OpenFeign (Inter-service communication).
    *   **Database:** PostgreSQL.
    *   **Tools:** Keycloak (IAM), Docker, OWASP Dependency Check.
*   **Risks/Unknowns:**
    *   Keycloak realm configuration (Clients, Roles, Users) is manual but critical.
    *   Network connectivity between containers (Docker DNS).

## 2. 📋 Checklist
- [ ] **Step 1: Infrastructure Setup** (Docker Compose for DBs & Keycloak)
- [ ] **Step 2: Dependency Injection & DevSecOps** (Update POMs)
- [ ] **Step 3: Service Configuration** (Connect to DBs)
- [ ] **Step 4: Product Service Implementation** (Core CRUD)
- [ ] **Step 5: Order Service Implementation** (Logic + Feign Client)
- [ ] **Step 6: Frontend Integration** (Real Data)
- [ ] **Step 7: Containerization** (Dockerfiles for Apps)
- [ ] **Verification** (Full End-to-End Test)

## 3. 📝 Step-by-Step Implementation Details

> **Note:** Commit changes to the repository after each step is finished and validated to be working as expected.

### Step 1: Infrastructure Setup (Docker Compose)
*   **Goal:** efficient local development environment with persistent databases and authentication.
*   **Action:**
    *   Create `docker-compose.yml` at the project root.
    *   Define services:
        *   `postgres-product` (Port 5432)
        *   `postgres-order` (Port 5433)
        *   `keycloak` (Port 8080)
        *   `pgadmin` (Optional, for DB inspection)
*   **Verification:** Run `docker-compose up -d` and verify containers are healthy.
*   **Commit:** Commit Step 1 changes.

### Step 2: Dependency Injection & DevSecOps
*   **Goal:** Add necessary libraries and security scanning tools.
*   **Action:**
    *   **Files:** `product-service/pom.xml`, `order-service/pom.xml`.
    *   **Add Dependencies:**
        *   `spring-boot-starter-data-jpa`
        *   `postgresql`
        *   `lombok`
        *   `spring-cloud-starter-openfeign` (Order Service Only)
    *   **Add Plugins:**
        *   `owasp-dependency-check-maven` (Goal: check)
*   **Verification:** Run `mvn clean install` (expect build failure due to missing DB config, but dependency resolution should pass).
*   **Commit:** Commit Step 2 changes.

### Step 3: Service Configuration
*   **Goal:** Configure apps to connect to their respective PostgreSQL databases.
*   **Action:**
    *   **Files:** `product-service/src/main/resources/application.yml`, `order-service/src/main/resources/application.yml`.
    *   **Config:**
        *   `spring.datasource.url`: `jdbc:postgresql://localhost:5432/product-db` (adjust ports).
        *   `spring.datasource.username/password`.
        *   `spring.jpa.hibernate.ddl-auto`: `update`.
        *   `logging.pattern.level`: Include `%X{user_id}` for traceability.
*   **Verification:** Apps start successfully without DB errors (requires Step 1 running).
*   **Commit:** Commit Step 3 changes.

### Step 4: Product Service Implementation
*   **Goal:** Implement the Product Catalog.
*   **Action:**
    *   Create Entity: `ma.enset.productservice.entities.Product` (id, name, price, quantity).
    *   Create Repo: `ma.enset.productservice.repository.ProductRepository` (extends JpaRepository).
    *   Update Controller: `ProductController`.
        *   `GET /products` (Authorized: USER, ADMIN)
        *   `GET /products/{id}` (Authorized: USER, ADMIN)
        *   `POST /products` (Authorized: ADMIN)
        *   `PUT /products/{id}` (Authorized: ADMIN)
*   **Verification:** Use Postman/Curl to Create and List products.
*   **Commit:** Commit Step 4 changes.

### Step 5: Order Service Implementation
*   **Goal:** Implement Order processing and interaction with Product Service.
*   **Action:**
    *   Create Entities: `Order`, `OrderLineItem` (store productId, price, quantity).
    *   Enable Feign: Add `@EnableFeignClients` to main class.
    *   Create Client: `ma.enset.orderservice.client.ProductRestClient` (Interface).
    *   Update Controller: `OrderController`.
        *   `POST /orders` (Logic: Fetch product -> Check Stock -> Calculate Total -> Save Order).
        *   `GET /orders/{id}`.
*   **Verification:** Create an order and verify it validates against product stock.
*   **Commit:** Commit Step 5 changes.

### Step 6: Frontend Integration
*   **Goal:** User interface for the microservices.
*   **Action:**
    *   Modify `react-app/src/App.js`.
    *   Fetch products using `axios.get('/products')` through Gateway.
    *   Display products in a table.
    *   Add "Order" button next to products.
*   **Verification:** Login via Keycloak, see products, click order, see success message.
*   **Commit:** Commit Step 6 changes.

### Step 7: Containerization
*   **Goal:** Dockerize the Java and React applications.
*   **Action:**
    *   Create `Dockerfile` in `product-service`, `order-service`, `gateway`.
    *   Update `docker-compose.yml` to include these services (optional, or run standalone).
*   **Verification:** Verify all services run within Docker containers.
*   **Commit:** Commit Step 7 changes.

## 4. 🧪 Testing Strategy
*   **Unit Tests:** Test Entities and basic Service logic using JUnit.
*   **Integration Tests:**
    *   Start Infrastructure (`docker-compose up`).
    *   Start Services (`mvn spring-boot:run`).
    *   **Scenario:**
        1. Login as Admin -> Create Product (Laptop, $1000).
        2. Login as Client -> View Product.
        3. Client -> Order Product.
        4. Verify Order created in DB.
*   **Security Scan:** Run `mvn dependency-check:check`.

## 5. ✅ Success Criteria
*   [ ] `docker-compose up` starts Postgres & Keycloak.
*   [ ] Product Service persists data to Postgres.
*   [ ] Order Service persists data and communicates with Product Service.
*   [ ] Security (Roles) is enforced (Admin vs Client).
*   [ ] Access logs show User ID.