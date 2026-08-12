# 🛒 JayKart – Online Shopping Store

<div align="center">

![JayKart Banner](https://img.shields.io/badge/JayKart-E--Commerce-blue?style=for-the-badge&logo=shopify&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.3-6DB33F?style=for-the-badge&logo=spring-boot)
![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react)
![MySQL](https://img.shields.io/badge/MySQL-9-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=JSON%20web%20tokens)
![Stripe](https://img.shields.io/badge/Stripe-Payments-635BFF?style=for-the-badge&logo=stripe)

**A full-stack e-commerce web application with secure authentication, product management, cart, order processing, and Stripe payment integration.**

[Backend (Spring Boot)](#backend) · [Frontend (React + Vite)](#frontend) · [Database Schema](#database) · [Getting Started](#getting-started)

</div>

---

## Overview

**JayKart** is a production-ready full-stack e-commerce platform built using **Spring Boot 4** on the backend and **React 19** on the frontend. It supports user registration/login with **JWT-based stateless authentication**, role-based access control (ADMIN/USER), a shopping cart with **Redux state management**, real-time **Stripe payment processing**, order history, and a full admin panel.

---

## Features

### Authentication & Security
- JWT-based stateless authentication (no sessions)
- Role-based access control: `ROLE_USER` and `ROLE_ADMIN`
- Secure password hashing with BCrypt
- CSRF protection with Spring Security
- Custom JWT filter in the Spring Security filter chain

### Shopping & Products
- Browse all products on the home page
- Detailed product pages with add-to-cart
- Shopping cart with real-time quantity updates (Redux Toolkit)
- Persistent cart state across sessions

### Payments
- Stripe Payment Intent API integration
- Secure card entry via Stripe Elements (React)
- Payment confirmation and order recording on success

### Orders & Profile
- User order history with status tracking
- Update shipping address and profile info
- Admin panel: view & manage all orders and contact messages

### DevOps & Observability
- Spring Boot Actuator with all endpoints exposed
- Caffeine in-memory caching for products
- Structured JSON file logging with log level control via env vars
- Multi-environment properties: `dev`, `qa`, `prod`
- SpringDoc OpenAPI (Swagger UI) at `/swagger-ui.html`

---

## Architecture

```
jaykart/
├── eazystore/                  # Spring Boot Backend
│   └── src/main/java/com/jaykabra/jaykart/
│       ├── config/             # Caffeine cache, Stripe, Auditor
│       ├── constants/          # App-wide constants
│       ├── controller/         # REST API controllers
│       ├── dto/                # Data Transfer Objects
│       ├── entity/             # JPA Entities (Customer, Product, Order...)
│       ├── exception/          # Global exception handling
│       ├── filter/             # JWT authentication filter
│       ├── repository/         # Spring Data JPA repositories
│       ├── security/           # Spring Security configuration
│       ├── service/            # Business logic (interfaces + impl)
│       └── util/               # JWT utility
└── eazystore-ui/               # React + Vite Frontend
    └── src/
        ├── api/                # Axios API client
        ├── components/         # React components
        │   ├── admin/          # Admin-only pages
        │   └── footer/         # Footer
        └── store/              # Redux slices (auth, cart)
```

---

## Tech Stack

### Backend
| Technology | Purpose |
|---|---|
| **Java 26** | Core language |
| **Spring Boot 4.0.3** | Application framework |
| **Spring Security 6** | Authentication & authorization |
| **Spring Data JPA (Hibernate)** | ORM & database access |
| **MySQL 9** | Relational database |
| **JWT (jjwt 0.13)** | Stateless token-based auth |
| **Stripe Java SDK 31.4** | Payment processing |
| **Caffeine Cache** | In-memory product caching |
| **Lombok** | Boilerplate reduction |
| **SpringDoc OpenAPI 3** | API documentation (Swagger UI) |
| **Spring Boot Actuator** | Health & metrics endpoints |

### Frontend
| Technology | Purpose |
|---|---|
| **React 19** | UI framework |
| **Vite 6** | Build tool & dev server |
| **Redux Toolkit 2** | Global state management (cart, auth) |
| **React Router DOM 7** | Client-side routing with loaders & actions |
| **Axios** | HTTP client |
| **Stripe React Elements** | Secure payment UI |
| **Tailwind CSS 4** | Utility-first styling |
| **React Toastify** | Notification system |
| **FontAwesome** | Icons |

---

## Database

**Database:** `jaykart` (MySQL)

### Core Entities
| Table | Description |
|---|---|
| `customer` | Registered users with hashed passwords |
| `role` | User roles (ROLE_USER, ROLE_ADMIN) |
| `product` | Product catalog |
| `orders` | Order records per customer |
| `order_item` | Line items per order |
| `address` | Shipping addresses linked to orders |
| `contact` | Contact form submissions |

All entities extend `BaseEntity` with JPA Auditing (`created_at`, `updated_at`, `created_by`, `updated_by`).

---

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8+
- Node.js 18+ & npm

### 1. Clone the Repository
```bash
git clone https://github.com/maheshwarijay2005/jaykart.git
cd jaykart
```

### 2. Database Setup
```sql
CREATE DATABASE jaykart;
-- Schema is auto-applied via Spring JPA on first run
```

### 3. Configure Backend
Edit `eazystore/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/jaykart
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

Set your Stripe secret key in `stripe.properties`:
```properties
stripe.api.key=sk_test_YOUR_SECRET_KEY
```

### 4. Run the Backend
```bash
cd eazystore
./mvnw spring-boot:run
# Backend starts at http://localhost:8080
```

### 5. Configure Frontend
Edit `eazystore-ui/src/main.jsx` and set your Stripe publishable key:
```js
const stripePromise = loadStripe("pk_test_YOUR_PUBLISHABLE_KEY");
```

### 6. Run the Frontend
```bash
cd eazystore-ui
npm install
npm run dev
# Frontend starts at http://localhost:5173
```

---

## API Endpoints

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `POST` | `/api/v1/auth/register` | Register new user | No |
| `POST` | `/api/v1/auth/login` | Login & receive JWT | No |
| `GET` | `/api/v1/products` | Get all products | No |
| `GET` | `/api/v1/products/{id}` | Get product by ID | No |
| `POST` | `/api/v1/orders` | Place an order | USER |
| `GET` | `/api/v1/orders` | Get user orders | USER |
| `GET` | `/api/v1/profile` | Get user profile | USER |
| `PUT` | `/api/v1/profile` | Update profile | USER |
| `POST` | `/api/v1/payment/create-payment-intent` | Create Stripe intent | USER |
| `POST` | `/api/v1/contact` | Submit contact form | No |
| `GET` | `/api/v1/admin/orders` | View all orders | ADMIN |
| `GET` | `/api/v1/admin/messages` | View contact messages | ADMIN |

Full interactive API docs available at: `http://localhost:8080/swagger-ui.html`

---

## Environment Variables

| Variable | Default | Description |
|---|---|---|
| `DATABASE_HOST` | `localhost` | MySQL host |
| `DATABASE_PORT` | `3306` | MySQL port |
| `DATABASE_NAME` | `jaykart` | Database name |
| `DATABASE_USERNAME` | `root` | DB username |
| `DATABASE_PASSWORD` | _(none)_ | DB password |
| `LOG_LEVEL` | `INFO` | Logging level |
| `LOG_FILE_NAME` | `logs/app.log` | Log file path |

---

## Security Flow

```
Client → HTTP Request
    → JWT Filter (extracts & validates token)
    → Spring Security Context (sets Authentication)
    → Controller (checks role via @PreAuthorize or Security config)
    → Service → Repository → MySQL
```

---

## Author

**Jay Kabra**

[![GitHub](https://img.shields.io/badge/GitHub-maheshwarijay2005-181717?style=for-the-badge&logo=github)](https://github.com/maheshwarijay2005)

---

## License

This project is open source and available under the [MIT License](LICENSE).

---

<div align="center">
  Built with ❤️ by Jay Kabra
</div>
