package com.meng.orderservice.services;

import com.meng.orderservice.dao.OrderDAO;
import com.meng.orderservice.dto.OrderDTO;
import com.meng.orderservice.dto.OrderWNicknameDTO;
import com.meng.orderservice.entity.Order;
import com.meng.orderservice.entity.OrderState;
import com.meng.orderservice.entity.PayState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;


import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private OrderDAO orderDAO;
    private RestTemplate restTemplate;

    @Value("${paycode.api}")
    private String payCodeApiUrl;

    @Value("${nickname.api}")
    private String nicknameApiUrl;

    @Value("${wx.templateId}")
    private String templateId;

    public OrderService() {}
    @Autowired
    public OrderService(OrderDAO orderDAO, RestTemplate restTemplate) { this.orderDAO=orderDAO; this.restTemplate = restTemplate;}

    static public Order convertToEntity(OrderDTO dto){
        Order order = new Order();
        order.setOrderId(dto.getOrderId());
        order.setUserId(dto.getUserId());
        order.setDateCreate(dto.getDateCreate());
        order.setDateModify(dto.getDateModify());
        order.setOrderState(dto.getOrderState());
        order.setTaxYear(dto.getTaxYear());
        order.setBalance(dto.getBalance());
        order.setPaymentState(dto.getPaymentState());
        order.setDatePay(dto.getDatePay());
        order.setAdmin(dto.getAdmin());
        return order;
    }

    static public OrderDTO convertToDto(Order order){
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setUserId(order.getUserId());
        dto.setDateCreate(order.getDateCreate());
        dto.setDateModify(order.getDateModify());
        dto.setOrderState(order.getOrderState());
        dto.setTaxYear(order.getTaxYear());
        dto.setBalance(order.getBalance());
        dto.setPaymentState(order.getPaymentState());
        dto.setDatePay(order.getDatePay());
        dto.setAdmin(order.getAdmin());
        return dto;
    }

    static public OrderState strToState(String state){
        String cvtStr=state.toUpperCase();
        switch (cvtStr) {
            case "NOT_SUBMITTED":
                return OrderState.NOT_SUBMITTED;
            case "SUBMITTED":
                return OrderState.SUBMITTED;
            case "UNDER_REVIEW":
                return OrderState.UNDER_REVIEW;
            case "REVIEWED":
                return OrderState.REVIEWED;
            case "FINISHED":
                return OrderState.FINISHED;
            default: return null;
        }
    }

    static public PayState strToPayState(String payState){
        String cvtStr=payState.toUpperCase();
        switch (cvtStr) {
            case "UNPAID":
                return PayState.UNPAID;
            case "PAID":
                return PayState.PAID;
            case "UNSURE":
                return PayState.UNSURE;
            default: return null;
        }
    }

    static public String stateToStr(OrderState state){
        switch (state) {
            case NOT_SUBMITTED:
                return "NOT_SUBMITTED";
                case SUBMITTED:
                    return "SUBMITTED";
                    case UNDER_REVIEW:
                        return "UNDER_REVIEW";
                        case REVIEWED:
                            return "REVIEWED";
                            case FINISHED:
                                return "FINISHED";
                                default:
                                    return null;
        }
    }

    @Transactional
    public OrderDTO createOrder(OrderDTO dto){
        Order order = OrderService.convertToEntity(dto);
        order.setDateCreate(OffsetDateTime.now());
        order.setDateModify(OffsetDateTime.now());
        order.setOrderState(OrderState.NOT_SUBMITTED);
        return convertToDto(orderDAO.save(order));
    }


    public OrderDTO getOrderById(Long id){
        try{
            return OrderService.convertToDto(orderDAO.findByOrderId(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<OrderDTO> getAllOrders(){
        try{
            List<OrderDTO> lDTO=new ArrayList<OrderDTO>();
            List<Order> orders=orderDAO.findAll();
            for(int i=0;i<orders.size();i++){
                lDTO.add(OrderService.convertToDto(orders.get(i)));
            }
            return lDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<OrderDTO> getOrdersByUserId(String userId){
        try{
            List<Order> orders=orderDAO.findByUserId(userId);
            List<OrderDTO> lDTO=new ArrayList<>();
            for(int i=0;i<orders.size();i++){
                lDTO.add(OrderService.convertToDto(orders.get(i)));
            }
            return lDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<OrderDTO> getOrdersByState(OrderState state){
        try{
            List<Order> orders=orderDAO.findByOrderState(state);
            List<OrderDTO> lDTO=new ArrayList<>();
            for(int i=0;i<orders.size();i++){
                lDTO.add(OrderService.convertToDto(orders.get(i)));
            }
            return lDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<OrderDTO> getPaidOrdersByState(OrderState state) {
        try {
            List<Order> orders = orderDAO.findPaidOrdersByState(state);
            return orders.stream()
                    .map(OrderService::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<OrderDTO> getOrderByTaxYear(Integer taxYear){
        try{
            List<Order> orders=orderDAO.findByTaxYear(taxYear);
            List<OrderDTO> lDTO=new ArrayList<>();
            for(int i=0;i<orders.size();i++){
                lDTO.add(OrderService.convertToDto(orders.get(i)));
            }
            return lDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<OrderDTO> getOrderByDateCreate(OffsetDateTime start, OffsetDateTime end){
        try{
            List<Order> orders=orderDAO.findByDateCreateBetween(start, end);
            List<OrderDTO> lDTO=new ArrayList<>();
            for(int i=0;i<orders.size();i++){
                lDTO.add(OrderService.convertToDto(orders.get(i)));
            }
            return lDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<OrderDTO> getOrderByDateModify(OffsetDateTime start, OffsetDateTime end){
        try{
            List<Order> orders=orderDAO.findByDateModifyBetween(start, end);
            List<OrderDTO> lDTO=new ArrayList<>();
            for(int i=0;i<orders.size();i++){
                lDTO.add(OrderService.convertToDto(orders.get(i)));
            }
            return lDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<OrderDTO> getOrdersByPayState(PayState state){
        try{
            List<Order> orders=orderDAO.findByPaymentState(state);
            List<OrderDTO> lDTO=new ArrayList<>();
            for(int i=0;i<orders.size();i++){
                lDTO.add(OrderService.convertToDto(orders.get(i)));
            }
            return lDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<OrderDTO> getOrderByDatePay(OffsetDateTime start, OffsetDateTime end){
        try{
            List<Order> orders=orderDAO.findByDatePayBetween(start, end);
            List<OrderDTO> lDTO=new ArrayList<>();
            for(int i=0;i<orders.size();i++){
                lDTO.add(OrderService.convertToDto(orders.get(i)));
            }
            return lDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<OrderDTO> getOrderByBalance(Double start, Double end){
        try{
            List<Order> orders=orderDAO.findByBalanceBetween(start, end);
            List<OrderDTO> lDTO=new ArrayList<>();
            for(int i=0;i<orders.size();i++){
                lDTO.add(OrderService.convertToDto(orders.get(i)));
            }
            return lDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public OrderDTO updateTaxYear(Long orderId, Integer taxYear){
        try{
            int i=orderDAO.updateTaxYear(orderId, taxYear);
            if (i>0) {
                return OrderService.convertToDto(orderDAO.findByOrderId(orderId));
            }
            else return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public OrderDTO updateState (Long orderId, OrderState state){
        try{
            int i=orderDAO.updateOrderState(orderId, state);
            if (i>0) {
                return OrderService.convertToDto(orderDAO.findByOrderId(orderId));
            }
            else return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public OrderDTO updateDateModify(Long orderId){
        try{
            int i= orderDAO.updateDateModify(orderId);
            if (i>0) {
                return OrderService.convertToDto(orderDAO.findByOrderId(orderId));
            }
            else return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public OrderDTO updateBalance(Long orderId, Double balance){
        try{
            int i= orderDAO.updateBalance(orderId, balance);
            if (i>0) {
                return OrderService.convertToDto(orderDAO.findByOrderId(orderId));
            }
            else return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public OrderDTO updateBalanceNState(Long orderId, Double balance, OrderState orderState, PayState payState){
        try{
            int i= orderDAO.updateBalanceNState(orderId, balance, orderState, payState);
            if (i>0) {
                return OrderService.convertToDto(orderDAO.findByOrderId(orderId));
            }
            else return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public OrderDTO updateDatePay(Long orderId){
        try{
            int i= orderDAO.updateDatePay(orderId);
            if (i>0) {
                return OrderService.convertToDto(orderDAO.findByOrderId(orderId));
            }
            else return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public OrderDTO updatePayState (Long orderId, PayState state){
        try{
            int i=orderDAO.updatePaymentState(orderId, state);
            if (i>0) {
                return OrderService.convertToDto(orderDAO.findByOrderId(orderId));
            }
            else return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deleteOrderById(Long orderId){
        try{
            orderDAO.deleteByOrderId(orderId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deleteOrderByUserId(String userId){
        try{
            orderDAO.deleteByUserId(userId);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deleteOrderByTaxYear(Integer taxYear){
        try{
            orderDAO.deleteByTaxYear(taxYear);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public int batchUpdateOrderStates(List<Long> orderIds, OrderState newOrderState, PayState newPayState) {
        return orderDAO.batchUpdateOrderStatesAndPayStates(orderIds, newOrderState, newPayState);
    }


    public String getHomePageMsg(String userId){
        try{
            List<Order> orderList = orderDAO.findValidUnpaidOrderByUserId(userId);
            if(!orderList.isEmpty()){
                Order order = orderList.get(0);
                String payCode=getPayCode(order.getOrderId());
                return "您的订单税后价格为CA$"+String.format("%.2f",order.getBalance())+", 订单号"+order.getOrderId()+", 支付时要备注的Ref Code: "+payCode+", 感谢您的支持！";
            }
            else return "";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getPayCode(Long orderId){
        String url = this.payCodeApiUrl + orderId;
//        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null && responseBody.containsKey("payCode")) {
                return String.valueOf(responseBody.get("payCode")); // 取出 payCode 并转成 String
            } else {
                throw new RuntimeException("No payCode found in response");
            }
        } catch (HttpStatusCodeException e) {
            // 处理 HTTP 4xx / 5xx 错误
            int statusCode = e.getStatusCode().value();
            throw new RuntimeException("Error calling PayCode service: HTTP " + statusCode + ", "+ e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create pay code: " + e.getMessage(), e);
        }
    }

    public List<OrderWNicknameDTO> attachNicknames(List<OrderDTO> dtoList){
        try{
            if (dtoList == null || dtoList.isEmpty()) {
                return Collections.emptyList();
            }

            List<String> userIds = dtoList.stream()
                    .map(OrderDTO::getUserId)
                    .distinct()
                    .collect(Collectors.toList());

            String userServiceUrl = this.nicknameApiUrl;

            ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(
                    userServiceUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(userIds),
                    new ParameterizedTypeReference<Map<String, String>>() {}
            );

            Map<String, String> userNicknameMap = responseEntity.getBody() != null ? responseEntity.getBody() : new HashMap<>();

            return dtoList.stream()
                    .map(dto -> new OrderWNicknameDTO(dto, userNicknameMap.getOrDefault(dto.getUserId(), "Unknown")))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
