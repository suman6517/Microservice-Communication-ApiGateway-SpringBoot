package com.codingSuman.ecommerce.InventoryService.repository;

import com.codingSuman.ecommerce.InventoryService.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product,Long>
{
}
