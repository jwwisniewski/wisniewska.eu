# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Primary Directive

**This is a learning project.** The user is learning Spring Boot. Do NOT write code yourself. Instead, provide hints, explanations, and pointers so the user writes the code themselves. Only write or edit code when the user explicitly asks you to.

## Project Overview

Copywriter portfolio/blog website (wisniewska.eu) with a public landing page and admin panel for managing blog posts. Built with Spring Boot 4.0.3, Thymeleaf, Spring Security, Spring Data JPA, and H2 (dev) / PostgreSQL (prod).

The implementation plan is in `docs/plans/copywriter_portfolio_project_plan.md`.

## Build & Run Commands

```bash
# Build
./mvnw clean package

# Run
./mvnw spring-boot:run

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=WwwApplicationTests

# Run a single test method
./mvnw test -Dtest=WwwApplicationTests#contextLoads
```

## Tech Stack

- **Java 21** / **Spring Boot 4.0.3**
- **Thymeleaf** with Spring Security integration (`thymeleaf-extras-springsecurity6`)
- **Spring Data JPA** with **H2** (development) and **PostgreSQL** (production on Railway)
- **Lombok** for boilerplate reduction (annotation processor configured in maven-compiler-plugin)
- **Maven 3.9.12** via wrapper (`./mvnw`)

## Architecture (Planned)

The target structure follows standard Spring Boot layered architecture:

- `eu.wisniewska.www.controller` — MVC controllers (`IndexController`, `AdminController`, `AdminUserController`)
- `eu.wisniewska.www.entity` — JPA entities (`AdminUser`; `Post` planned)
- `eu.wisniewska.www.repository` — Spring Data repositories (`AdminUserRepository`)
- `eu.wisniewska.www.service` — Business logic services (`AdminUserService`)
- `eu.wisniewska.www.config` — Security and app configuration (`SecurityConfig`)
- `src/main/resources/templates/` — Thymeleaf templates (public + `admin/` subdirectory)
- `src/main/resources/static/` — CSS, JS, images

## Key Design Decisions

- Server-side rendering with Thymeleaf (not a SPA)
- Spring Security protects `/_admin/**` routes (requires `ADMIN` role); public routes (`/`, `/actuator/**`) are unauthenticated
- Service account configured via `app.admin.*` properties (overridable via env vars in production)
- Configuration uses YAML format (`application.yaml`) with profile overrides (`application-production.yaml`, `application-test.yaml`)
- H2 file-based database for development (`create-drop`); PostgreSQL for production (`update`)
- Database schema managed via `spring.jpa.hibernate.ddl-auto`
- Deployed to Railway with PostgreSQL; CI via GitHub Actions
