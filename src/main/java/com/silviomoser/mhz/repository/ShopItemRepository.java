package com.silviomoser.mhz.repository;

import com.silviomoser.mhz.data.ShopItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by silvio on 14.10.18.
 */
public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {
}
