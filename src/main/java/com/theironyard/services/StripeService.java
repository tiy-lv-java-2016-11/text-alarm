package com.theironyard.services;

import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    @Value("${stripe.key.secret}")
    private String key;

    @PostConstruct
    public void init(){
        Stripe.apiKey = key;
    }

    public String chargeCard(String token, int amount){
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", "usd");
        params.put("description", "Monthly Subscription to Text Alarm");
        params.put("source", token);

        String id = null;
        try {
            Charge charge = Charge.create(params);
            id = charge.getId();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (CardException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        }
        return id;
    }
}
