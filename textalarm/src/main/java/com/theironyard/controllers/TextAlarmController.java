package com.theironyard.controllers;

import com.theironyard.entities.User;
import com.theironyard.repositories.UserRepository;
import com.theironyard.services.StripeService;
import com.theironyard.services.TwilioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class TextAlarmController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    StripeService stripe;

    @Autowired
    TwilioService twilioService;

    /*

    gets the chargeid form stripe and saves the id to the user.
     */
    @RequestMapping(path = "/", method = RequestMethod.POST)
    public String pay(String stripeToken, HttpSession session){
        User sUser = userRepository.findFirstByUsername((String)session.getAttribute(UserController.SESSION_USERNAME));
        if(sUser == null){
            return "redirect:/login";
        }

        String chargeId = stripe.charge(stripeToken, 2000);
        sUser.setCustomerId(chargeId);
        userRepository.save(sUser);
        return "redirect:/schedule";
    }

    @RequestMapping(path = "/schedule",method = RequestMethod.GET)
    public String getSchedule(HttpSession session){
        User sUser = userRepository.findFirstByUsername((String)session.getAttribute(UserController.SESSION_USERNAME));
        if(sUser == null){
            return "home";
        }
        return "schedule";
    }
    /*
    expects int hour to set current users hour
     */
    @RequestMapping(path = "/schedule",method = RequestMethod.POST)
    public String setAlarm(HttpSession session, int hour){
        User sUser = userRepository.findFirstByUsername((String) session.getAttribute(UserController.SESSION_USERNAME));
        if(sUser == null){
            return "redirect:/";
        }
        sUser.setAlarmTime(hour);
        userRepository.save(sUser);
        return "redirect:/";
    }
}
