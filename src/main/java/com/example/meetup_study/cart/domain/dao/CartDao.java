package com.example.meetup_study.cart.domain.dao;

import com.example.meetup_study.cart.domain.dto.CartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CartDao {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String cartKey = "cart::";

    public static String generateCartKey(String id) {
        return cartKey + id;
    }

    public List<Object> getCartList(String userId) {

        final String key = generateCartKey(userId);

        List<Object> cartList = redisTemplate
                .opsForList()
                .range(key, 0, -1);

        return cartList;

    }


    public void createCart(String userId, CartDto cart) {
        final String key = generateCartKey(userId);

        if (isRoomIdAlreadyInCart(key, cart.getRoomId())) {
            return;
        }

        redisTemplate.opsForList().rightPush(key, cart);
    }

    private boolean isRoomIdAlreadyInCart(String cartKey, Long roomId) {

        List<Object> carts = redisTemplate.opsForList().range(cartKey, 0, -1);
        for (Object cartObj : carts) {
            if (cartObj instanceof CartDto) {
                CartDto cart = (CartDto) cartObj;
                if (cart.getRoomId().equals(roomId)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void removeCart(String userId) {
        final String key = generateCartKey(userId);
        redisTemplate.delete(key);
    }

}
