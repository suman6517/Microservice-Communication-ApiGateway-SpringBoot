package com.codingSuman.ecommerce.InventoryService.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto
{
    private List<OrderRequestItemDto> items;
}

