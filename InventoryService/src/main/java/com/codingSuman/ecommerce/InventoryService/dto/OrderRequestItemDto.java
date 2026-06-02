package com.codingSuman.ecommerce.InventoryService.dto;

import lombok.Data;

@Data
public class OrderRequestItemDto
{
    private Long productId;
    private Integer quantity;
}
