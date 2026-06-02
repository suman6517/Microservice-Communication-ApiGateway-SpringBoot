package com.codingSuman.ecommerce.OrderService.service;


import com.codingSuman.ecommerce.OrderService.client.InventoryOpenFeignClient;
import com.codingSuman.ecommerce.OrderService.dto.OrderRequestDto;
import com.codingSuman.ecommerce.OrderService.entity.OrderItem;
import com.codingSuman.ecommerce.OrderService.entity.OrderStatus;
import com.codingSuman.ecommerce.OrderService.entity.Orders;
import com.codingSuman.ecommerce.OrderService.repository.OrdersRepo;
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

    public OrderRequestDto createOrder(OrderRequestDto orderRequestDto)
    {
        Double totalPrice = inventoryOpenFeignClient.reduceStocks(orderRequestDto);

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
}
