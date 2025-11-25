package com.example.tonalli_backend.controller;

import com.example.tonalli_backend.dto.AvatarUpdateRequest;
import com.example.tonalli_backend.dto.ShopResponse;
import com.example.tonalli_backend.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @GetMapping
    public ResponseEntity<ShopResponse> getShop(Authentication auth) {
        return ResponseEntity.ok(shopService.getShopData(auth.getName()));
    }

    @PostMapping("/buy/{itemId}")
    public ResponseEntity<Void> buyItem(@PathVariable Integer itemId, Authentication auth) {
        shopService.buyItem(auth.getName(), itemId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/avatar")
    public ResponseEntity<Void> saveAvatar(@RequestBody AvatarUpdateRequest request, Authentication auth) {
        shopService.saveAvatarConfiguration(auth.getName(), request);
        return ResponseEntity.ok().build();
    }
}