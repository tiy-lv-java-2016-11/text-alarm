package com.comtherionyard.Contollers;

import com.comtherionyard.Entities.User;
import com.comtherionyard.Repositories.UserRepository;
import com.comtherionyard.Services.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * Created by darionmoore on 1/21/17.
 */
@Controller
public class PurchaseController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    StripeService stripe;

    /**
     * Maps and gets the text-purchase page
     */
    @RequestMapping(path = "/text-purchase", method = RequestMethod.GET)
    public String getPurchasePage(HttpSession session){
        return "text-purchase";
    }

    /**
     *generates a token for the users purchase
     * saves the token to a chargeId for later reference
     * saves the chargeId to the users object
     * redirects to schedule page
     */
    @RequestMapping(path = "/text-purchase", method = RequestMethod.POST)
    public String getToken(String stripeToken, User user){
        String chargeId = stripe.chargeCard(stripeToken, 2000);
        user.setUserChargeId(chargeId);
        return "redirect:/spam-schedule";

    }
}
