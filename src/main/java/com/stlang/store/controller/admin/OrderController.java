package com.stlang.store.controller.admin;

import com.stlang.store.domain.Order;
import com.stlang.store.dto.OrderDTO;
import com.stlang.store.exception.DataIncorrectFormatException;
import com.stlang.store.service.serviceimpl.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${api.path}")
public class OrderController {

    @Autowired
    private OrderService orderService;

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

}
