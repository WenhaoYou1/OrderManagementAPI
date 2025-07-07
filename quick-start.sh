#!/bin/bash

echo "🚀 Order Service Quick Start Script"
echo "================================"

# Check Java version
echo "📋 Checking Java version..."
if ! command -v java &> /dev/null; then
    echo "❌ Java not installed, please install Java 17 or higher first"
    exit 1
fi

java_version=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$java_version" -lt 17 ]; then
    echo "❌ Java version too low, requires Java 17 or higher"
    exit 1
fi
echo "✅ Java version check passed: $(java -version 2>&1 | head -n 1)"

# Check Docker
echo "📋 Checking Docker..."
if ! command -v docker &> /dev/null; then
    echo "❌ Docker not installed, please install Docker first"
    exit 1
fi
echo "✅ Docker installed"

# Set Maven Wrapper permissions
echo "📋 Setting Maven Wrapper permissions..."
chmod +x mvnw
echo "✅ Maven Wrapper permissions set"

# Build project
echo "📋 Building project..."
./mvnw clean install -DskipTests
if [ $? -ne 0 ]; then
    echo "❌ Project build failed"
    exit 1
fi
echo "✅ Project build completed"

# Stop and remove existing containers
echo "📋 Cleaning existing containers..."
docker stop pg-taxfile rabbitmq order-service 2>/dev/null || true
docker rm pg-taxfile rabbitmq order-service 2>/dev/null || true
echo "✅ Container cleanup completed"

# Start PostgreSQL
echo "📋 Starting PostgreSQL database..."
docker run --name pg-taxfile \
  -e POSTGRES_DB=taxfile_db \
  -e POSTGRES_USER=javaorderdbapi \
  -e POSTGRES_PASSWORD=59fbfd738908c1f6b2dddd57bd67573910de67aac5ef0b15afa1cb9b91bb3b02 \
  -p 5433:5432 \
  -d postgres:14

if [ $? -ne 0 ]; then
    echo "❌ PostgreSQL startup failed"
    exit 1
fi
echo "✅ PostgreSQL started successfully (port: 5433)"

# Wait for PostgreSQL to start
echo "⏳ Waiting for PostgreSQL to start..."
sleep 10

# Start RabbitMQ
echo "📋 Starting RabbitMQ message queue..."
docker run -d --hostname rabbitmq-host --name rabbitmq \
  -p 5672:5672 -p 15672:15672 \
  -e RABBITMQ_DEFAULT_USER=guest \
  -e RABBITMQ_DEFAULT_PASS=guest \
  rabbitmq:3-management

if [ $? -ne 0 ]; then
    echo "❌ RabbitMQ startup failed"
    exit 1
fi
echo "✅ RabbitMQ started successfully (port: 5672, management: 15672)"

# Wait for RabbitMQ to start
echo "⏳ Waiting for RabbitMQ to start..."
sleep 10

# Start application
echo "📋 Starting order service..."
./mvnw spring-boot:run &
APP_PID=$!

# Wait for application to start
echo "⏳ Waiting for application to start..."
sleep 30

# Check if application started successfully
if curl -s http://localhost:9004/orders/getAll > /dev/null 2>&1; then
    echo "✅ Order service started successfully!"
    echo ""
    echo "🎉 Deployment completed!"
    echo "================================"
    echo "📊 Service URLs:"
    echo "   - Order Service API: http://localhost:9004"
    echo "   - RabbitMQ Management: http://localhost:15672 (username/password: guest/guest)"
    echo ""
    echo "🧪 Test APIs:"
    echo "   - Get all orders: curl http://localhost:9004/orders/getAll"
    echo "   - Run test script: ./test-api.sh"
    echo ""
    echo "📝 Log file: logs/order-service-app.log"
    echo ""
    echo "🛑 Stop services:"
    echo "   - Stop application: kill $APP_PID"
    echo "   - Stop database: docker stop pg-taxfile"
    echo "   - Stop message queue: docker stop rabbitmq"
    echo ""
    echo "🔧 Using Docker Compose (recommended):"
    echo "   - Start: docker-compose up -d"
    echo "   - Stop: docker-compose down"
else
    echo "❌ Application startup failed, please check logs"
    kill $APP_PID 2>/dev/null || true
    exit 1
fi

# Keep script running
echo "Press Ctrl+C to stop services..."
wait $APP_PID 