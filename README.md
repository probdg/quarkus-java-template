# Quarkus Java Template

[![CI/CD Pipeline](https://github.com/probdg/quarkus-java-template/workflows/CI/CD%20Pipeline/badge.svg)](https://github.com/probdg/quarkus-java-template/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=quarkus-template&metric=alert_status)](https://sonarcloud.io/dashboard?id=quarkus-template)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=quarkus-template&metric=coverage)](https://sonarcloud.io/dashboard?id=quarkus-template)

Template Quarkus siap produksi dengan best practices, quality gates, dan DevOps tools. Template ini mengikuti standar industri dan terinspirasi dari [nuxt-template](https://github.com/sadigitID/nuxt-template).

## 📋 Fitur

| Fitur | Package/Tool | Deskripsi |
|-------|--------------|-----------|
| **Framework** | Quarkus 3.17.5 | Supersonic Subatomic Java Framework |
| **Bahasa** | Java 17 | LTS version dengan modern features |
| **Build Tool** | Maven | Dependency management dan build automation |
| **REST API** | RESTEasy Reactive | Reactive REST endpoints |
| **Database** | PostgreSQL + Panache | ORM dengan Active Record pattern |
| **Validation** | Hibernate Validator | Bean validation |
| **Health Check** | SmallRye Health | Liveness dan Readiness probes |
| **Metrics** | Micrometer + Prometheus | Application metrics |
| **OpenAPI** | SmallRye OpenAPI | API documentation (Swagger) |
| **Logging** | JSON Logging | Structured logging |
| **Secrets Management** | HashiCorp Vault | Secure secrets and configuration management |
| **Testing** | JUnit 5 + REST Assured | Unit dan integration testing |
| **Code Quality** | Checkstyle + PMD + SpotBugs | Static code analysis |
| **Coverage** | JaCoCo | Code coverage reporting |
| **SonarQube** | SonarQube Scanner | Continuous code quality |
| **CI/CD** | GitHub Actions | Automated pipeline |
| **Docker** | Dockerfile + Compose | Containerization |

## 🚀 Quick Start

### Prasyarat

- [Java 17+](https://adoptium.net/)
- [Maven 3.8+](https://maven.apache.org/)
- [Docker](https://www.docker.com/) (optional)
- [PostgreSQL 16](https://www.postgresql.org/) (optional, untuk development)

### Setup

```bash
# Clone repository
git clone https://github.com/probdg/quarkus-java-template.git
cd quarkus-java-template

# Copy environment variables
cp .env.example .env

# Install dependencies dan build
mvn clean install

# Jalankan development server
mvn quarkus:dev
```

Development server berjalan di `http://localhost:8080`.

> **Hot Reload**: Quarkus memiliki fitur hot reload otomatis. Setiap perubahan kode akan langsung ter-reload tanpa restart server.

## 📁 Struktur Project

```
quarkus-template/
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── controller/       # REST endpoints
│   │   │   ├── service/          # Business logic
│   │   │   ├── repository/       # Data access layer
│   │   │   ├── model/            # Domain models
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── config/           # Configuration classes
│   │   │   └── exception/        # Exception handlers
│   │   └── resources/
│   │       └── application.yml   # Application configuration
│   └── test/
│       ├── java/com/example/     # Test classes
│       └── resources/
│           └── application.yml   # Test configuration
│
├── .github/
│   └── workflows/
│       └── ci.yml                # CI/CD pipeline
│
├── docker/                       # Docker configurations
├── pom.xml                       # Maven configuration
├── checkstyle.xml                # Checkstyle rules
├── pmd-ruleset.xml               # PMD rules
├── formatter.xml                 # Code formatter config
├── Dockerfile                    # Docker build
├── docker-compose.yml            # Docker Compose
└── README.md                     # Documentation

```

## 🛠 Scripts Maven

| Perintah | Deskripsi |
|----------|-----------|
| `mvn quarkus:dev` | Jalankan development server dengan hot reload |
| `mvn clean install` | Build project dan install ke local repository |
| `mvn test` | Jalankan unit tests |
| `mvn verify` | Jalankan unit dan integration tests |
| `mvn checkstyle:check` | Jalankan Checkstyle |
| `mvn pmd:check` | Jalankan PMD |
| `mvn spotbugs:check` | Jalankan SpotBugs |
| `mvn jacoco:report` | Generate code coverage report |
| `mvn sonar:sonar` | Jalankan SonarQube analysis |
| `mvn clean package` | Build JAR file untuk production |
| `mvn clean package -Pnative` | Build native executable |

## 🌐 API Endpoints

### Greeting API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/greeting` | Get default greeting |
| GET | `/api/greeting/{name}` | Get personalized greeting |
| POST | `/api/greeting` | Create personalized greeting |

### Secured API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/secured/public` | Public endpoint (no auth required) |
| GET | `/api/secured/user` | User endpoint (requires user or admin role) |
| GET | `/api/secured/admin` | Admin endpoint (requires admin role) |
| GET | `/api/secured/vault-secret` | Get secret from Vault |

### Health & Metrics

| Endpoint | Description |
|----------|-------------|
| `/health` | Overall health status |
| `/health/live` | Liveness probe |
| `/health/ready` | Readiness probe |
| `/metrics` | Prometheus metrics |

### Documentation

| Endpoint | Description |
|----------|-------------|
| `/api/swagger` | OpenAPI specification (JSON) |
| `/api/swagger-ui` | Swagger UI documentation |

## 🧪 Testing

```bash
# Jalankan semua tests
mvn test

# Jalankan specific test class
mvn test -Dtest=GreetingControllerTest

# Jalankan dengan coverage
mvn clean test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

## 🐳 Docker

### Build dan Jalankan

```bash
# Build image
docker build -t quarkus-template .

# Jalankan container
docker run -p 8080:8080 quarkus-template

# Atau gunakan Docker Compose (termasuk PostgreSQL)
docker-compose up -d

# Lihat logs
docker-compose logs -f app

# Stop containers
docker-compose down
```

### Docker Compose Services

- **app**: Quarkus application (port 8080)
- **postgres**: PostgreSQL database (port 5432)
- **redis**: Redis cache (port 6379)
- **kafka**: Kafka message broker (port 9092)
- **zookeeper**: Zookeeper for Kafka (port 2181)
- **minio**: MinIO object storage (port 9000, console: 9001)
- **vault**: HashiCorp Vault for secrets management (port 8200)
- **pgadmin**: PgAdmin web interface (port 5050, profile: dev)

```bash
# Jalankan dengan PgAdmin (development)
docker-compose --profile dev up -d
```

## 🔐 HashiCorp Vault Integration

Template ini terintegrasi dengan HashiCorp Vault untuk manajemen secrets yang aman.

### Fitur Vault

- **Secrets Management**: Simpan dan kelola secrets dengan aman (API keys, passwords, tokens)
- **Dynamic Secrets**: Generate secrets on-demand dengan TTL
- **Encryption as a Service**: Enkripsi dan dekripsi data
- **Access Control**: Fine-grained access control policies

### Setup Vault

#### Development Mode (Docker Compose)

Vault sudah termasuk dalam `docker-compose.yml` dan akan berjalan secara otomatis:

```bash
# Start all services including Vault
docker-compose up -d

# Check Vault status
docker-compose logs vault
```

**Default Credentials (Development Only):**
- URL: `http://localhost:8200`
- Root Token: `root`
- Username: `quarkus`
- Password: `quarkus`

#### Initialize Vault for Development

Setelah Vault berjalan, inisialisasi secrets untuk aplikasi:

```bash
# Set environment variables
export VAULT_ADDR=http://localhost:8200
export VAULT_TOKEN=root

# Enable userpass authentication (if not already enabled)
docker-compose exec vault vault auth enable userpass

# Create a user for the application
docker-compose exec vault vault write auth/userpass/users/quarkus \
  password=quarkus \
  policies=default

# Write example secrets to Vault
# Note: The full path in Vault is secret/application/{your-path}
# When using VaultService, only provide the path after secret/application/
docker-compose exec vault vault kv put secret/application/config/app \
  api-key=example-api-key-12345 \
  db-encryption-key=secret-encryption-key-67890

# Read secrets to verify
docker-compose exec vault vault kv get secret/application/config/app
```

### Using Vault in Your Application

#### 1. Inject VaultService

```java
import com.example.service.VaultService;
import jakarta.inject.Inject;

@ApplicationScoped
public class MyService {
  
  @Inject
  VaultService vaultService;
  
  public void useSecret() {
    // Get secret from Vault
    // Note: Only provide the path after 'secret/application/'
    // The full path in Vault would be: secret/application/config/app
    Map<String, String> secrets = vaultService.getSecret("config/app");
    String apiKey = secrets.get("api-key");
    
    // Write secret to Vault
    vaultService.writeSecret("config/new-secret", 
      Map.of("username", "user", "password", "pass"));
  }
}
```

#### 2. Example Endpoint

Contoh endpoint tersedia di `SecuredController`:

```bash
# Get secret from Vault
curl http://localhost:8080/api/secured/vault-secret
```

### Vault Configuration

Configuration Vault dapat ditemukan di `application.yml`:

```yaml
quarkus:
  vault:
    url: ${VAULT_URL:http://localhost:8200}
    authentication:
      userpass:
        username: ${VAULT_USERNAME:quarkus}
        password: ${VAULT_PASSWORD:quarkus}
    # Note: This is commented out to avoid eager connection during startup
    # Uncomment only if you want to use Vault as a configuration source
    # secret-config-kv-path: secret/application
    kv-secret-engine-version: 2
```

### Production Considerations

⚠️ **Catatan Penting untuk Production:**

1. **Jangan gunakan dev mode**: Gunakan production Vault server dengan TLS
2. **Gunakan authentication method yang sesuai**: AppRole, Kubernetes, atau Cloud auth
3. **Enable audit logging**: Track semua akses ke secrets
4. **Implement secret rotation**: Rotasi secrets secara berkala
5. **Use proper policies**: Terapkan least-privilege access control
6. **Secure the Vault token**: Jangan commit atau log Vault tokens
7. **Enable TLS**: Selalu gunakan HTTPS untuk komunikasi dengan Vault

Untuk production setup, lihat [Quarkus Vault Guide](https://quarkus.io/guides/vault).

## 🔍 Code Quality

### Checkstyle

Memeriksa style dan formatting kode berdasarkan Google Java Style Guide.

```bash
mvn checkstyle:check
```

### PMD

Mendeteksi code smells dan potential bugs.

```bash
mvn pmd:check
```

### SpotBugs

Mendeteksi bugs menggunakan static analysis.

```bash
mvn spotbugs:check
```

### JaCoCo

Generate code coverage report.

```bash
mvn clean test jacoco:report
open target/site/jacoco/index.html
```

### SonarQube

Untuk menjalankan SonarQube analysis, set environment variables:

```bash
export SONAR_TOKEN=your-token-here
mvn clean verify sonar:sonar
```

## 🔐 Environment Variables

Copy `.env.example` ke `.env` dan sesuaikan:

```properties
# Application
PORT=8080

# Database
DB_URL=jdbc:postgresql://localhost:5432/quarkus_template
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Security
JWT_SECRET=changeme-generate-a-secure-secret-key
JWT_EXPIRATION=86400

# Vault
VAULT_URL=http://localhost:8200
VAULT_USERNAME=quarkus
VAULT_PASSWORD=quarkus

# Logging
LOG_LEVEL=INFO

# Environment
ENVIRONMENT=development
```

## 📦 Production Build

### JVM Mode

```bash
# Build uber JAR
mvn clean package -DskipTests

# Run
java -jar target/quarkus-template-1.0.0-SNAPSHOT-runner.jar
```

### Native Mode

Memerlukan GraalVM:

```bash
# Build native executable
mvn clean package -Pnative -DskipTests

# Run (startup sangat cepat!)
./target/quarkus-template-1.0.0-SNAPSHOT-runner
```

## 🔄 CI/CD

Pipeline otomatis menggunakan GitHub Actions:

1. **Code Quality**: Checkstyle, PMD, SpotBugs
2. **Build & Test**: Compile, unit tests, coverage
3. **Integration Tests**: Tests dengan PostgreSQL
4. **SonarQube**: Code quality analysis
5. **Docker**: Build dan push image

### Required Secrets

Configure di GitHub repository settings:

- `SONAR_TOKEN`: SonarQube token
- `DOCKER_USERNAME`: Docker Hub username
- `DOCKER_PASSWORD`: Docker Hub password/token

## 🎯 Best Practices

Template ini mengikuti best practices berikut:

### 1. Code Organization
- ✅ Separation of concerns (Controller, Service, Repository)
- ✅ DTOs untuk data transfer
- ✅ Exception handlers terpusat
- ✅ Validation menggunakan Bean Validation

### 2. Testing
- ✅ Unit tests untuk business logic
- ✅ Integration tests untuk API endpoints
- ✅ Minimum 50% code coverage

### 3. Code Quality
- ✅ Checkstyle untuk code style consistency
- ✅ PMD untuk code quality checks
- ✅ SpotBugs untuk bug detection
- ✅ SonarQube untuk continuous inspection

### 4. Security
- ✅ Input validation
- ✅ Exception handling yang proper
- ✅ No hardcoded secrets
- ✅ Security headers

### 5. Operations
- ✅ Health checks (liveness & readiness)
- ✅ Metrics & monitoring
- ✅ Structured logging
- ✅ Docker containerization

### 6. Documentation
- ✅ OpenAPI/Swagger documentation
- ✅ Code comments
- ✅ README dengan examples
- ✅ API endpoint documentation

## 📚 Resources

- [Quarkus Documentation](https://quarkus.io/guides/)
- [RESTEasy Reactive Guide](https://quarkus.io/guides/resteasy-reactive)
- [Hibernate ORM Panache](https://quarkus.io/guides/hibernate-orm-panache)
- [Testing Guide](https://quarkus.io/guides/getting-started-testing)
- [Docker Guide](https://quarkus.io/guides/container-image)

## 🤝 Contributing

1. Fork repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'feat: add amazing feature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## 📄 License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.

## 👥 Authors

- **Template Creator** - [probdg](https://github.com/probdg)

## 🙏 Acknowledgments

- Inspired by [nuxt-template](https://github.com/sadigitID/nuxt-template)
- Quarkus team for the amazing framework
- Community contributors