# Implementation Plan - Secure Microservices with Spring Boot, React, & Keycloak

## 1. 🔍 Analysis & Context
*   **Objective:** Transform a skeleton project into a fully functional, secure microservices application (Product & Order management) with PostgreSQL databases, compliance with DevSecOps requirements (OWASP, Logging), and a React frontend.
*   **Affected Files:**
    *   **Infrastructure:** `docker-compose.yml`, `Dockerfile`(s).
    *   **Configuration:** `pom.xml` (dependencies), `application.yml` (DB & Auth).
    *   **Backend Code:** `ProductController`, `OrderController`, `SecurityConfig`, new Entities/Repositories.
    *   **Frontend Code:** `App.js` (API consumption).
    *   **DevOps:** `.github/workflows/devsecops.yml`.
    *   **Documentation:** `README.md`.
*   **Key Dependencies:**
    *   **Spring Boot:** Data JPA, Web, Security, OAuth2 Resource Server.
    *   **Spring Cloud:** OpenFeign (Inter-service communication).
    *   **Database:** PostgreSQL.
    *   **Tools:** Keycloak (IAM), Docker, OWASP Dependency Check, SonarQube, Trivy.
*   **Risks/Unknowns:**
    *   Keycloak realm configuration (Clients, Roles, Users) is manual but critical.
    *   Network connectivity between containers (Docker DNS).

## 2. 📋 Checklist
- [x] **Step 1: Infrastructure Setup** (Docker Compose for DBs & Keycloak)
  *   Status: ✅ Implemented in `docker-compose.yml`. Containers running on ports 5434, 5435, 8080, 5050.
- [x] **Step 2: Dependency Injection & DevSecOps** (Update POMs)
  *   Status: ✅ Implemented. Dependencies added. OWASP plugin configured (skipped execution due to NVD API downtime).
- [x] **Step 3: Service Configuration** (Connect to DBs)
  *   Status: ✅ Implemented. Configured DB connection. Fixed version incompatibility by downgrading Boot to 3.4.1.
- [x] **Step 4: Product Service Implementation** (Core CRUD)
  *   Status: ✅ Implemented. Entity, Repo, Controller, Security (Role Mapping) added.
- [x] **Step 5: Order Service Implementation** (Logic + Feign Client)
  *   Status: ✅ Implemented. Order Entity/Repo, Feign Client with Token Propagation, Controller logic.
- [x] **Step 6: Frontend Integration** (Real Data)
  *   Status: ✅ Implemented. React App updated to fetch/display products and orders.
- [x] **Step 7: Containerization** (Dockerfiles for Apps)
  *   Status: ✅ Implemented. Dockerfiles created for all services and React app. `docker-compose.yml` updated to orchestrate the full stack.
- [x] **Step 8: DevSecOps Pipeline** (GitHub Actions with Trivy, OWASP, SonarQube)
  *   Status: ✅ Implemented in `.github/workflows/devsecops.yml`. Pipeline includes SCA, SAST, and Container Scanning.
- [x] **Step 9: Academic Documentation** (Full Report in README)
  *   Status: ✅ Implemented. Comprehensive README.md created with architecture, security, and DevSecOps details.
- [ ] **Verification** (Full End-to-End Test)

## 3. 📝 Step-by-Step Implementation Details

> **Note:** Commit changes to the repository after each step is finished and validated to be working as expected.

### Step 1: Infrastructure Setup (Docker Compose)
*   **Goal:** efficient local development environment with persistent databases and authentication.
*   **Action:**
    *   Create `docker-compose.yml` at the project root.
*   **Verification:** Run `docker-compose up -d` and verify containers are healthy.
*   **Commit:** Commit Step 1 changes.

### Step 2: Dependency Injection & DevSecOps
*   **Goal:** Add necessary libraries and security scanning tools.
*   **Action:**
    *   **Files:** `product-service/pom.xml`, `order-service/pom.xml`.
    *   **Add Plugins:** `owasp-dependency-check-maven`.
*   **Verification:** Run `mvn clean install`.
*   **Commit:** Commit Step 2 changes.

### Step 3: Service Configuration
*   **Goal:** Configure apps to connect to their respective PostgreSQL databases.
*   **Action:**
    *   **Files:** `product-service/src/main/resources/application.yml`, `order-service/src/main/resources/application.yml`.
*   **Verification:** Apps start successfully without DB errors.
*   **Commit:** Commit Step 3 changes.

### Step 4: Product Service Implementation
*   **Goal:** Implement the Product Catalog.
*   **Action:**
    *   Create Entity, Repo, Controller with RBAC.
*   **Verification:** Use Postman/Curl to Create and List products.
*   **Commit:** Commit Step 4 changes.

### Step 5: Order Service Implementation
*   **Goal:** Implement Order processing and interaction with Product Service.
*   **Action:**
    *   Create Entities, Feign Client, Controller logic.
*   **Verification:** Create an order and verify it validates against product stock.
*   **Commit:** Commit Step 5 changes.

### Step 6: Frontend Integration
*   **Goal:** User interface for the microservices.
*   **Action:**
    *   Modify `react-app/src/App.js` to fetch and display data.
*   **Verification:** Login via Keycloak, see products, click order.
*   **Commit:** Commit Step 6 changes.

### Step 7: Containerization
*   **Goal:** Dockerize the Java and React applications.
*   **Action:**
    *   Create `Dockerfile` in each service. Update `docker-compose.yml`.
*   **Verification:** Verify all services run within Docker containers.
*   **Commit:** Commit Step 7 changes.

### Step 8: DevSecOps Pipeline (GitHub Actions)
*   **Goal:** Automate security scanning and quality assurance.
*   **Action:**
    *   Create `.github/workflows/devsecops.yml`.
    *   **Jobs:**
        1. **Build:** Compile Maven projects.
        2. **SCA (OWASP):** Run `dependency-check:check` to identify vulnerable libraries.
        3. **SAST (SonarQube):** Run SonarCloud/SonarQube analysis for code quality.
        4. **Container Scan (Trivy):** Scan Docker images for vulnerabilities (CVEs).
        5. **Reporting:** Upload security reports as artifacts.
*   **Verification:** Push to GitHub and verify all pipeline stages pass.
*   **Commit:** Commit GitHub Actions workflow.

### Step 9: Academic Documentation (Full Report)
*   **Goal:** Provide a comprehensive report for the professor.
*   **Action:**
    *   Update `README.md` with:
        1. **Project Context:** Objectives and technologies.
        2. **Global Architecture:** Sequence diagrams and service topology.
        3. **Security Model:** Keycloak configuration, OAuth2 flow, and JWT propagation.
        4. **Microservices Details:** Product & Order service logic.
        5. **DevSecOps Implementation:** Explanation of Trivy, OWASP, and SonarQube usage.
        6. **User Manual:** How to run and test.
        7. **Interface Placeholders:** `[INSERT UI SCREENSHOT HERE]`.
*   **Verification:** Final review of the README for clarity and academic rigor.
*   **Commit:** Final commit with full documentation.

## 4. 🧪 Testing Strategy
*   **Unit Tests:** Test Entities and basic Service logic using JUnit.
*   **Security Scan:** Run `mvn dependency-check:check`.
*   **Pipeline Verification:** Ensure GitHub Actions completes successfully.

## 5. ✅ Success Criteria
*   [ ] `docker-compose up` starts the full stack.
*   [ ] Security (Roles) is enforced at Gateway and Service levels.
*   [ ] GitHub Actions pipeline successfully scans dependencies and images.
*   [ ] README contains a detailed academic report with placeholders.
