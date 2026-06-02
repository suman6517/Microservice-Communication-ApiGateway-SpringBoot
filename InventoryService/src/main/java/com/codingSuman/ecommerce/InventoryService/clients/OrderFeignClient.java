package com.codingSuman.ecommerce.InventoryService.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "OrderService" , path = "/orders")
public interface OrderFeignClient
{
    @GetMapping("/core/helloOrders")
     String helloOrders();
}
