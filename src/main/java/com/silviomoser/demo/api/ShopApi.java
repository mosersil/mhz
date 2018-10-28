package com.silviomoser.demo.api;

import com.paymill.context.PaymillContext;
import com.paymill.models.Transaction;
import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.ShopItem;
import com.silviomoser.demo.data.ShopOrder;
import com.silviomoser.demo.data.builder.PersonBuilder;
import com.silviomoser.demo.data.type.RoleType;
import com.silviomoser.demo.data.type.ShopOrderStatusType;
import com.silviomoser.demo.repository.PersonRepository;
import com.silviomoser.demo.repository.ShopItemRepository;
import com.silviomoser.demo.repository.ShopOrderRepository;
import com.silviomoser.demo.security.utils.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    ShopOrderRepository shopOrderRepository;

    @Autowired
    PaymillContext paymillContext;




    @ApiOperation(value = "List my purchases")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = ShopItem.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(value = "/public/api/shoptransactions", method = RequestMethod.GET)
    public List<ShopOrder> listOrders() {
        return shopOrderRepository.findByPerson(SecurityUtils.getMy());
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
        ShopOrder shopOrder = new ShopOrder();
        HashSet<ShopItem> items = new HashSet<>(order.length);
        Arrays.stream(order).forEach(it -> {
            items.add(shopItemRepository.getOne(it.getShopItemId()));
        });
        shopOrder.setItems(items);
        if (SecurityUtils.getMy()!=null) {
            shopOrder.setPerson(SecurityUtils.getMy());
        }
        ShopOrder item = shopOrderRepository.save(shopOrder);
        return item.getId();

    }




    @RequestMapping(value = "/internal/shop/authorizeOrder", method = RequestMethod.POST)
    public long shopClient(@RequestBody ClientDataSubmission clientDataSubmission) {

        Optional<ShopOrder> order = shopOrderRepository.findById(clientDataSubmission.getId());

        Person me = SecurityUtils.getMy();

        if (order.isPresent() && me!=null) {
            ShopOrder shopOrder = order.get();
            shopOrder.setPerson(me);
            shopOrder.setStatus(ShopOrderStatusType.READY_FOR_PAYMENT);
            return shopOrderRepository.save(shopOrder).getId();
        }
        return 0;
    }

    @RequestMapping(value = "/public/api/createpayment", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    public void createPayPalPayment(HttpServletRequest request, HttpServletResponse response, CreatePaymentSubmission createPaymentSubmission) throws IOException {

        Transaction transaction = paymillContext.getTransactionService().createWithToken(createPaymentSubmission.getToken(), createPaymentSubmission.getAmount(), createPaymentSubmission.getCurrency(), createPaymentSubmission.getDescription());


        transaction.getResponseCode();

        ShopOrder shopOrder = shopOrderRepository.getOne(Long.parseLong(createPaymentSubmission.getDescription()));

        shopOrder.setPaymentResponse(transaction.getResponseCode());
        shopOrder.setPaymentStatus(transaction.getStatus().getValue());
        shopOrder.setPaymentId(transaction.getId());

        if (transaction.getResponseCode()==20000) {
            shopOrder.setStatus(ShopOrderStatusType.PAYED);
        }

        shopOrderRepository.save(shopOrder);
        response.sendRedirect("/#!/shop-transactions");
    }



    public static class CreatePaymentSubmission {
        public CreatePaymentSubmission() {}

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

        public OrderSubmission() {}

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

        public ClientDataSubmission() {}

        private long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

    }
}
