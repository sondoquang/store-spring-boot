package com.stlang.store.service;

import com.stlang.store.domain.Order;
import com.stlang.store.dto.OrderDTO;

import java.util.List;

public interface IOrderService {

    Order createOrder(OrderDTO orderDTO);
    List<Order> getOrders(String username);
    List<Order> findAllOrders();

}
