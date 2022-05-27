package com.example.demo.service;

import com.example.demo.converter.OrderConverter;
import com.example.demo.dto.OrderDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderConverter orderConverter;

    public OrderDto save(OrderDto orderDto) {
        Order savedOrder = orderRepository.save(orderConverter.fromOrderDtoToOrder(orderDto));
        return orderConverter.fromOrderToOrderDto(savedOrder);
    }

    public void delete(Integer id) throws NotFoundException {
        orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order with id: " + id + " does not exist"));
        orderRepository.deleteById(id);
    }

    public List<OrderDto> findAll() {
        return orderRepository
                .findAll()
                .stream()
                .map(orderConverter::fromOrderToOrderDto)
                .collect(Collectors.toList());
    }

    public OrderDto findById(Integer id) throws NotFoundException {
        return orderConverter.fromOrderToOrderDto(orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order with id: " + id + " does not exist")));
    }
}
