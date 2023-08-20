package com.example.meetup_study.cart.service;

import com.example.meetup_study.cart.domain.dto.CartDto;

import java.util.List;

public interface CartService {

    List<Object> getCartList(Long userId);

    void createCart(Long userId, CartDto cartDto);

    void removeCart(Long userId);

}
