package com.stlang.store.service;

import com.stlang.store.domain.Order;
import com.stlang.store.dto.OrderDTO;

public interface IOrderService {

    Order createOrder(OrderDTO orderDTO);

}
