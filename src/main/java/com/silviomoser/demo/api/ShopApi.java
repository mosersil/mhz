package com.silviomoser.demo.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.paymill.context.PaymillContext;
import com.paymill.models.Transaction;
import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.ShopItemPurchase;
import com.silviomoser.demo.data.ShopItem;
import com.silviomoser.demo.data.ShopTransaction;
import com.silviomoser.demo.data.Views;
import com.silviomoser.demo.data.type.ShopOrderStatusType;
import com.silviomoser.demo.repository.ShopItemRepository;
import com.silviomoser.demo.repository.ShopTransactionRepository;
import com.silviomoser.demo.security.utils.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * Created by silvio on 14.10.18.
 */
@RestController
@Slf4j
public class ShopApi {

    @Autowired
    ShopItemRepository shopItemRepository;

    @Autowired
    ShopTransactionRepository shopTransactionRepository;

    @Autowired
    PaymillContext paymillContext;


    @ApiOperation(value = "List my purchases")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = ShopItem.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @JsonView(Views.Public.class)
    @RequestMapping(value = "/internal/shop/transactions", method = RequestMethod.GET)
    public List<ShopTransaction> listOrders() {
        return shopTransactionRepository.findByPerson(SecurityUtils.getMy());
    }


    @ApiOperation(value = "List available shop items")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = ShopItem.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(value = "/public/api/shopitems", method = RequestMethod.GET)
    public List<ShopItem> listItems() {

        List<ShopItem> all = shopItemRepository.findAll();

        return all;

    }


    @RequestMapping(value = "/public/api/shopinit", method = RequestMethod.POST)
    public long init(@RequestBody OrderSubmission[] order) {
        final ShopTransaction shopTransaction = new ShopTransaction();
        final HashSet<ShopItemPurchase> items = new HashSet<>(order.length);
        Arrays.stream(order).forEach(it -> {
            final ShopItemPurchase shopItemPurchase = new ShopItemPurchase();
            shopItemPurchase.setItem(shopItemRepository.getOne(it.getShopItemId()));
            shopItemPurchase.setAmount(it.getCount());
            items.add(shopItemPurchase);
        });
        shopTransaction.setShopItemPurchases(items);
        if (SecurityUtils.getMy() != null) {
            shopTransaction.setPerson(SecurityUtils.getMy());
        }
        final ShopTransaction item = shopTransactionRepository.save(shopTransaction);
        return item.getId();

    }


    @RequestMapping(value = "/internal/shop/authorizeOrder", method = RequestMethod.POST)
    public long shopClient(@RequestBody ClientDataSubmission clientDataSubmission) {

        Optional<ShopTransaction> order = shopTransactionRepository.findById(clientDataSubmission.getId());

        Person me = SecurityUtils.getMy();

        if (order.isPresent() && me != null) {
            ShopTransaction shopTransaction = order.get();
            shopTransaction.setPerson(me);
            shopTransaction.setStatus(ShopOrderStatusType.READY_FOR_PAYMENT);
            return shopTransactionRepository.save(shopTransaction).getId();
        }
        return 0;
    }

    @RequestMapping(value = "/public/api/createpayment", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    public void createPayPalPayment(HttpServletRequest request, HttpServletResponse response, CreatePaymentSubmission createPaymentSubmission) throws IOException {

        Transaction transaction = paymillContext.getTransactionService().createWithToken(createPaymentSubmission.getToken(), createPaymentSubmission.getAmount(), createPaymentSubmission.getCurrency(), createPaymentSubmission.getDescription());


        transaction.getResponseCode();

        ShopTransaction shopTransaction = shopTransactionRepository.getOne(Long.parseLong(createPaymentSubmission.getDescription()));

        shopTransaction.setPaymentResponse(transaction.getResponseCode());
        shopTransaction.setPaymentStatus(transaction.getStatus().getValue());
        shopTransaction.setPaymentId(transaction.getId());

        if (transaction.getResponseCode() == 20000) {
            shopTransaction.setStatus(ShopOrderStatusType.PAYED);
        }

        shopTransactionRepository.save(shopTransaction);
        response.sendRedirect("/#!/shop-transactions");
    }


    public static class CreatePaymentSubmission {
        public CreatePaymentSubmission() {
        }

        private int amount;
        private String currency;
        private String description;
        private String token;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        @Override
        public String toString() {
            return "CreatePaymentSubmission{" +
                    "amount=" + amount +
                    ", currency='" + currency + '\'' +
                    ", description='" + description + '\'' +
                    ", token='" + token + '\'' +
                    '}';
        }
    }

    /**
     * Created by silvio on 14.10.18.
     */
    public static class OrderSubmission {

        public OrderSubmission() {
        }

        private int count;
        private long shopItemId;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public long getShopItemId() {
            return shopItemId;
        }

        public void setShopItemId(long shopItemId) {
            this.shopItemId = shopItemId;
        }

        @Override
        public String toString() {
            return "OrderSubmission{" +
                    "count=" + count +
                    ", shopItemId=" + shopItemId +
                    '}';
        }
    }

    public static class ClientDataSubmission {

        public ClientDataSubmission() {
        }

        private long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

    }
}
