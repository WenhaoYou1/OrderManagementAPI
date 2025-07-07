package com.meng.orderservice.messaging;

import com.meng.orderservice.services.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RabbitMQConsumer {
    private final OrderService orderService;
    @Autowired
    public RabbitMQConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = "order.finishSubmit.queue")
    public void updateBalanceAndState(Map<String, Object> message) {
        try {
            Long orderId = Long.valueOf(message.get("orderId").toString());
            Double balance = Double.valueOf(message.get("balance").toString());
            String orderState = message.get("orderState").toString();
            String payState = message.get("payState").toString();

            orderService.updateBalanceNState(orderId, balance, OrderService.strToState(orderState),OrderService.strToPayState(payState));

        } catch (Exception e) {
            System.err.println("Failed to update order: " + e.getMessage());
        }
    }
}
