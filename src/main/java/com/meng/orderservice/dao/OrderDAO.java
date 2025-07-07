package com.meng.orderservice.dao;

import com.meng.orderservice.entity.Order;
import com.meng.orderservice.entity.OrderState;
import com.meng.orderservice.entity.PayState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDAO extends JpaRepository<Order, Long> {
    public Order findByOrderId(Long orderId);
    public List<Order> findByUserId(String userId);
    public List<Order> findByTaxYear(Integer year);
    public List<Order> findByOrderState(OrderState orderState);
    public List<Order> findByPaymentState(PayState paymentState);
    public List<Order> findByBalanceBetween(Double start, Double end);
    public List<Order> findByDatePayBetween(OffsetDateTime start, OffsetDateTime end);
    public List<Order> findByDateCreateBetween(OffsetDateTime start, OffsetDateTime end);
    public List<Order> findByDateModifyBetween(OffsetDateTime start, OffsetDateTime end);

    public Optional<Order> findByUserIdAndOrderStateAndPaymentState(String userId, OrderState orderState, PayState paymentState);

    @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.orderState <> 'NOT_SUBMITTED' AND o.paymentState = 'UNPAID' AND o.balance > 0.01")
    List<Order> findValidUnpaidOrderByUserId(String userId);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.taxYear = :newTaxYear, o.dateModify = CURRENT_TIMESTAMP WHERE o.orderId = :orderId")
    int updateTaxYear(Long orderId, Integer newTaxYear);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.orderState = :newOrderState, o.dateModify = CURRENT_TIMESTAMP WHERE o.orderId = :orderId")
    int updateOrderState(Long orderId, OrderState newOrderState);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.dateModify = CURRENT_TIMESTAMP WHERE o.orderId = :orderId")
    int updateDateModify(Long orderId);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.balance = :newBalance, o.dateModify = CURRENT_TIMESTAMP WHERE o.orderId = :orderId")
    int updateBalance(Long orderId, Double newBalance);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.datePay = CURRENT_TIMESTAMP WHERE o.orderId = :orderId")
    int updateDatePay(Long orderId);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.paymentState = :newPayState, o.datePay = CURRENT_TIMESTAMP WHERE o.orderId = :orderId")
    int updatePaymentState(Long orderId, PayState newPayState);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.balance = :newBalance, o.paymentState = :newPayState, o.orderState = :newOrderState WHERE o.orderId = :orderId")
    int updateBalanceNState(Long orderId, Double newBalance, OrderState newOrderState, PayState newPayState);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.orderState = :newOrderState, o.paymentState = :newPayState, o.dateModify = CURRENT_TIMESTAMP WHERE o.orderId IN :orderIds")
    int batchUpdateOrderStatesAndPayStates(@Param("orderIds") List<Long> orderIds, @Param("newOrderState") OrderState newOrderState, @Param("newPayState") PayState newPayState);

    @Query("SELECT o FROM Order o WHERE o.orderState = :orderState AND o.paymentState = 'PAID'")
    List<Order> findPaidOrdersByState(@Param("orderState") OrderState orderState);

    void deleteByOrderId(Long orderId);
    void deleteByUserId(String userId);
    void deleteByTaxYear(Integer taxYear);
}
