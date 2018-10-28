package com.silviomoser.demo.data;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.data.type.ShopOrderStatusType;

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
@Table(name = "SHOP_TRANSACTION")
public class ShopTransaction extends AbstractEntity implements Comparable<ShopTransaction> {

    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    private Person person;

    /*
    @JsonView(Views.Public.class)
    @JoinTable(name = "SHOP_TRANSACTION_ITEM", joinColumns = {
            @JoinColumn(name = "SHOP_TRANSACTION_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
            @JoinColumn(name = "SHOP_ITEM_ID", referencedColumnName = "ID")})
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ShopItem> items;
    */

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


    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Set<ShopItemPurchase> getShopItemPurchases() {
        return shopItemPurchases;
    }

    public void setShopItemPurchases(Set<ShopItemPurchase> shopItemPurchases) {
        this.shopItemPurchases = shopItemPurchases;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public ShopOrderStatusType getStatus() {
        return status;
    }

    public void setStatus(ShopOrderStatusType status) {
        this.status = status;
    }

    public int getPaymentResponse() {
        return paymentResponse;
    }

    public void setPaymentResponse(int paymentResponse) {
        this.paymentResponse = paymentResponse;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public int compareTo(ShopTransaction o) {
        return this.getDate().compareTo(o.getDate());
    }

}
