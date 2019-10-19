package com.silviomoser.mhz.repository;

import com.silviomoser.mhz.data.Person;
import com.silviomoser.mhz.data.ShopTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by silvio on 19.10.18.
 */
public interface ShopTransactionRepository extends JpaRepository<ShopTransaction, Long> {

    List<ShopTransaction> findByPerson(Person person);
}
