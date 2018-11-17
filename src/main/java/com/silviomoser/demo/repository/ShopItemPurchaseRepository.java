package com.silviomoser.demo.repository;

import com.silviomoser.demo.data.ShopItemPurchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopItemPurchaseRepository extends JpaRepository<ShopItemPurchase, Long> {
}
