package com.codingSuman.ecommerce.OrderService.repository;

import com.codingSuman.ecommerce.OrderService.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepo extends JpaRepository<Orders, Long>
{
}
