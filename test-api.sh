#!/bin/bash

echo "=== Order Service API Test ==="
echo "Service URL: http://localhost:9004"
echo ""

# Test if service is running
echo "1. Testing service health status..."
curl -s http://localhost:9004/actuator/health || echo "Service not running or health check endpoint not available"

echo ""
echo "2. Getting all orders..."
curl -s http://localhost:9004/orders/getAll | jq '.' 2>/dev/null || echo "Service not running or returned error"

echo ""
echo "3. Creating test order..."
curl -s -X POST http://localhost:9004/orders/create \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "test-user-001",
    "taxYear": 2024,
    "balance": 299.99,
    "orderState": "PENDING",
    "paymentState": "UNPAID"
  }' | jq '.' 2>/dev/null || echo "Service not running or returned error"

echo ""
echo "=== API Endpoints List ==="
echo "POST   /orders/create                    - Create order"
echo "GET    /orders/getAll                    - Get all orders"
echo "GET    /orders/getById/{id}              - Get order by ID"
echo "GET    /orders/getByUser/{userId}        - Get orders by user ID"
echo "GET    /orders/getByTaxYear/{taxYear}    - Get orders by tax year"
echo "GET    /orders/getByState/{state}        - Get orders by order state"
echo "GET    /orders/getByPaymentState/{state} - Get orders by payment state"
echo "PUT    /orders/updateState/{id}          - Update order state"
echo "PUT    /orders/updateBalance/{id}        - Update order amount"
echo "PUT    /orders/updatePaymentState/{id}   - Update payment state"
echo "DELETE /orders/deleteById/{id}           - Delete order"
echo ""
echo "=== Order States (OrderState) ==="
echo "PENDING   - Pending"
echo "PROCESSING - Processing"
echo "COMPLETED - Completed"
echo "CANCELLED - Cancelled"
echo ""
echo "=== Payment States (PayState) ==="
echo "UNPAID    - Unpaid"
echo "PAID      - Paid"
echo "REFUNDED  - Refunded" 