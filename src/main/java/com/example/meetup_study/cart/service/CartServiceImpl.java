package com.example.meetup_study.cart.service;

import com.example.meetup_study.cart.domain.dao.CartDao;
import com.example.meetup_study.cart.domain.dto.CartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartDao cartDao;

    @Override
    public void createCart(Long userId, CartDto cartDto) {
        cartDao.createCart(userId.toString(), cartDto);
    }

    @Override
    public List<Object> getCartList(Long userId) {
        return cartDao.getCartList(userId.toString());
    }

    @Override
    public void removeCart(Long userId) {
        cartDao.removeCart(userId.toString());
    }
}
