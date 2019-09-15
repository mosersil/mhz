package com.silviomoser.mhz.data;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.mhz.data.type.ShopOrderStatusType;
import com.silviomoser.mhz.data.type.ShopPaymentType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
    @ToString.Exclude
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

    @JsonView(Views.Public.class)
    @Column(name = "PAYMENT")
    private ShopPaymentType payment;

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
