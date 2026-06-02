package com.codingSuman.ecommerce.InventoryService.controller;


import com.codingSuman.ecommerce.InventoryService.clients.OrderFeignClient;
import com.codingSuman.ecommerce.InventoryService.dto.OrderRequestDto;
import com.codingSuman.ecommerce.InventoryService.dto.OrderRequestItemDto;
import com.codingSuman.ecommerce.InventoryService.dto.ProductDto;
import com.codingSuman.ecommerce.InventoryService.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController
{

    private final ProductService productService;
    private final DiscoveryClient discoveryClient;
    private final RestClient restClient;
    private final OrderFeignClient orderFeignClient;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllInventory() {
        List<ProductDto> inventories = productService.getAllInventory();
        return ResponseEntity.ok(inventories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getInventoryById(@PathVariable Long id) {
        ProductDto inventory = productService.getProductById(id);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/fetchOrders")
    public String fetchFromOrders(HttpServletRequest httpServletRequest)
    {
//        ServiceInstance orderService = discoveryClient.getInstances("ORDERSERVICE").getFirst();

//        String response =  restClient.get()
//                .uri(orderService.getUri()+"/orders/core/helloOrders")
//                .retrieve()
//                .body(String.class);
//
//        return response;

        return orderFeignClient.helloOrders();
    }

    @PutMapping("/reduce-stocks")
    public ResponseEntity<Double> reduceStocks(@RequestBody OrderRequestDto orderRequestDto)
    {

        Double totalPrice= productService.reduceStocks(orderRequestDto);

        return ResponseEntity.ok(totalPrice);

    }
}
