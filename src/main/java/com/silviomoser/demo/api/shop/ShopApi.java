package com.silviomoser.demo.api.shop;

import com.fasterxml.jackson.annotation.JsonView;
import com.itextpdf.text.DocumentException;
import com.paymill.context.PaymillContext;
import com.paymill.models.Transaction;
import com.silviomoser.demo.api.core.ApiException;
import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.ShopItem;
import com.silviomoser.demo.data.ShopItemPurchase;
import com.silviomoser.demo.data.ShopTransaction;
import com.silviomoser.demo.data.Views;
import com.silviomoser.demo.data.type.ShopOrderStatusType;
import com.silviomoser.demo.repository.ShopItemRepository;
import com.silviomoser.demo.repository.ShopTransactionRepository;
import com.silviomoser.demo.security.utils.SecurityUtils;
import com.silviomoser.demo.utils.PdfBuilder;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
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
            @ApiResponse(code = 200, message = "Success", response = ShopTransaction.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @JsonView(Views.Public.class)
    @RequestMapping(value = "/api/protected/shop/transactions", method = RequestMethod.GET)
    public List<ShopTransaction> listTransactions() {
        return shopTransactionRepository.findByPerson(SecurityUtils.getMy());
    }


    @ApiOperation(value = "List available shop items")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = ShopItem.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(value = "/api/public/shop/offering", method = RequestMethod.GET)
    public List<ShopItem> listItems() {
        log.debug("Offering API called");
        return shopItemRepository.findAll();
    }

    @ApiOperation(value = "Submit shopping cart, place order to initialize payment")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Long.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(value = "/api/public/shop/submitorder", method = RequestMethod.POST)
    public long init(@RequestBody OrderSubmission[] order) {
        final ShopTransaction shopTransaction = new ShopTransaction();
        final HashSet<ShopItemPurchase> items = new HashSet<>(order.length);
        Arrays.stream(order).forEach(it -> {
            final ShopItemPurchase shopItemPurchase = new ShopItemPurchase();
            shopItemPurchase.setTransaction(shopTransaction);
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


    @RequestMapping(value = "/api/protected/shop/prepare_transaction", method = RequestMethod.POST)
    public long shopClient(@RequestBody ClientDataSubmission clientDataSubmission) {

        Optional<ShopTransaction> order = shopTransactionRepository.findById(clientDataSubmission.getId());

        if (!order.isPresent()) {
            throw new ApiException("could not find an order with specified ID");
        }

        final Person me = SecurityUtils.getMy();

        if (me == null) {
            throw new ApiException("unauthorized call. This should never happen...");
        }


        final ShopTransaction shopTransaction = order.get();
        shopTransaction.setPerson(me);
        shopTransaction.setStatus(ShopOrderStatusType.READY_FOR_PAYMENT);
        return shopTransactionRepository.save(shopTransaction).getId();
    }


    @RequestMapping(value = "/api/protected/shop/receipt", produces = MediaType.APPLICATION_PDF_VALUE, method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getAddressList(@RequestParam(name = "id", required = true) long id) throws DocumentException {
        ShopTransaction shopTransaction = shopTransactionRepository.findById(id).get();
        if (shopTransaction.getPerson().equals(SecurityUtils.getMy())) {
            ByteArrayInputStream bis = PdfBuilder.generateReceipt(shopTransaction);
            return pdfResponse(bis, "receipt");
        }
        throw new ApiException("Invalid request");

    }


    private ResponseEntity<InputStreamResource> pdfResponse(ByteArrayInputStream bis, String fileName) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("inline; filename=%s.pdf", fileName));

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @RequestMapping(value = "/api/public/createpayment", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
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
