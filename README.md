# Community Shared Electric Bicycle & Micromobility Fleet Network

A full-stack enterprise-grade smart-city web application built with **Java 17, Spring Boot 3.x, Spring Security 6, Spring Data JPA, Thymeleaf, HTML5, CSS3, JavaScript, Bootstrap 5, and MySQL 8.0**.

The system enables municipal authorities, fleet managers, and community residents to manage e-bikes, docking stations, ride trips, real-time GPS & battery telemetry, automated fare calculation, invoicing, simulated payments, maintenance purchase orders, sector budgets, and double-entry accounting.

---

## 1. System Features

### 🚴 E-Bike Fleet Operations
* **Lifecycle Management:** Add, edit, remove, and monitor smart e-bikes.
* **Status Transitions:** Real-time state management (`AVAILABLE`, `IN_USE`, `MAINTENANCE`, `CHARGING`, `DISABLED`).
* **Docking Station Integration:** Automated slot tracking (freed when rented, locked when returned).

### ⚡ Battery & GPS Telemetry
* **Automated Low Battery Alerts:** Whenever an e-bike's battery drops below 20%, an immediate alert is generated and flagged on the Admin Dashboard and Telemetry desk.
* **OpenStreetMap GPS Tracking:** Leaflet.js-powered map visualization of bikes and stations without requiring paid API keys.
* **Interactive Telemetry Simulator:** Built-in tool to simulate live IoT telemetry packets (latitude, longitude, battery percentage).

### 💳 Trip Unlocking & Dynamic Fare Billing
* **One-Click Unlocking:** Riders can locate available e-bikes and digitally unlock them with instantaneous session initialization.
* **Dynamic Fare Formula:**
  $$\text{Fare} = \text{Base Fare} + (\text{Duration in Minutes} \times \text{Per-Minute Rate}) + (\text{Distance in Km} \times \text{Per-Km Rate})$$
  *(All rates retrieved dynamically from the active database tariff)*
* **Automated Invoice Generation:** Invoices are automatically issued with a 7-day payment window upon ride completion.
* **Simulated Payments:** Supports UPI, Credit/Debit Cards, Smart Mobility Wallets, and Kiosk Cash.

### 📊 Financial & Double-Entry Accounting
* **Balanced General Ledger:** Every rental payment triggers a balanced journal entry:
  * **Debit:** `1010 - Cash and Operating Bank`
  * **Credit:** `4010 - Rider Rental Revenue`
* **Fleet Maintenance POs:** Vendor work orders with multi-stage status workflows (`CREATED` $\rightarrow$ `APPROVED` $\rightarrow$ `COMPLETED`).
  * When completed, automatically debits `5010 - Fleet Maintenance Expense` and credits `1010 - Cash and Operating Bank`.
* **Sector-Wise Budgets:** Sector budgets for *Fleet Maintenance*, *Docking Stations*, *Battery Charging*, *Fleet Rebalancing*, and *Operations* with live variance tracking:
  $$\text{Variance} = \text{Allocated Fund} - \text{Actual Spend}$$

---

## 2. Technology Stack

* **Backend:** Java 17, Spring Boot 3.2.5
* **Security:** Spring Security 6 (BCrypt password hashing, session management, role-based authorization)
* **Database:** MySQL 8.0 (`ebike_fleet_db`)
* **ORM:** Spring Data JPA / Hibernate
* **Frontend:** Thymeleaf template engine, HTML5, CSS3, JavaScript (ES6)
* **CSS Framework:** Bootstrap 5.3.2, FontAwesome 6
* **Data Visualization:** Chart.js (Doughnut & Bar charts)
* **Mapping:** Leaflet.js (OpenStreetMap)
* **Build Tool:** Maven 3.9+

---

## 3. Application Architecture

```
com.ebike/
├── EbikeFleetApplication.java
├── config/
│   ├── DataInitializer.java              # Populates realistic demo dataset
│   └── SecurityConfig.java               # Spring Security 6 & URL filters
├── controller/
│   ├── AuthController.java               # Login, registration, profile
│   ├── DashboardController.java          # Role-based dashboard router
│   ├── BikeController.java               # Bike fleet CRUD
│   ├── StationController.java            # Docking station CRUD
│   ├── RiderController.java              # Rider portal & directory
│   ├── TripController.java               # Ride unlocks & completions
│   ├── TelemetryController.java          # Telemetry & simulator
│   ├── InvoiceController.java            # Invoices & billing
│   ├── PaymentController.java            # Payments ledger
│   ├── PurchaseOrderController.java      # Maintenance work orders
│   ├── VendorController.java             # Suppliers & vendor registry
│   ├── BudgetController.java             # Sector budget management
│   ├── AccountController.java            # Chart of accounts & journal ledger
│   ├── ReportController.java             # Analytics & sustainability report
│   └── api/                              # REST API Endpoints
│       ├── BikeApiController.java
│       ├── StationApiController.java
│       ├── TelemetryApiController.java
│       ├── TripApiController.java
│       ├── InvoiceApiController.java
│       ├── PaymentApiController.java
│       ├── PurchaseOrderApiController.java
│       ├── BudgetApiController.java
│       └── ReportApiController.java
├── service/                              # Core business logic services
├── repository/                           # Spring Data JPA interfaces
├── model/                                # JPA Entity models and enums
├── dto/                                  # Data Transfer Objects
├── security/                             # CustomUserDetails & AuthSuccessHandler
└── exception/                            # GlobalExceptionHandler & custom exceptions
```

---

## 4. Database Setup & MySQL Configuration

### Step 1: Verify MySQL Service
Ensure MySQL 8.0 is running on port `3306`:
```sql
CREATE DATABASE IF NOT EXISTS ebike_fleet_db;
```

### Step 2: Configure `src/main/resources/application.properties`
The application is pre-configured with the default local credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ebike_fleet_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=dhiptha@2007
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

---

## 5. How to Run

### Option A: Run via Maven CLI
```bash
# 1. Clean and build the package
mvn clean install

# 2. Run the Spring Boot application
mvn spring-boot:run
```

### Option B: Run in VS Code or IntelliJ IDEA
1. Open the project root directory in VS Code or IntelliJ IDEA.
2. Run `com.ebike.EbikeFleetApplication.java`.
3. Open your browser and navigate to: [http://localhost:8080](http://localhost:8080)

---

## 6. Pre-Configured Demo Accounts

| Role | Username | Password | Purpose & Access |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin` | `admin123` | Full access to fleet, bikes, stations, telemetry, trips, riders, and reports |
| **INVOICING_USER** | `finance` | `finance123` | Invoices, payments, purchase orders, vendors, budgets, and ledger |
| **RIDER** | `rider1` | `rider123` | Rider portal, unlock e-bikes, active ride tracker, pay invoices |
| **RIDER** | `rider2` | `rider123` | Second sample rider with pending invoice |

*(Note: The login screen also features 1-click quick credentials auto-fill buttons for demo convenience.)*

---

## 7. REST API Endpoints (Postman Compatible)

All REST endpoints support JSON payloads and return standardized `ApiResponseDto<T>` responses with proper HTTP status codes:

### E-Bikes API
* `GET /api/bikes` — List all e-bikes
* `GET /api/bikes/{id}` — Get bike details
* `POST /api/bikes` — Register new e-bike
* `PUT /api/bikes/{id}` — Update e-bike
* `DELETE /api/bikes/{id}` — Delete e-bike

### Docking Stations API
* `GET /api/stations` — List all docking stations
* `GET /api/stations/{id}` — Get station by ID
* `POST /api/stations` — Add new docking station
* `PUT /api/stations/{id}` — Update station
* `DELETE /api/stations/{id}` — Delete station

### Telemetry API
* `POST /api/telemetry` — Transmit GPS & battery telemetry packet (triggers low battery alert if battery < 20%)
* `GET /api/telemetry/{bikeId}` — Query telemetry logs for a bike

### Trips API
* `GET /api/trips` — List all trips
* `POST /api/trips` — Unlock bike and start ride session
* `PUT /api/trips/{id}/complete` — Complete trip, calculate fare, return bike to station, and generate invoice

### Financial & Accounting APIs
* `GET /api/invoices` — List all invoices
* `POST /api/invoices` — Create custom invoice
* `GET /api/payments` — List payment transactions
* `POST /api/payments` — Settle invoice with simulated payment (updates ledger)
* `GET /api/purchase-orders` — List maintenance POs
* `POST /api/purchase-orders` — Issue maintenance PO
* `GET /api/budgets` — Query sector budgets
* `POST /api/budgets` — Update budget allocation
* `GET /api/reports/budget-variance` — Generate budget variance report

---

## 8. Automated Test Suite (TC-01 to TC-12)

The test suite validates all core requirements specified in the project specification:
```bash
mvn test
```

* **TC-01:** Rider registration and BCrypt password encryption
* **TC-02:** User authentication and role authority assignment
* **TC-03:** E-bike creation and initial status verification
* **TC-04:** Docking station creation and initial slot availability check
* **TC-05:** Real-time battery and GPS telemetry recording
* **TC-06:** Automatic low-battery alert trigger when battery < 20%
* **TC-07:** E-bike unlocking and trip initialization
* **TC-08:** Trip completion, docking return, and dynamic fare calculation
* **TC-09:** Automatic invoice generation upon trip completion
* **TC-10:** Invoice payment settlement and double-entry journal entry posting
* **TC-11:** Sector budget variance calculation and spend recording
* **TC-12:** Role-based access control and authority verification
