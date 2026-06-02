package com.codingSuman.ecommerce.OrderService.service;


import com.codingSuman.ecommerce.OrderService.client.InventoryOpenFeignClient;
import com.codingSuman.ecommerce.OrderService.dto.OrderRequestDto;
import com.codingSuman.ecommerce.OrderService.entity.OrderItem;
import com.codingSuman.ecommerce.OrderService.entity.OrderStatus;
import com.codingSuman.ecommerce.OrderService.entity.Orders;
import com.codingSuman.ecommerce.OrderService.repository.OrdersRepo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService
{
    private final OrdersRepo orderRepository;
    private final ModelMapper modelMapper;
    private final InventoryOpenFeignClient inventoryOpenFeignClient;

    public List<OrderRequestDto> getAllOrders() {
        log.info("Fetching all orders");
        List<Orders> orders = orderRepository.findAll();
        return orders.stream().map(order -> modelMapper.map(order, OrderRequestDto.class)).toList();
    }

    public OrderRequestDto getOrderById(Long id) {
        log.info("Fetching order with ID: {}", id);
        Orders order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        return modelMapper.map(order, OrderRequestDto.class);
    }

    // @Retry(name = "inventoryRetry" , fallbackMethod = "createOrderFallback")
    @CircuitBreaker(name = "inventoryCircuitBreaker" , fallbackMethod = "createOrderFallback")
    //@RateLimiter(name = "inventoryRateLimiter" , fallbackMethod = "createOrderFallback")
    public OrderRequestDto createOrder(OrderRequestDto orderRequestDto)
    {
        log.info("Calling The Create Order Method");
        Double totalPrice = inventoryOpenFeignClient.reduceStocks(orderRequestDto); // Third Party Call

        Orders order = modelMapper.map(orderRequestDto, Orders.class);

        for(OrderItem orderItem : order.getItems())
        {
            orderItem.setOrder(order);
        }

        order.setTotalPrice(totalPrice);
        order.setOrderStatus(OrderStatus.CONFIRMED);

        orderRepository.save(order);
        return modelMapper.map(order, OrderRequestDto.class);
    }

    public OrderRequestDto createOrderFallback(OrderRequestDto orderRequestDto , Throwable throwable)
    {
        log.error("Fallback occurred due to: {} ", throwable.getMessage());

        return new OrderRequestDto();
    }
}
