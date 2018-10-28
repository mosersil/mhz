package com.silviomoser.demo.repository;

import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.ShopTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by silvio on 19.10.18.
 */
public interface ShopTransactionRepository extends JpaRepository<ShopTransaction, Long> {

    List<ShopTransaction> findByPerson(Person person);
}
