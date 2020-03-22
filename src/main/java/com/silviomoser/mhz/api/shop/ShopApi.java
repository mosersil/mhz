package com.silviomoser.mhz.api.shop;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.mhz.api.core.ApiException;
import com.silviomoser.mhz.config.PaymentConfiguration;
import com.silviomoser.mhz.data.Person;
import com.silviomoser.mhz.data.ShopItem;
import com.silviomoser.mhz.data.ShopOrderSubmission;
import com.silviomoser.mhz.data.ShopTransaction;
import com.silviomoser.mhz.data.Views;
import com.silviomoser.mhz.data.type.ShopOrderStatusType;
import com.silviomoser.mhz.data.type.ShopPaymentType;
import com.silviomoser.mhz.repository.ShopItemPurchaseRepository;
import com.silviomoser.mhz.repository.ShopItemRepository;
import com.silviomoser.mhz.repository.ShopTransactionRepository;
import com.silviomoser.mhz.security.utils.SecurityUtils;
import com.silviomoser.mhz.services.ServiceException;
import com.silviomoser.mhz.services.ShopService;
import com.silviomoser.mhz.ui.i18.I18Helper;
import com.silviomoser.mhz.utils.FormatUtils;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by silvio on 14.10.18.
 */
@RestController
@Slf4j
public class ShopApi {

    @Autowired
    I18Helper i18Helper;

    @Autowired
    ShopItemRepository shopItemRepository;

    @Autowired
    ShopItemPurchaseRepository shopItemPurchaseRepository;

    @Autowired
    ShopTransactionRepository shopTransactionRepository;

    @Autowired
    PaymentConfiguration paymentConfiguration;

    @Autowired
    private ShopService shopService;


    @ApiOperation(value = "List my purchases")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = ShopTransaction.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @JsonView(Views.Public.class)
    @RequestMapping(value = "/api/protected/shop/transactions", method = RequestMethod.GET)
    public List<ShopTransaction> listTransactions() {
        try {
            return shopService.getMyTransactions(ShopOrderStatusType.PAYED, ShopOrderStatusType.AWAITING_PAYMENT);
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            throw new ApiException(i18Helper.getMessage(i18Helper.getMessage("generic_techerror")), HttpStatus.BAD_REQUEST);
        }
    }


    @ApiOperation(value = "List available shop items")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = ShopItem.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(value = "/api/public/shop/offering", method = RequestMethod.GET)
    public List<ShopItem> listItems() {
        log.debug("Offering API called");
        return shopService.getOffering();
    }

    @ApiOperation(value = "Submit shopping cart, place order to initialize payment")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Long.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(value = "/api/public/shop/submitorder", method = RequestMethod.POST)
    public long init(@RequestBody ShopOrderSubmission[] order) {
        try {
            final ShopTransaction item = shopService.placeOrder(order);
            return item.getId();
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            throw new ApiException(i18Helper.getMessage(i18Helper.getMessage("generic_techerror")), HttpStatus.BAD_REQUEST);
        }
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

    @RequestMapping(value = "/api/protected/shop/transaction", method = RequestMethod.GET)
    public ShopTransaction getTransaction(@RequestParam(name = "id", required = true) long id) {
        Optional<ShopTransaction> optionalShopTransaction = shopTransactionRepository.findById(id);
        if (!optionalShopTransaction.isPresent()) {
            throw new ApiException("no transaction found", HttpStatus.NOT_FOUND);
        }
        return optionalShopTransaction.get();
    }


    @RequestMapping(value = "/api/protected/shop/receipt", method = RequestMethod.GET)
    public ModelAndView getReceipt(@RequestParam(name = "id") long id) {
        final Optional<ShopTransaction> optionalShopTransaction = shopTransactionRepository.findById(id);
        if (!optionalShopTransaction.isPresent()) {
            log.warn(String.format("Could not find shop transaction with ID '%s'", id));
            throw new ApiException(String.format("Could not find shop transaction with ID '%s'", id));
        }
        final ShopTransaction shopTransaction = optionalShopTransaction.get();

        if (shopTransaction.getPerson().equals(SecurityUtils.getMe())) {
            try {
                log.debug("Assemble document {} in format {}", id, "PDF");
                return new ModelAndView("PDF", "transaction", shopTransaction);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new ApiException(e.getMessage());
            }
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

    @RequestMapping(value = "/api/protected/shop/createadvancepayment", method = RequestMethod.POST)
    public void createAdvancePayment(@RequestBody CreateAdvancePaymentDataSubmission createAdvancePaymentDataSubmission) {
        final long transactionId = createAdvancePaymentDataSubmission.getTransactionId();

        final Optional<ShopTransaction> optionalShopTransaction = shopTransactionRepository.findById(transactionId);
        if (optionalShopTransaction.isPresent()) {
            final ShopTransaction shopTransaction = optionalShopTransaction.get();
            shopTransaction.setPayment(ShopPaymentType.ADVANCE);
            shopTransaction.setStatus(ShopOrderStatusType.AWAITING_PAYMENT);
            shopTransactionRepository.save(shopTransaction);
        } else {
            log.warn("No valid transaction found for id '{}'", createAdvancePaymentDataSubmission.getTransactionId());
            throw new ApiException("No valid transaction found");
        }

    }

    @RequestMapping(value = "/api/protected/shop/createpayment", method = RequestMethod.POST)
    public void createCreditCardPayment(@RequestBody CreatePaymentDataSubmission createPaymentDataSubmission) {
        log.info("start creating payment for transaction '{}', cardholder '{}'", createPaymentDataSubmission.getTransactionId(), createPaymentDataSubmission.getCardholder_name());
        Stripe.apiKey = paymentConfiguration.getPrivateKey();


        final long transactionId = createPaymentDataSubmission.getTransactionId();

        final Optional<ShopTransaction> optionalShopTransaction = shopTransactionRepository.findById(transactionId);

        if (optionalShopTransaction.isPresent()) {
            final ShopTransaction shopTransaction = optionalShopTransaction.get();
            final Map<String, Object> params = new HashMap<>();

            params.put("amount", ShopHelper.calculateTotal(shopTransaction));
            params.put("currency", "chf");
            //params.put("name", createPaymentDataSubmission.getCardholder_name());
            params.put("description", String.format("transaction %s for client %s", shopTransaction.getId(), FormatUtils.toFirstLastName(shopTransaction.getPerson())));
            params.put("source", createPaymentDataSubmission.getToken());
            try {
                Charge charge = Charge.create(params);
                shopTransaction.setPaymentResponse(charge.getLastResponse().code());
                shopTransaction.setPaymentStatus(charge.getStatus());
                shopTransaction.setPaymentId(charge.getId());
                shopTransaction.setPayment(ShopPaymentType.CREDITCARD);
                shopTransaction.setStatus(ShopOrderStatusType.PAYED);
                shopTransactionRepository.save(shopTransaction);
                //response.sendRedirect("/#!/shop-transactions");
            } catch (StripeException se) {
                log.error("Unexpected Stripe exception: " + se.getMessage(), se);
                throw new ApiException(se.getMessage());
            }
        } else {
            log.warn("No valid transaction found for id '{}'", createPaymentDataSubmission.getTransactionId());
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
