package com.silviomoser.demo.data;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.data.type.ShopOrderStatusType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by silvio on 14.10.18.
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "SHOP_TRANSACTION")
public class ShopTransaction extends AbstractEntity implements Comparable<ShopTransaction> {

    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    private Person person;

    @JsonView(Views.Public.class)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "transaction")
    private Set<ShopItemPurchase> shopItemPurchases;

    @JsonView(Views.Public.class)
    @Column(name = "DATE")
    private LocalDateTime date = LocalDateTime.now();

    @JsonView(Views.Public.class)
    @Column(name = "STATUS")
    private ShopOrderStatusType status = ShopOrderStatusType.INITIATED;

    @Column(name = "PAYMENT_ID")
    private String paymentId;

    @Column(name = "PAYMENT_RESPONSE")
    private int paymentResponse;

    @Column(name = "PAYMENT_STATUS")
    private String paymentStatus;


    @Override
    public int compareTo(ShopTransaction o) {
        return this.getDate().compareTo(o.getDate());
    }

}
