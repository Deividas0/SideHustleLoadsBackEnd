package com.example.demo.Controller;

import com.example.demo.Emails.EmailsService;
import com.example.demo.Service.UserService;
import com.example.demo.Utility.JwtToken.JwtDecoder;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"}, allowCredentials = "true")
public class PaymentController {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailsService emailsService;

    @Autowired
    private JwtDecoder jwtDecoder;

    @PostMapping("/create-stripe-session")
    public ResponseEntity<Map<String, String>> createStripeSession(
            @RequestBody Map<String, String> request,
            @RequestHeader("Authorization") String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }

        int userId = jwtDecoder.decodeUserIdFromToken(authorizationHeader);
        String token = authorizationHeader.substring(7);
        Stripe.apiKey = stripeSecretKey;

        try {
            String amount = request.get("amount");

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:5500/success.html?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl("http://localhost:5500/cancel.html")
                    .putMetadata("Token", authorizationHeader)
                    .putMetadata("userId", String.valueOf(userId))
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("eur")
                                                    .setUnitAmount(Long.parseLong(amount) * 100)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Account Top-Up")
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();


            Session session = Session.create(params);
            return ResponseEntity.ok(Map.of("sessionId", session.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create Stripe session"));
        }
    }


    @PostMapping("/update-balance")
    public ResponseEntity<Map<String, Object>> updateBalance(
            @RequestBody Map<String, String> request) {



        String sessionId = request.get("sessionId");

        try {
            Stripe.apiKey = stripeSecretKey;
            Session session = Session.retrieve(sessionId);
            String Token = session.getMetadata().get("jwt");

            if (session.getMetadata().get("userId") == null) {
                throw new IllegalArgumentException("User ID is missing in the Stripe session metadata.");
            }
            int id = Integer.parseInt(session.getMetadata().get("userId"));

            int amount = (int) (session.getAmountTotal() / 100);

            userService.updateUserBalanceById(id, amount);

            String recipientEmail = userService.getUserEmailById(id);
            String username = userService.getUserUsernameById(id);
            emailsService.successfulTopUpEmail(recipientEmail, username, amount);

            int newBalance = userService.getUserBalanceById(id);
            return ResponseEntity.ok(Map.of("newBalance", newBalance));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
