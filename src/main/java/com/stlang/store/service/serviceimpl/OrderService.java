package com.stlang.store.service.serviceimpl;

import com.stlang.store.dao.OrderDAO;
import com.stlang.store.domain.Account;
import com.stlang.store.domain.Order;
import com.stlang.store.domain.OrderDetail;
import com.stlang.store.domain.Product;
import com.stlang.store.dto.OrderDTO;
import com.stlang.store.service.IAccountService;
import com.stlang.store.service.IOrderDetailService;
import com.stlang.store.service.IOrderService;
import com.stlang.store.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements IOrderService {

    @Autowired
    OrderDAO orderDAO;

    @Autowired
    IAccountService accountService;

    @Autowired
    IProductService productService;

    @Autowired
    IOrderDetailService orderDetailService;

    @Override
    public Order createOrder(OrderDTO orderDTO) {

        Account existingAccount = accountService.findById(orderDTO.getUsername());

        Order order = new Order();
        order.setAccount(existingAccount);
        order.setAddress(orderDTO.getAddress());
        order.setFullname(orderDTO.getFullname());
        order.setPhone(orderDTO.getPhone());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setTotalPrice(orderDTO.getTotalPrice());

        Order saveOrder = orderDAO.save(order);
        orderDTO.getDetails().stream().forEach(detail -> {
            Product existingProduct = productService.findById(detail.getProductId());
            if(existingProduct != null) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setName(detail.getProductName());
                orderDetail.setQuantity(detail.getQuantity());
                orderDetail.setPrice(detail.getQuantity() * existingProduct.getPrice());
                orderDetail.setProduct(existingProduct);
                orderDetail.setOrder(saveOrder);
                orderDetailService.createOrderDetail(orderDetail);
            }
        });
        return order;
    }
}
