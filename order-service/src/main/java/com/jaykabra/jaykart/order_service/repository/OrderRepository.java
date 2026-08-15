package com.jaykabra.jaykart.order_service.repository;


import com.jaykabra.jaykart.order_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

  /**
   * Fetch orders for a customer, sorted by creation date in descending order.
   */
  List<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

  List<Order> findByOrderStatus(String orderStatus);

  @Query("SELECT o FROM Order o WHERE o.customerId=:customerId ORDER BY o.createdAt DESC")
  List<Order> findOrdersByCustomerId(@Param("customerId") Long customerId);

  @Query("SELECT o FROM Order o WHERE o.orderStatus=?1")
  List<Order> findOrdersByStatus(String orderStatus);

  @Query(value = "SELECT * FROM orders o WHERE o.customer_id=:customerId ORDER BY o.created_at DESC"
  , nativeQuery = true)
  List<Order> findOrdersByCustomerWithNativeQuery(@Param("customerId") Long customerId);

  @Query(value = "SELECT * FROM orders o WHERE o.order_status=?1", nativeQuery = true)
  List<Order> findOrdersByStatusWithNativeQuery(String orderStatus);

  @Transactional
  @Modifying
  @Query("UPDATE Order o SET o.orderStatus=:orderStatus,o.updatedAt=CURRENT_TIMESTAMP,o.updatedBy=:updatedBy WHERE o.orderId=:orderId")
  int updateOrderStatus(@Param("orderId") Long orderId, @Param("orderStatus") String orderStatus,
          @Param("updatedBy") String updatedBy);
}