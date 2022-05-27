package com.example.demo.converter;

import com.example.demo.dto.OrderDto;
import com.example.demo.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter {
    public OrderDto fromOrderToOrderDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .user(order.getUser())
                .bookSet(order.getBookSet())
                .quantity(order.getQuantity())
                .build();
    }

    public Order fromOrderDtoToOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setId(orderDto.getId());
        order.setUser(orderDto.getUser());
        order.setBookSet(orderDto.getBookSet());
        order.setQuantity(orderDto.getQuantity());
        return order;
    }
}
