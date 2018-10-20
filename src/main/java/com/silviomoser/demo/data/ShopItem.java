package com.silviomoser.demo.data;

import com.silviomoser.demo.data.type.ShopItemType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by silvio on 14.10.18.
 */
@Entity
@Table(name = "SHOPITEM")
public class ShopItem extends AbstractEntity {

    @Column(name = "NAME", nullable = false, length = 30)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false, length = 200)
    private String description;
    @Column(name="PRICE")
    private float price;
    @Column(name = "TYPE")
    private ShopItemType shopItemType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public ShopItemType getShopItemType() {
        return shopItemType;
    }

    public void setShopItemType(ShopItemType shopItemType) {
        this.shopItemType = shopItemType;
    }


    @Override
    public String toString() {
        return "ShopItem{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", shopItemType=" + shopItemType +
                '}';
    }
}
