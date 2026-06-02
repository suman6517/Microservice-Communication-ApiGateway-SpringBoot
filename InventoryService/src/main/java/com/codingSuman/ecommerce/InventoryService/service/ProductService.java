package com.codingSuman.ecommerce.InventoryService.service;

import com.codingSuman.ecommerce.InventoryService.dto.OrderRequestDto;
import com.codingSuman.ecommerce.InventoryService.dto.OrderRequestItemDto;
import com.codingSuman.ecommerce.InventoryService.dto.ProductDto;
import com.codingSuman.ecommerce.InventoryService.entity.Product;
import com.codingSuman.ecommerce.InventoryService.repository.ProductRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService
{
    private final ProductRepo productRepository;
    private final ModelMapper modelMapper;

    public List<ProductDto> getAllInventory() {
        log.info("Fetching all inventory items");
        List<Product> inventories = productRepository.findAll();
        return inventories.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
    }

    public ProductDto getProductById(Long id) {
        log.info("Fetching Product with ID: {}", id);
        Optional<Product> inventory = productRepository.findById(id);
        return inventory.map(item -> modelMapper.map(item, ProductDto.class))
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
    }

    @Transactional
    public Double reduceStocks(OrderRequestDto orderRequestDto)
    {
        log.info("Reducing stocks");
        Double totalPrice=0.0;

        for(OrderRequestItemDto orderRequestItemDto : orderRequestDto.getItems())
        {
            Long productId = orderRequestItemDto.getProductId();
            Integer quantity = orderRequestItemDto.getQuantity();

            Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

            if(product.getStock() < quantity)
            {
                throw new RuntimeException("Stock exceeded");
            }

            product.setStock(product.getStock() - quantity);
            totalPrice += quantity * product.getPrice();
        }

        return totalPrice;
    }
}
