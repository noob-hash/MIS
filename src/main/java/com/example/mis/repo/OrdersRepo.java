package com.example.mis.repo;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.mis.entity.Orders;

@Repository
public interface OrdersRepo extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM Orders o WHERE o.status = :status")
    List<Orders> findByStatus(String status);

    @Query(value = "SELECT orders.id, users_details.name, " +
            "(SELECT SUM(order_item.price) FROM order_item WHERE order_item.order_id = orders.id) AS total_amount, " +
            "DATE(orders.last_updated_date) AS last_purchase_date " +
            "FROM orders " +
            "JOIN users_details ON users_details.id = orders.supplier_id", nativeQuery = true)
    List<Map<String, Object>> getOrderSummary();

    @Query(value = """
                SELECT o.id,
                    o.order_date,
                    o.status,
                    o.total_amount,
                    o.created_date,
                    o.last_updated_date,
                    u.name,
                    u.org_name,
                    u.contact,
                    oi.id,
                    oi.quantity,
                    oi.price,
                    (oi.quantity * oi.price),
                    inv.name,
                    inv.description,
                    inv.sku,
                    inv.unit,
                    inv.manufacturer,
                    inv.category
                FROM orders o
                JOIN users_details u ON o.supplier_id = u.id
                JOIN order_item oi ON o.id = oi.order_id
                JOIN inventory inv ON oi.item_id = inv.id
                ORDER BY o.id, oi.id
            """, nativeQuery = true)
    List<Object[]> findAllOrderDetails();

    // For single order
    @Query(value = """
            SELECT o.id as order_id, o.order_date, o.status,o.total_amount, o.created_date,o.last_updated_date,
                u.name as supplier_name,u.org_name as supplier_organization,u.contact as supplier_contact,
                oi.id as item_id, oi.quantity,
                oi.price as unit_price,
                (oi.quantity * oi.price) as subtotal,
                inv.name as item_name,
                inv.description as item_description,
                inv.sku as item_sku,
                inv.unit as item_unit,
                inv.manufacturer as item_manufacturer,
                inv.category as item_category
            FROM orders o
            JOIN users_details u ON o.supplier_id = u.id
            JOIN order_item oi ON o.id = oi.order_id
            JOIN inventory inv ON oi.item_id = inv.id
            WHERE o.id = :orderId
            """, nativeQuery = true)
    List<Object[]> findOrderDetailsById(@Param("orderId") Long orderId);

}
