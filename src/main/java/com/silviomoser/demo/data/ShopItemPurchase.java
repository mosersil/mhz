package com.silviomoser.demo.data;


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

    @ManyToOne
    @JoinColumn(name = "SHOP_ITEM_ID")
    private ShopItem item;

    @Column(name = "AMOUNT")
    private int amount;

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
}
