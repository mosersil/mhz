package com.silviomoser.demo.api.shop;

import com.fasterxml.jackson.annotation.JsonView;
import com.itextpdf.text.DocumentException;
import com.paymill.context.PaymillContext;
import com.silviomoser.demo.api.core.ApiException;
import com.silviomoser.demo.data.*;
import com.silviomoser.demo.data.type.ShopOrderStatusType;
import com.silviomoser.demo.repository.ShopItemPurchaseRepository;
import com.silviomoser.demo.repository.ShopItemRepository;
import com.silviomoser.demo.repository.ShopTransactionRepository;
import com.silviomoser.demo.security.utils.SecurityUtils;
import com.silviomoser.demo.utils.FormatUtils;
import com.silviomoser.demo.utils.PdfBuilder;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by silvio on 14.10.18.
 */
@RestController
@Slf4j
public class ShopApi {

    @Autowired
    ShopItemRepository shopItemRepository;

    @Autowired
    ShopItemPurchaseRepository shopItemPurchaseRepository;

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
        return shopTransactionRepository.findByPerson(SecurityUtils.getMe());
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
        if (SecurityUtils.getMe() != null) {
            shopTransaction.setPerson(SecurityUtils.getMe());
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

        final Person me = SecurityUtils.getMe();

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
        if (shopTransaction.getPerson().equals(SecurityUtils.getMe())) {
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

    @RequestMapping(value = "/api/protected/shop/createpayment", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    public void createPayPalPayment(HttpServletRequest request, HttpServletResponse response, CreatePaymentDataSubmission createPaymentDataSubmission) throws IOException {

        Stripe.apiKey = "";


        final long transactionId = createPaymentDataSubmission.getTransactionId();

        final Optional<ShopTransaction> optionalShopTransaction = shopTransactionRepository.findById(transactionId);

        if (optionalShopTransaction.isPresent()) {
            final ShopTransaction shopTransaction = optionalShopTransaction.get();
            final Map<String, Object> params = new HashMap<>();

            params.put("amount", ShopHelper.calculateTotal(shopTransaction));
            params.put("currency", "chf");
            params.put("description", String.format("transaction %s for client %s", shopTransaction.getId(), FormatUtils.toFirstLastName(shopTransaction.getPerson())));
            params.put("source", createPaymentDataSubmission.getToken());
            try {
                Charge charge = Charge.create(params);
                shopTransaction.setPaymentResponse(charge.getLastResponse().code());
                shopTransaction.setPaymentStatus(charge.getStatus());
                shopTransaction.setPaymentId(charge.getId());
                shopTransaction.setStatus(ShopOrderStatusType.PAYED);
                shopTransactionRepository.save(shopTransaction);
                response.sendRedirect("/#!/shop-transactions");
            } catch (StripeException se) {
                throw new ApiException(se.getMessage());
            }
        } else {
            throw new ApiException("No valid transaction found");
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
