.PHONY: help setup dev build test clean docker docker-up docker-down lint format check install

# Default target
.DEFAULT_GOAL := help

# Colors for output
CYAN := \033[0;36m
GREEN := \033[0;32m
YELLOW := \033[0;33m
RED := \033[0;31m
NC := \033[0m # No Color

## help: Show this help message
help:
	@echo "$(CYAN)Quarkus Java Template - Available Commands$(NC)"
	@echo ""
	@grep -E '^## .*:' $(MAKEFILE_LIST) | sed 's/## //' | column -t -s ':'

## setup: Initialize project (install dependencies, setup hooks)
setup:
	@echo "$(CYAN)Setting up project...$(NC)"
	@chmod +x setup.sh
	@./setup.sh

## dev: Run development server with hot reload
dev:
	@echo "$(CYAN)Starting development server...$(NC)"
	mvn quarkus:dev

## build: Build the application
build:
	@echo "$(CYAN)Building application...$(NC)"
	mvn clean package -DskipTests

## build-native: Build native executable
build-native:
	@echo "$(CYAN)Building native executable...$(NC)"
	mvn clean package -Pnative -DskipTests

## test: Run all tests
test:
	@echo "$(CYAN)Running tests...$(NC)"
	mvn test

## test-coverage: Run tests with coverage report
test-coverage:
	@echo "$(CYAN)Running tests with coverage...$(NC)"
	mvn clean test jacoco:report
	@echo "$(GREEN)Coverage report: target/site/jacoco/index.html$(NC)"

## verify: Run all verification (tests + integration tests)
verify:
	@echo "$(CYAN)Running verification...$(NC)"
	mvn verify

## lint: Run all linters (Checkstyle, PMD, SpotBugs)
lint:
	@echo "$(CYAN)Running linters...$(NC)"
	@echo "$(YELLOW)Checkstyle...$(NC)"
	@mvn checkstyle:check
	@echo "$(YELLOW)PMD...$(NC)"
	@mvn pmd:check
	@echo "$(YELLOW)SpotBugs...$(NC)"
	@mvn spotbugs:check
	@echo "$(GREEN)✓ All linters passed$(NC)"

## format: Format code
format:
	@echo "$(CYAN)Formatting code...$(NC)"
	mvn formatter:format
	@echo "$(GREEN)✓ Code formatted$(NC)"

## format-check: Check code formatting
format-check:
	@echo "$(CYAN)Checking code formatting...$(NC)"
	mvn formatter:validate

## check: Run all quality checks
check: lint format-check test
	@echo "$(GREEN)✓ All checks passed$(NC)"

## clean: Clean build artifacts
clean:
	@echo "$(CYAN)Cleaning build artifacts...$(NC)"
	mvn clean
	@echo "$(GREEN)✓ Clean completed$(NC)"

## install: Install to local Maven repository
install:
	@echo "$(CYAN)Installing to local repository...$(NC)"
	mvn clean install
	@echo "$(GREEN)✓ Installation completed$(NC)"

## sonar: Run SonarQube analysis
sonar:
	@echo "$(CYAN)Running SonarQube analysis...$(NC)"
	mvn clean verify sonar:sonar

## docker: Build Docker image
docker:
	@echo "$(CYAN)Building Docker image...$(NC)"
	docker build -t quarkus-template:latest .
	@echo "$(GREEN)✓ Docker image built$(NC)"

## docker-up: Start Docker Compose services
docker-up:
	@echo "$(CYAN)Starting Docker services...$(NC)"
	docker-compose up -d
	@echo "$(GREEN)✓ Services started$(NC)"

## docker-down: Stop Docker Compose services
docker-down:
	@echo "$(CYAN)Stopping Docker services...$(NC)"
	docker-compose down
	@echo "$(GREEN)✓ Services stopped$(NC)"

## docker-logs: Show Docker Compose logs
docker-logs:
	docker-compose logs -f

## run: Run the application (requires build)
run:
	@echo "$(CYAN)Running application...$(NC)"
	java -jar target/quarkus-template-*-runner.jar

## deps-update: Check for dependency updates
deps-update:
	@echo "$(CYAN)Checking for dependency updates...$(NC)"
	mvn versions:display-dependency-updates

## deps-tree: Display dependency tree
deps-tree:
	@echo "$(CYAN)Displaying dependency tree...$(NC)"
	mvn dependency:tree

## swagger: Open Swagger UI
swagger:
	@echo "$(CYAN)Opening Swagger UI...$(NC)"
	@echo "http://localhost:8080/api/swagger-ui"
	@xdg-open http://localhost:8080/api/swagger-ui 2>/dev/null || open http://localhost:8080/api/swagger-ui 2>/dev/null || echo "Please open http://localhost:8080/api/swagger-ui in your browser"

## health: Check application health
health:
	@echo "$(CYAN)Checking application health...$(NC)"
	@curl -s http://localhost:8080/health | jq . || echo "Application not running or jq not installed"

## metrics: View Prometheus metrics
metrics:
	@echo "$(CYAN)Fetching metrics...$(NC)"
	@curl -s http://localhost:8080/metrics

## info: Display project information
info:
	@echo "$(CYAN)Project Information$(NC)"
	@echo "Name: Quarkus Java Template"
	@echo "Version: 1.0.0-SNAPSHOT"
	@echo "Java: $(shell java -version 2>&1 | head -n 1)"
	@echo "Maven: $(shell mvn -version | head -n 1)"
	@echo ""
	@echo "$(CYAN)Endpoints:$(NC)"
	@echo "  Application: http://localhost:8080"
	@echo "  Health: http://localhost:8080/health"
	@echo "  Metrics: http://localhost:8080/metrics"
	@echo "  Swagger: http://localhost:8080/api/swagger-ui"
