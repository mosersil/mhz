package com.silviomoser.demo.repository;

import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.ShopOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by silvio on 19.10.18.
 */
public interface ShopOrderRepository extends JpaRepository<ShopOrder, Long> {

    List<ShopOrder> findByPerson(Person person);
}
