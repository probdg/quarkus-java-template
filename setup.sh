#!/bin/bash

# Setup script for Quarkus Java Template
# This script sets up Git hooks and validates the development environment

set -e

echo "🚀 Setting up Quarkus Java Template..."
echo ""

# Check Java version
echo "📋 Checking Java version..."
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -ge 17 ]; then
        echo "✅ Java $JAVA_VERSION detected"
    else
        echo "❌ Java 17 or higher is required. Current version: $JAVA_VERSION"
        exit 1
    fi
else
    echo "❌ Java is not installed"
    exit 1
fi

# Check Maven
echo ""
echo "📋 Checking Maven..."
if command -v mvn &> /dev/null; then
    MVN_VERSION=$(mvn -version | head -n 1 | awk '{print $3}')
    echo "✅ Maven $MVN_VERSION detected"
else
    echo "❌ Maven is not installed"
    exit 1
fi

# Setup Git hooks directory
echo ""
echo "📋 Setting up Git hooks..."
if [ -d ".git" ]; then
    mkdir -p .git/hooks
    
    # Create pre-commit hook
    cat > .git/hooks/pre-commit << 'EOF'
#!/bin/bash

echo "🔍 Running pre-commit checks..."

# Run formatter check
echo "📝 Checking code formatting..."
if ! mvn formatter:validate -q; then
    echo "❌ Code formatting issues found. Run 'mvn formatter:format' to fix."
    exit 1
fi

# Run checkstyle
echo "🔍 Running Checkstyle..."
if ! mvn checkstyle:check -q; then
    echo "❌ Checkstyle violations found. Please fix them."
    exit 1
fi

# Compile code
echo "🔨 Compiling code..."
if ! mvn clean compile -q -DskipTests; then
    echo "❌ Compilation failed. Please fix the errors."
    exit 1
fi

# Run tests
echo "🧪 Running tests..."
if ! mvn test -q; then
    echo "❌ Tests failed. Please fix them."
    exit 1
fi

echo "✅ All pre-commit checks passed!"
exit 0
EOF
    
    chmod +x .git/hooks/pre-commit
    echo "✅ Git pre-commit hook installed"
else
    echo "⚠️  Not a Git repository. Skipping Git hooks setup."
fi

# Create .env file if it doesn't exist
echo ""
echo "📋 Setting up environment variables..."
if [ ! -f ".env" ]; then
    cp .env.example .env
    echo "✅ Created .env file from .env.example"
    echo "⚠️  Please update the .env file with your configuration"
else
    echo "✅ .env file already exists"
fi

# Download dependencies
echo ""
echo "📦 Downloading dependencies..."
mvn dependency:resolve -q
echo "✅ Dependencies downloaded"

# Build project
echo ""
echo "🔨 Building project..."
if mvn clean compile -q -DskipTests; then
    echo "✅ Project built successfully"
else
    echo "❌ Build failed"
    exit 1
fi

# Run tests
echo ""
echo "🧪 Running tests..."
if mvn test -q; then
    echo "✅ All tests passed"
else
    echo "❌ Some tests failed"
    exit 1
fi

echo ""
echo "✨ Setup completed successfully!"
echo ""
echo "Next steps:"
echo "  1. Update .env with your configuration"
echo "  2. Review README.md for documentation"
echo "  3. Run 'mvn quarkus:dev' to start development server"
echo ""
echo "Happy coding! 🎉"
