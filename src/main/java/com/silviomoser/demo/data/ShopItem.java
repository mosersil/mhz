package com.silviomoser.demo.data;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.data.type.ShopItemType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by silvio on 14.10.18.
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "SHOP_ITEM")
public class ShopItem extends AbstractEntity {

    @NotNull
    @JsonView(Views.Public.class)
    @Column(name = "NAME", nullable = false, length = 30)
    private String name;

    @NotNull
    @Column(name = "DESCRIPTION", nullable = false, length = 200)
    private String description;

    @Column(name="PRICE")
    private long price;

    @NotNull
    @Column(name = "TYPE")
    private ShopItemType shopItemType;

}
