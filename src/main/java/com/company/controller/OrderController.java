package com.company.controller;

import com.company.entity.Order;
import com.company.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderController {

    private final ObjectMapper objectMapper;

    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<String> getOrderById(@PathVariable Long id) throws JsonProcessingException {
        Order order = orderService.getOrderById(id);
        String json = objectMapper.writeValueAsString(order);
        return ResponseEntity.ok(json);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody String text) throws JsonProcessingException {
        Order order = objectMapper.readValue(text, Order.class);
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        Order updatedOrder = orderService.updateOrder(id, order);
        return updatedOrder != null ? ResponseEntity.ok(updatedOrder) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
