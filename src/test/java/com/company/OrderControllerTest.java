package com.company;

import com.company.controller.OrderController;
import com.company.entity.Customer;
import com.company.entity.Order;
import com.company.entity.Product;
import com.company.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private Order order;
    private Customer customer;
    private Product product;

    @BeforeEach
    public void setup() {
        customer = new Customer();
        customer.setCustomerId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setContactNumber("123456789");

        product = new Product();
        product.setProductId(1L);
        product.setName("Product 1");
        product.setDescription("Description 1");
        product.setPrice(100.0);
        product.setQuantityInStock(10);

        order = new Order();
        order.setOrderId(1L);
        order.setCustomer(customer);
        order.setProducts(Collections.singletonList(product));
        order.setOrderDate("2024-01-01");
        order.setShippingAddress("123 Main St");
        order.setTotalPrice(100.0);
        order.setOrderStatus("PROCESSING");
    }

    @Test
    public void testGetOrderById() throws Exception {
        when(orderService.getOrderById(order.getOrderId())).thenReturn(order);

        mockMvc.perform(get("/api/v1/orders/{id}", order.getOrderId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(order.getOrderId()));
    }

    @Test
    public void testCreateOrder() throws Exception {
        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(order.getOrderId()));
    }

    @Test
    public void testGetAllOrders() throws Exception {
        when(orderService.getAllOrders()).thenReturn(Collections.singletonList(order));

        mockMvc.perform(get("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(order.getOrderId()));
    }

    @Test
    public void testUpdateOrder() throws Exception {
        when(orderService.updateOrder(Mockito.anyLong(), any(Order.class))).thenReturn(order);

        mockMvc.perform(put("/api/v1/orders/{id}", order.getOrderId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(order.getOrderId()));
    }

    @Test
    public void testDeleteOrder() throws Exception {
        mockMvc.perform(delete("/api/v1/orders/{id}", order.getOrderId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
