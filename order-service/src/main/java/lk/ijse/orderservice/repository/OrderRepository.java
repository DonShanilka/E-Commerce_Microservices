package lk.ijse.orderservice.repository;

import lk.ijse.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);
    List<Order> findTop100ByUserIdOrderByCreatedAtDesc(Long userId);

    @Query(value = "SELECT CAST(o.created_at AS date) AS sale_date, SUM(oi.quantity) AS units_sold " +
            "FROM orders o JOIN order_items oi ON oi.order_id = o.id " +
            "WHERE oi.product_id = :productId AND o.status IN ('COMPLETED') " +
            "AND o.created_at >= :fromDate GROUP BY CAST(o.created_at AS date) ORDER BY sale_date",
            nativeQuery = true)
    List<Object[]> findDailySalesForProduct(@Param("productId") Long productId, @Param("fromDate") LocalDate fromDate);
}