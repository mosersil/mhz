package com.silviomoser.demo.data;


import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "SHOP_ITEM_PURCHASE")
public class ShopItemPurchase extends AbstractEntity {

    @JsonView(Views.Public.class)
    @ManyToOne
    @JoinColumn(name = "SHOP_ITEM_ID")
    private ShopItem item;

    @JsonView(Views.Public.class)
    @Column(name = "AMOUNT")
    private int amount;

    @ManyToOne
    @JoinColumn(name = "SHOP_TRANSACTION_ID")
    private ShopTransaction transaction;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ShopItem getItem() {
        return item;
    }

    public void setItem(ShopItem item) {
        this.item = item;
    }

    public ShopTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(ShopTransaction transaction) {
        this.transaction = transaction;
    }
}
