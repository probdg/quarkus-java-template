# Contributing to Quarkus Java Template

Thank you for your interest in contributing to the Quarkus Java Template! This document provides guidelines for contributing to this project.

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Workflow](#development-workflow)
- [Commit Convention](#commit-convention)
- [Pull Request Process](#pull-request-process)
- [Coding Standards](#coding-standards)
- [Testing Guidelines](#testing-guidelines)

## 📜 Code of Conduct

This project adheres to a code of conduct. By participating, you are expected to uphold this code.

## 🚀 Getting Started

1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/YOUR_USERNAME/quarkus-java-template.git
   cd quarkus-java-template
   ```
3. **Add upstream remote**:
   ```bash
   git remote add upstream https://github.com/probdg/quarkus-java-template.git
   ```
4. **Install dependencies**:
   ```bash
   mvn clean install
   ```

## 🔄 Development Workflow

1. **Create a feature branch**:
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make your changes** and ensure they follow our coding standards

3. **Test your changes**:
   ```bash
   mvn clean test
   mvn checkstyle:check
   mvn pmd:check
   mvn spotbugs:check
   ```

4. **Commit your changes** following our commit convention

5. **Push to your fork**:
   ```bash
   git push origin feature/your-feature-name
   ```

6. **Open a Pull Request**

## 💬 Commit Convention

We follow [Conventional Commits](https://www.conventionalcommits.org/) specification:

### Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- **feat**: A new feature
- **fix**: A bug fix
- **docs**: Documentation changes
- **style**: Code style changes (formatting, missing semi colons, etc)
- **refactor**: Code refactoring
- **perf**: Performance improvements
- **test**: Adding or updating tests
- **build**: Changes to build system or dependencies
- **ci**: Changes to CI configuration
- **chore**: Other changes that don't modify src or test files

### Examples

```bash
# Feature
git commit -m "feat(api): add user authentication endpoint"

# Bug fix
git commit -m "fix(validation): correct email validation regex"

# Documentation
git commit -m "docs(readme): update installation instructions"

# Refactoring
git commit -m "refactor(service): simplify greeting logic"
```

## 📝 Pull Request Process

1. **Update documentation** if needed (README, API docs, etc.)
2. **Add tests** for new features or bug fixes
3. **Ensure all tests pass**:
   ```bash
   mvn clean verify
   ```
4. **Update the CHANGELOG** if applicable
5. **Request review** from maintainers
6. **Address review comments** if any
7. **Wait for approval** and merge

### Pull Request Checklist

- [ ] Code follows the project's coding standards
- [ ] Tests are added/updated and passing
- [ ] Documentation is updated
- [ ] Checkstyle, PMD, and SpotBugs checks pass
- [ ] No merge conflicts
- [ ] Commit messages follow convention
- [ ] PR description clearly explains the changes

## 🎨 Coding Standards

### Java Code Style

We follow Google Java Style Guide with some modifications:

- **Indentation**: 4 spaces (not tabs)
- **Line length**: Maximum 100 characters
- **Naming conventions**:
  - Classes: `PascalCase`
  - Methods/Variables: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
  - Packages: `lowercase`

### Code Quality Tools

Your code must pass:

1. **Checkstyle**: Code style verification
   ```bash
   mvn checkstyle:check
   ```

2. **PMD**: Code quality checks
   ```bash
   mvn pmd:check
   ```

3. **SpotBugs**: Bug detection
   ```bash
   mvn spotbugs:check
   ```

### Best Practices

- ✅ Write clean, self-documenting code
- ✅ Add JavaDoc comments for public APIs
- ✅ Use meaningful variable and method names
- ✅ Keep methods small and focused
- ✅ Follow SOLID principles
- ✅ Avoid code duplication
- ✅ Handle exceptions properly
- ✅ Validate input data
- ✅ Log important operations

## 🧪 Testing Guidelines

### Test Coverage

- Minimum **50%** overall coverage
- **80%+** coverage for critical business logic
- Test both success and failure cases

### Test Structure

```java
@QuarkusTest
class ServiceTest {
    
    @Inject
    MyService service;
    
    @Test
    void testSuccessCase() {
        // Given
        String input = "test";
        
        // When
        String result = service.process(input);
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("test"));
    }
    
    @Test
    void testFailureCase() {
        // Given
        String invalidInput = "";
        
        // When & Then
        assertThrows(ValidationException.class, () -> {
            service.process(invalidInput);
        });
    }
}
```

### Testing Commands

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=GreetingControllerTest

# Run with coverage
mvn clean test jacoco:report

# Run integration tests
mvn verify
```

## 🐛 Reporting Bugs

1. **Check existing issues** to avoid duplicates
2. **Use the issue template** if available
3. **Include**:
   - Clear description
   - Steps to reproduce
   - Expected vs actual behavior
   - Environment details (Java version, OS, etc.)
   - Stack traces or error messages

## 💡 Suggesting Features

1. **Check existing feature requests**
2. **Open a new issue** with:
   - Clear use case
   - Expected behavior
   - Possible implementation approach
   - Benefits to the project

## ❓ Questions?

If you have questions about contributing:

1. Check the [README](README.md)
2. Search existing issues
3. Open a new issue with the "question" label

## 📄 License

By contributing, you agree that your contributions will be licensed under the Apache License 2.0.

---

Thank you for contributing to Quarkus Java Template! 🎉
