package com.company.service;

import com.company.entity.Order;
import com.company.entity.Product;
import com.company.exception.CustomerNotFoundException;
import com.company.exception.OrderNotFoundException;
import com.company.repostory.CustomerRepository;
import com.company.repostory.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;


    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }


    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }


    public Order createOrder(Order order) {
        order.setOrderDate(String.valueOf(new Date()));

        double totalPrice = order.getProducts().stream()
                .mapToDouble(Product::getPrice)
                .sum();
        order.setTotalPrice(totalPrice);

        if (!customerRepository.existsById(order.getCustomer().getCustomerId())) {
            throw new CustomerNotFoundException("Customer does not exist");
        }

        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, Order order) {
        if (orderRepository.existsById(id)) {
            order.setOrderId(id);

            double totalPrice = order.getProducts().stream()
                    .mapToDouble(Product::getPrice)
                    .sum();
            order.setTotalPrice(totalPrice);

            return orderRepository.save(order);
        }
        return null;
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
