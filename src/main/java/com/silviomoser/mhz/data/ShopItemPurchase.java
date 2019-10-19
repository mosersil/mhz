package com.silviomoser.mhz.data;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@ToString
@Table(name = "SHOP_ITEM_PURCHASE")
public class ShopItemPurchase extends AbstractEntity {

    @JsonView(Views.Public.class)
    @ManyToOne
    @JoinColumn(name = "SHOP_ITEM_ID")
    private ShopItem item;

    @JsonView(Views.Public.class)
    @Column(name = "AMOUNT")
    private int amount;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "SHOP_TRANSACTION_ID")
    private ShopTransaction transaction;


}
