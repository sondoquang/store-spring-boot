package com.stlang.store.dao;

import com.stlang.store.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDAO extends JpaRepository<Order, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM ORDERS WHERE USERNAME = ?1")
    List<Order> findByUsername(String username);
}
