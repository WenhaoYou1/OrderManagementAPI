package com.meng.orderservice.controller;

import com.meng.orderservice.dto.OrderDTO;
import com.meng.orderservice.dto.OrderWNicknameDTO;
import com.meng.orderservice.entity.OrderState;
import com.meng.orderservice.entity.PayState;
import com.meng.orderservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.createOrder(orderDTO));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/getByIdWNickname/{id}")
    public ResponseEntity<?> getOrderWNicknameById(@PathVariable long id) {
        try{
            OrderDTO dto = orderService.getOrderById(id);
            if(dto==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Order not found"));
            }
            List<OrderWNicknameDTO> list=orderService.attachNicknames(List.of(orderService.getOrderById(id)));
            if(list==null){return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Order w nickname not found"));}
            if(list.isEmpty()){return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Order w nickname not found"));}
            return ResponseEntity.ok(list.get(0));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }

    }

    @GetMapping("/getByUser/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrderByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @GetMapping("/getByTaxYear/{taxYear}")
    public ResponseEntity<List<OrderDTO>> getOrderByTaxYear(@PathVariable Integer taxYear) {
        return ResponseEntity.ok(orderService.getOrderByTaxYear(taxYear));
    }

    @GetMapping("/getByState/{state}")
    public ResponseEntity<List<OrderDTO>> getOrderByState(@PathVariable String state) {
        return ResponseEntity.ok(orderService.getOrdersByState(OrderService.strToState(state)));
    }

    @GetMapping("/getPaidOrdersByState/{state}")
    public ResponseEntity<List<OrderDTO>> getPaidOrdersByState(@PathVariable String state) {
        OrderState orderState = OrderService.strToState(state);
        if (orderState == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(orderService.getPaidOrdersByState(orderState));
    }

    @GetMapping("/getPaidOrdersByStateWNickname/{state}")
    public ResponseEntity<List<OrderWNicknameDTO>> getPaidOrdersWNicknameByState(@PathVariable String state) {
        OrderState orderState = OrderService.strToState(state);
        if (orderState == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(orderService.attachNicknames(orderService.getPaidOrdersByState(orderState)));
    }

    @GetMapping("/getByStateWNickname/{state}")
    public ResponseEntity<?> getOrderWNicknameByState(@PathVariable String state) {
        try{
            return ResponseEntity.ok(orderService.attachNicknames(orderService.getOrdersByState(OrderService.strToState(state))));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/getByDateCreate")
    public ResponseEntity<List<OrderDTO>> getOrderByDateCreate(@RequestParam("start") String start, @RequestParam("end") String end) {
        return ResponseEntity.ok(
                orderService.getOrderByDateCreate(OffsetDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME),
                        OffsetDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME)));
    }

    @GetMapping("/getByDateModify")
    public ResponseEntity<List<OrderDTO>> getOrderByDateModify(@RequestParam("start") String start, @RequestParam("end") String end) {
        return ResponseEntity.ok(
                orderService.getOrderByDateModify(OffsetDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME),
                        OffsetDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME)));
    }

    @GetMapping("/getByPaymentState/{state}")
    public ResponseEntity<List<OrderDTO>> getOrderByPaymentState(@PathVariable String state) {
        return ResponseEntity.ok(orderService.getOrdersByPayState(OrderService.strToPayState(state)));
    }

    @GetMapping("/getByPaymentStateWNickname/{state}")
    public ResponseEntity<?> getOrderWNicknameByPaymentState(@PathVariable String state) {
        try{
            return ResponseEntity.ok(orderService.attachNicknames(orderService.getOrdersByPayState(OrderService.strToPayState(state))));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/getByDatePay")
    public ResponseEntity<List<OrderDTO>> getOrderByDatePay(@RequestParam("start") String start, @RequestParam("end") String end) {
        return ResponseEntity.ok(
                orderService.getOrderByDatePay(OffsetDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME),
                        OffsetDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME)));
    }

    @GetMapping("/getByBalance")
    public ResponseEntity<List<OrderDTO>> getOrderByBalance(@RequestParam("start") Double start, @RequestParam("end") Double end) {
        return ResponseEntity.ok(orderService.getOrderByBalance(start, end));
    }

    @PutMapping("/updateTaxYear/{id}")
    public ResponseEntity<?> updateTaxYear(@PathVariable long id, @RequestParam("year") Integer taxYear) {
        OrderDTO resp=orderService.updateTaxYear(id, taxYear);
        if(resp!=null){
            return ResponseEntity.ok(resp);
        } else{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No rows updated for order ID: " + id);
        }
    }

    @PutMapping("/updateState/{id}")
    public ResponseEntity<?> updateOrderState(@PathVariable Long id, @RequestParam("state") String state) {
        OrderDTO resp=orderService.updateState(id, OrderService.strToState(state));
        if(resp!=null){
            return ResponseEntity.ok(resp);
        } else{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No rows updated for order ID: " + id);
        }
    }

    @PutMapping("/updateDate/{id}")
    public ResponseEntity<?> updateDateModify(@PathVariable Long id) {
        OrderDTO resp=orderService.updateDateModify(id);
        if(resp!=null){
            return ResponseEntity.ok(resp);
        } else{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No rows updated for order ID: " + id);
        }
    }

    @PutMapping("/updateBalance/{id}")
    public ResponseEntity<?> updateBalance(@PathVariable Long id, @RequestParam("balance") Double balance) {
        OrderDTO resp=orderService.updateBalance(id, balance);
        if(resp!=null){
            return ResponseEntity.ok(resp);
        } else{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No rows updated for order ID: " + id);
        }
    }

    @PutMapping("/updatePaymentState/{id}")
    public ResponseEntity<?> updatePaymentState(@PathVariable Long id, @RequestParam("state") String state) {
        OrderDTO resp=orderService.updatePayState(id, OrderService.strToPayState(state));
        if(resp!=null){
            return ResponseEntity.ok(resp);
        } else{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No rows updated for order ID: " + id);
        }
    }

    @PutMapping("/updateDatePay/{id}")
    public ResponseEntity<?> updateDatePay(@PathVariable Long id) {
        OrderDTO resp=orderService.updateDatePay(id);
        if(resp!=null){
            return ResponseEntity.ok(resp);
        } else{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No rows updated for order ID: " + id);
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteOrderById(@PathVariable long id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted for order ID: " + id);
    }

    @DeleteMapping("/deleteByUser/{userId}")
    public ResponseEntity<?> deleteOrderByUserId(@PathVariable String userId) {
        orderService.deleteOrderByUserId(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted for user ID: " + userId);
    }

    @DeleteMapping("/deleteByTaxYear/{year}")
    public ResponseEntity<?> deleteOrderByTaxYear(@PathVariable Integer year) {
        orderService.deleteOrderByTaxYear(year);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted for tax year: " + year);
    }

    @PutMapping("/updateBalanceNPaymentState/{id}")
    public ResponseEntity<?> updateBalanceNPaymentState(@PathVariable Long id, @RequestParam("orderState") String orderState, @RequestParam("payState") String payState,
                                                @RequestParam("balance") Double balance) {
        try{
            OrderDTO resp=orderService.updateBalanceNState(id,balance, OrderService.strToState(orderState),OrderService.strToPayState(payState));
            if(resp!=null){
                return ResponseEntity.ok(resp);
            } else{
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No rows updated for order ID: " + id);
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/getHomePageMsg/{userId}")
    public ResponseEntity<Map<String,String>> getHomePageMsg(@PathVariable String userId) {
        try{
            String str= orderService.getHomePageMsg(userId);
            Map<String, String> result=Map.of("message", str);
            return ResponseEntity.ok(result);
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/batchUpdateState")
    public ResponseEntity<?> batchUpdateOrderStates(
            @RequestBody List<String> orderIds,
            @RequestParam("orderState") String orderState,
            @RequestParam("payState") String payState) {

        List<Long> orderIdList = orderIds.stream().map(Long::valueOf).collect(Collectors.toList());
        OrderState targetOrderState = OrderService.strToState(orderState);
        PayState targetPayState = OrderService.strToPayState(payState);

        if (targetOrderState == null || targetPayState == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid orderState or payState"));
        }

        int updatedCount = orderService.batchUpdateOrderStates(orderIdList, targetOrderState, targetPayState);

        if (updatedCount > 0) {
            return ResponseEntity.ok(Map.of("message", "Successfully updated " + updatedCount + " orders."));
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message","No orders updated."));
        }
    }

}
