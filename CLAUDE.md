# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Primary Directive

**This is a learning project.** The user is learning Spring Boot. Do NOT write code yourself. Instead, provide hints, explanations, and pointers so the user writes the code themselves. Only write or edit code when the user explicitly asks you to.

## Project Overview

Copywriter portfolio/blog website (wisniewska.eu) with a public landing page and admin panel for managing blog posts. Built with Spring Boot 4.1.0-SNAPSHOT, Thymeleaf, Spring Security, Spring Data JPA, and PostgreSQL.

The project is in early stage — only the Spring Boot skeleton exists. The implementation plan is in `docs:plans/copywriter_portfolio_project_plan.md`.

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

- **Java 25** / **Spring Boot 4.1.0-SNAPSHOT** (uses Spring Snapshots repository)
- **Thymeleaf** with Spring Security integration (`thymeleaf-extras-springsecurity6`)
- **Spring Data JPA** with **PostgreSQL**
- **Lombok** for boilerplate reduction (annotation processor configured in maven-compiler-plugin)
- **Maven 3.9.12** via wrapper (`./mvnw`)

## Architecture (Planned)

The target structure follows standard Spring Boot layered architecture:

- `eu.wisniewska.www.controller` — MVC controllers (`HomeController`, `AdminController`)
- `eu.wisniewska.www.entity` — JPA entities (`Post`, `User`)
- `eu.wisniewska.www.repository` — Spring Data repositories
- `eu.wisniewska.www.service` — Business logic services
- `eu.wisniewska.www.config` — Security and app configuration (`SecurityConfig`)
- `src/main/resources/templates/` — Thymeleaf templates (public + `admin/` subdirectory)
- `src/main/resources/static/` — CSS, JS, images

## Key Design Decisions

- Server-side rendering with Thymeleaf (not a SPA)
- Spring Security protects `/admin/**` routes; public routes (`/`, `/post/**`) are unauthenticated
- Configuration uses YAML format (`application.yaml`)
- PostgreSQL is the only supported database (no H2 fallback configured)
- Database schema managed via `spring.jpa.hibernate.ddl-auto`
