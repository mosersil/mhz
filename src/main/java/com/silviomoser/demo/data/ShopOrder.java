package com.silviomoser.demo.data;

import com.silviomoser.demo.data.type.ShopOrderStatusType;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by silvio on 14.10.18.
 */
@Entity
@Table(name = "SHOPORDER")
public class ShopOrder extends AbstractEntity implements Comparable<ShopOrder> {

    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    private Person person;

    @JoinTable(name = "SHOPORDER_SHOPITEM", joinColumns = {
            @JoinColumn(name = "SHOPORDER_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
            @JoinColumn(name = "SHOPITEM_ID", referencedColumnName = "ID")})
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ShopItem> items;

    @Column(name = "DATE")
    private LocalDateTime date = LocalDateTime.now();

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

    public Set<ShopItem> getItems() {
        return items;
    }

    public void setItems(Set<ShopItem> items) {
        this.items = items;
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
    public int compareTo(ShopOrder o) {
        return this.getDate().compareTo(o.getDate());
    }

}
