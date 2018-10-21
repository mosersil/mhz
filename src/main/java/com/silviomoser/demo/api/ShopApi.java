package com.silviomoser.demo.api;

import com.silviomoser.demo.data.AbstractEntity;
import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.ShopItem;
import com.silviomoser.demo.data.ShopOrder;
import com.silviomoser.demo.repository.PersonRepository;
import com.silviomoser.demo.repository.ShopItemRepository;
import com.silviomoser.demo.repository.ShopOrderRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * Created by silvio on 14.10.18.
 */
@RestController
public class ShopApi {

    @Autowired
    ShopItemRepository shopItemRepository;

    @Autowired
    ShopOrderRepository shopOrderRepository;

    @Autowired
    PersonRepository personRepository;


    @CrossOrigin(origins = "*")
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
        ShopOrder item = shopOrderRepository.save(shopOrder);
        return item.getId();

    }

    @RequestMapping(value = "/public/api/shopclient", method = RequestMethod.POST)
    public long shopClient(@RequestBody ClientDataSubmission clientDataSubmission) {

        Optional<Person> client = personRepository.findByEmail(clientDataSubmission.getEmail());
        Optional<ShopOrder> order = shopOrderRepository.findById(clientDataSubmission.getId());

        if (order.isPresent() && client.isPresent()) {
            ShopOrder shopOrder = order.get();
            shopOrder.setPerson(client.get());
            return shopOrderRepository.save(shopOrder).getId();
        }
        return 0;
    }

    @RequestMapping(value = "/public/api/createpayment", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    public int createPayPalPayment(HttpServletRequest request, CreatePaymentSubmission createPaymentSubmission) {

        System.out.println("prepare payment " + createPaymentSubmission);
        return 200;
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
        private String email;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
