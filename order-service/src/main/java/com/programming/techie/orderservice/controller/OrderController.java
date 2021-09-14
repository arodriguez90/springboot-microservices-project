package com.programming.techie.orderservice.controller;

import com.programming.techie.orderservice.client.InventoryClient;
import com.programming.techie.orderservice.dto.OrderDto;
import com.programming.techie.orderservice.model.Order;
import com.programming.techie.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/order")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    @PostMapping
    public String placeOrder(@RequestBody OrderDto orderDto){
        boolean allProductsInStock = orderDto.getOrderLineItemsList().stream()
                .allMatch(orderLineItems -> inventoryClient.checkStock(orderLineItems.getSkuCode()));

        if(allProductsInStock){
            Order order = new Order();
            order.setOrderLineItems(orderDto.getOrderLineItemsList());
            order.setOrderNumber(UUID.randomUUID().toString());

            orderRepository.save(order);

            return "Order Place Successfully";
        }else{
            return "Order Failed, One of the products in the order is not in stock";
        }
    }
}
