package com.stlang.store.controller.admin;

import com.stlang.store.domain.Order;
import com.stlang.store.dto.OrderDTO;
import com.stlang.store.exception.DataIncorrectFormatException;
import com.stlang.store.service.jwt.JWTService;
import com.stlang.store.service.serviceimpl.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${api.path}")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JWTService jwtService;

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.findAllOrders();
        return ResponseEntity.status(OK).body(orders);
    }

    @PostMapping("/orders")
    public ResponseEntity<String> addOrder(@Valid @RequestBody OrderDTO orderDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new DataIncorrectFormatException("Dữ liệu chưa đủ để tạo đơn hàng");
        }
        Order order =  orderService.createOrder(orderDTO);
        if(order != null) {
            return ResponseEntity.status(OK).body("Create Order Success");
        }
        return ResponseEntity.status(BAD_REQUEST).body("Create Order Failed");
    }

    @GetMapping("/history")
    public ResponseEntity<List<Order>> getOrderHistory(@RequestHeader("Authorization") String token) {
        String accessToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(accessToken);
        if(username == null) {
            return ResponseEntity.status(BAD_REQUEST).body(null);
        }
        List<Order> orders = orderService.getOrders(username);
        return ResponseEntity.status(OK).body(orders);
    }

}
