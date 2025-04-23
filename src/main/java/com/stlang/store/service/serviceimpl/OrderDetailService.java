package com.stlang.store.service.serviceimpl;

import com.stlang.store.dao.OrderDetailDAO;
import com.stlang.store.domain.OrderDetail;
import com.stlang.store.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailService implements IOrderDetailService {

    @Autowired
    OrderDetailDAO orderDetailDAO;

    @Override
    public OrderDetail createOrderDetail(OrderDetail orderDetail) {
        return orderDetailDAO.save(orderDetail);
    }
}
