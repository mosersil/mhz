package com.silviomoser.demo.data;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "SHOP_TRANSACTION_ID")
    private ShopTransaction transaction;


}
