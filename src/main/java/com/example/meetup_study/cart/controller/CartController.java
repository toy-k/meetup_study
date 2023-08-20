package com.example.meetup_study.cart.controller;

import com.example.meetup_study.cart.domain.dto.CartDto;
import com.example.meetup_study.cart.service.CartService;
import com.example.meetup_study.common.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final JwtService jwtService;

    private String ACCESSTOKEN = "AccessToken";

    @PostMapping
    public void createCart(@Valid @RequestBody CartDto cartDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        cartService.createCart(userIdOpt.get(), cartDto);
    }

    @GetMapping
    public List<Object> getCartList(HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        return cartService.getCartList(userIdOpt.get());
    }

    @DeleteMapping
    public void removeCart(HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        cartService.removeCart(userIdOpt.get());
    }
}
