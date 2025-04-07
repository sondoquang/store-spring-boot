package com.stlang.store.dao;

import com.stlang.store.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDAO extends JpaRepository<Order, Integer> {
}
