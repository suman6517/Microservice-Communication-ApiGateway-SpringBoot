package com.codingSuman.ecommerce.OrderService.cpntroller;

import com.codingSuman.ecommerce.OrderService.client.InventoryOpenFeignClient;
import com.codingSuman.ecommerce.OrderService.dto.OrderRequestDto;
import com.codingSuman.ecommerce.OrderService.service.OrdersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
@Slf4j
@RefreshScope
public class OrdersController
{
    private final OrdersService ordersService;



    @GetMapping
    public ResponseEntity<List<OrderRequestDto>> getAllOrders(HttpServletRequest httpServletRequest) {
        log.info("Fetching all orders via controller");
        List<OrderRequestDto> orders = ordersService.getAllOrders();
        return ResponseEntity.ok(orders);  // Returns 200 OK with the list of orders
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderRequestDto> getOrderById(@PathVariable Long id) {
        log.info("Fetching order with ID: {} via controller", id);
        OrderRequestDto order = ordersService.getOrderById(id);
        return ResponseEntity.ok(order);  // Returns 200 OK with the order
    }

    //@RequestHeader("X-User-Id") Long userId
    @GetMapping("/helloOrders")
    public String helloOrders()
    {
        int a = 5;
        return "The Value of a is: "+a;
        //return "Hello World!";
    }

    @PostMapping("/create-order")
    public ResponseEntity<OrderRequestDto> createOrder(@RequestBody OrderRequestDto orderRequestDto)
    {
        OrderRequestDto orderRequestDto1 = ordersService.createOrder(orderRequestDto);
        return ResponseEntity.ok(orderRequestDto1);

    }

}
