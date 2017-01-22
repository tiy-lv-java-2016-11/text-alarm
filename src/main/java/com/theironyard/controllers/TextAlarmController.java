package com.theironyard.controllers;

import com.theironyard.entities.User;
import com.theironyard.repositories.UserRepository;
import com.theironyard.services.StripeService;
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

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String getHome(HttpSession session){
        User user = userRepo.findFirstByUsername((String) session.getAttribute(UserController.SESSION_USERNAME));
        if (user == null){
            return "index";
        }
        if (user.getChargeId() == null){
            return "redirect:/payment";
        }
        else {
            return "redirect:/alarm";
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

    @RequestMapping(path = "/payment", method =RequestMethod.POST)
    public String submitPayment(HttpSession session, String token, RedirectAttributes redAtt){
        User user = userRepo.findFirstByUsername((String) session.getAttribute(UserController.SESSION_USERNAME));
        if (user == null){
            return "index";
        }

        String chargeId = stripe.chargeCard(token, 1999);
        user.setChargeId(chargeId);
        String message = "Payment Successful";
        redAtt.addFlashAttribute("message", message);
        userRepo.save(user);

        return "redirect:/alarm";
    }

    @RequestMapping(path = "/alarm", method = RequestMethod.POST)
    public String setAlarm(){

        return "redirect:/alarm";
    }
}
