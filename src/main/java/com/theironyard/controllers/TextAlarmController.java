package com.theironyard.controllers;

import com.theironyard.entities.User;
import com.theironyard.repositories.UserRepository;
import com.theironyard.services.StripeService;
import com.theironyard.services.TwilioService;
import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class TextAlarmController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    StripeService stripe;

    @Autowired
    TwilioService twilio;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String getHome(HttpSession session){
        User user = userRepo.findFirstByUsername((String) session.getAttribute(UserController.SESSION_USERNAME));
        if (user == null){
            return "index";
        }
        if (user.getChargeId() == null){
            return "payment";
        }
        else {
            return "alarm";
        }
    }

    @RequestMapping(path = "/payment", method = RequestMethod.GET)
    public String getPaymentForm(HttpSession session){
        User user = userRepo.findFirstByUsername((String) session.getAttribute(UserController.SESSION_USERNAME));
        if (user == null){
            return "index";
        }
        return "payment";
    }

    @RequestMapping(path = "/alarm", method = RequestMethod.GET)
    public String getAlarmForm(HttpSession session){
        User user = userRepo.findFirstByUsername((String) session.getAttribute(UserController.SESSION_USERNAME));
        if (user == null){
            return "index";
        }
        return "alarm";
    }

    /* *
     * Receives "token" generated Stripe Api, and submits a "charge" to Stripe,
     * if payment is successful, redirects to "alarm" page
     * */
    @RequestMapping(path = "/payment", method =RequestMethod.POST)
    public String submitPayment(HttpSession session, String stripeToken, RedirectAttributes redAtt){
        User user = userRepo.findFirstByUsername((String) session.getAttribute(UserController.SESSION_USERNAME));
        if (user == null){
            return "redirect:/index";
        }

        String chargeId = stripe.chargeCard(stripeToken, 1999);
        user.setChargeId(chargeId);
        String message = "Payment Successful";
        redAtt.addFlashAttribute("message", message);
        userRepo.save(user);

        return "redirect:/";
    }

    /* *
     * Receives "hour" from alarm form and sets the current users alarmTime to "hour"
     * */
    @RequestMapping(path = "/alarm", method = RequestMethod.POST)
    public String setAlarm(HttpSession session, int hour, RedirectAttributes redAtt){
        User user = userRepo.findFirstByUsername((String) session.getAttribute(UserController.SESSION_USERNAME));
        if (user == null){
            return "redirect:/index";
        }
        user.setAlarmTime(hour);
        userRepo.save(user);
        String message = String.format("Your alarm has been set for the %d00 hour", hour);
        redAtt.addFlashAttribute("message", message);
        return "redirect:/alarm";
    }
}
