// package com.thebookcooper.config;

// import com.stripe.Stripe;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Configuration;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// import javax.annotation.PostConstruct;

// @Configuration
// public class StripeConfig {

//     private static final Logger logger = LoggerFactory.getLogger(StripeConfig.class);

//     @Value("${stripe.api.key}")
//     private String apiKey;

//     @PostConstruct
//     public void setup() {
//         if (apiKey == null) {
//             logger.error("Stripe API key is not set. Please check your environment variables.");
//             throw new IllegalArgumentException("Stripe API key is not set.");
//         } else {
//             Stripe.apiKey = apiKey;
//             logger.info("Stripe API key has been initialized successfully.");
//         }
//     }
// }
