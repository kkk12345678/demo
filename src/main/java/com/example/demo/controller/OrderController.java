package com.example.demo.controller;

import com.example.demo.converter.BookSetConverter;
import com.example.demo.converter.OrderConverter;
import com.example.demo.converter.UserConverter;
import com.example.demo.dto.BookSetDto;
import com.example.demo.dto.OrderDto;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
@Log
public class OrderController {
    private final UserService userService;
    private final OrderService orderService;
    private final BookSetService bookSetService;
    private final UserConverter userConverter;
    private final BookSetConverter bookSetConverter;

    @PostMapping("/save")
    public OrderDto save(
            @RequestParam Integer userId,
            @RequestParam Integer bookSetId,
            @RequestParam Integer quantity
    ) throws ValidationException, NotFoundException {
        //retrieving user with userId from database
        UserDto userDto = userService.findById(userId);
        if (isNull(userDto)) {
            throw new NotFoundException("User with id : " + userId + "does not exist.");
        }
        BookSetDto bookSetDto = bookSetService.findById(bookSetId);
        if (isNull(bookSetDto)) {
            throw new NotFoundException("BookSet with id : " + bookSetId + "does not exist.");
        }
        log.info("Handling save order of " + userDto.getEmail() + ".");
        OrderDto orderDto = new OrderDto();
        orderDto.setUser(userConverter.fromUserDtoToUser(userDto));
        orderDto.setBookSet(bookSetConverter.fromBookSetDtoToBookSet(bookSetDto));
        orderDto.setQuantity(quantity);
        return orderService.save(orderDto);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws NotFoundException {
        log.info("Handling delete order with id: " + id);
        orderService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public List<OrderDto> findAll() {
        log.info("Handling find all orders request");
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public OrderDto findOne(@PathVariable Integer id) throws NotFoundException {
        log.info("Handling find order with id: " + id);
        OrderDto orderDto = orderService.findById(id);
        if (isNull(orderDto)) {
            throw new NotFoundException("Order with id: " + id.toString() + " does not exist");
        }
        return orderDto;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFoundHandler(NotFoundException e) {
        return e.getMessage();
    }

}
