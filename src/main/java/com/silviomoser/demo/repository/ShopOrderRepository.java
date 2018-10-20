package com.silviomoser.demo.repository;

import com.silviomoser.demo.data.ShopOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by silvio on 19.10.18.
 */
public interface ShopOrderRepository extends JpaRepository<ShopOrder, Long> {
}
