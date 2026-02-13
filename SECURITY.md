# Security Policy

## Supported Versions

We release patches for security vulnerabilities. The following versions are currently supported:

| Version | Supported          |
| ------- | ------------------ |
| 1.x.x   | :white_check_mark: |
| < 1.0   | :x:                |

## Reporting a Vulnerability

The Quarkus Java Template team takes security seriously. We appreciate your efforts to responsibly disclose your findings.

### How to Report a Security Vulnerability

**Please do NOT report security vulnerabilities through public GitHub issues.**

Instead, please report security vulnerabilities by emailing:

📧 **security@example.com**

### What to Include in Your Report

Please include the following information in your report:

- Type of vulnerability
- Full paths of source file(s) related to the vulnerability
- Location of the affected source code (tag/branch/commit or direct URL)
- Step-by-step instructions to reproduce the issue
- Proof-of-concept or exploit code (if possible)
- Impact of the vulnerability, including how an attacker might exploit it

### Response Timeline

You should expect a response within:

- **24 hours**: Initial acknowledgment of your report
- **7 days**: Assessment of the vulnerability and severity
- **30 days**: Resolution plan or patch release

### Security Best Practices

When using this template, please follow these security best practices:

#### 1. Environment Variables
- ✅ Never commit `.env` files
- ✅ Use strong, randomly generated secrets
- ✅ Rotate secrets regularly
- ✅ Use different secrets for each environment

```bash
# Generate secure secrets
openssl rand -base64 32
```

#### 2. Dependencies
- ✅ Keep dependencies up to date
- ✅ Review dependency vulnerabilities regularly
- ✅ Use tools like Dependabot or Snyk

```bash
# Check for vulnerabilities
mvn dependency:analyze
mvn versions:display-dependency-updates
```

#### 3. Database
- ✅ Use parameterized queries (already done with Panache)
- ✅ Enable SSL/TLS for database connections in production
- ✅ Use least privilege principle for database users
- ✅ Never expose database credentials

#### 4. API Security
- ✅ Validate all inputs
- ✅ Implement rate limiting
- ✅ Use HTTPS in production
- ✅ Implement proper authentication and authorization
- ✅ Set appropriate CORS policies

#### 5. Error Handling
- ✅ Don't expose stack traces in production
- ✅ Log errors securely (don't log sensitive data)
- ✅ Use generic error messages for users

#### 6. Docker Security
- ✅ Don't run containers as root (already configured)
- ✅ Keep base images updated
- ✅ Scan images for vulnerabilities
- ✅ Use multi-stage builds (already configured)

```bash
# Scan Docker image
docker scan quarkus-template:latest
```

#### 7. Code Quality
- ✅ Run Checkstyle, PMD, and SpotBugs
- ✅ Use SonarQube for continuous inspection
- ✅ Enable and monitor security-related compiler warnings

```bash
# Run all quality checks
mvn clean verify checkstyle:check pmd:check spotbugs:check
```

## Security Features

This template includes several security features out-of-the-box:

### 1. Input Validation
- Bean Validation for request validation
- Custom exception handlers for proper error responses

### 2. Health Checks
- Liveness and readiness probes
- Metrics for monitoring

### 3. Secure Defaults
- Non-root user in Docker
- Proper file permissions
- Minimal attack surface

### 4. Static Analysis
- Checkstyle for code style
- PMD for code quality
- SpotBugs for bug detection
- SonarQube integration

## Known Security Considerations

### Default Configuration
⚠️ The default configuration is for **development only**. Before deploying to production:

1. Change all default passwords and secrets
2. Enable HTTPS/TLS
3. Configure proper authentication
4. Set up rate limiting
5. Enable production logging
6. Configure proper CORS policies
7. Review and update security headers

### Database
⚠️ The default database credentials are insecure. Update these in production:

```yaml
# application.yml (production)
quarkus:
  datasource:
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    jdbc:
      url: ${DB_URL}?ssl=true&sslmode=require
```

## Vulnerability Disclosure Policy

We believe in responsible disclosure:

1. Report the vulnerability privately
2. Give us reasonable time to fix the issue
3. Public disclosure after fix is released (with your permission)
4. Credit will be given to security researchers who report valid vulnerabilities

## Security Updates

Subscribe to security updates:

- Watch this repository for security advisories
- Check the [CHANGELOG](CHANGELOG.md) for security-related changes
- Enable Dependabot alerts

## Contact

For security concerns, contact:
- Email: security@example.com
- PGP Key: [Available upon request]

## Recognition

We thank the following security researchers for their contributions:

- [Your name here]

---

Last Updated: February 2026
