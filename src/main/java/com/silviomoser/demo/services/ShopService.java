package com.silviomoser.demo.services;

import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.ShopItem;
import com.silviomoser.demo.data.ShopItemPurchase;
import com.silviomoser.demo.data.ShopOrderSubmission;
import com.silviomoser.demo.data.ShopTransaction;
import com.silviomoser.demo.data.type.ShopOrderStatusType;
import com.silviomoser.demo.repository.ShopItemRepository;
import com.silviomoser.demo.repository.ShopTransactionRepository;
import com.silviomoser.demo.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ShopService {

    @Autowired
    private ShopTransactionRepository shopTransactionRepository;

    @Autowired
    private ShopItemRepository shopItemRepository;

    public List<ShopItem> getOffering() {
        return shopItemRepository.findAll();
    }

    public List<ShopTransaction> getMyTransactions(ShopOrderStatusType... statusFilter) throws ServiceException {


        final Person me = SecurityUtils.getMe();
        if (me == null) {
            throw new ServiceException("Action requires an authenticated user");
        }

        log.debug(String.format("list transactions for %s", me));
        final List<ShopTransaction> allMyTransactions = shopTransactionRepository.findByPerson(SecurityUtils.getMe());

        List<ShopTransaction> filteredList;
        if (statusFilter == null || statusFilter.length == 0) {
            filteredList = allMyTransactions;
        } else {
            filteredList = allMyTransactions.stream().filter(it -> Arrays.asList(statusFilter).contains(it.getStatus())).collect(Collectors.toList());
        }
        log.debug("returning " + filteredList);
        return filteredList;
    }

    public ShopTransaction placeOrder(ShopOrderSubmission shopOrderSubmission[]) throws ServiceException {

        if (shopOrderSubmission==null || shopOrderSubmission.length==0) {
            throw new ServiceException("invalid order, must not be null");
        }

        final ShopTransaction shopTransaction = new ShopTransaction();
        final HashSet<ShopItemPurchase> items = new HashSet<>(shopOrderSubmission.length);
        Arrays.stream(shopOrderSubmission).forEach(it -> {
            final ShopItemPurchase shopItemPurchase = new ShopItemPurchase();
            shopItemPurchase.setTransaction(shopTransaction);
            shopItemPurchase.setItem(shopItemRepository.getOne(it.getShopItemId()));
            shopItemPurchase.setAmount(it.getCount());
            items.add(shopItemPurchase);
        });
        shopTransaction.setShopItemPurchases(items);
        if (SecurityUtils.getMe() != null) {
            shopTransaction.setPerson(SecurityUtils.getMe());
        }
        final ShopTransaction item = shopTransactionRepository.save(shopTransaction);
        return item;
    }

}
