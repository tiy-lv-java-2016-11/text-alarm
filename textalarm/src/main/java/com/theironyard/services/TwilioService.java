package com.theironyard.services;

import com.stripe.Stripe;
import com.theironyard.entities.User;
import com.theironyard.repositories.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TwilioService {
    @Autowired
    UserRepository userRepository;

    @Value("${twilio.sid}")
    private String accountSid;
    @Value("${twilio.token}")
    private String authToken;
    @Value("${twilio.phone}")
    private String fromPhone;

    @PostConstruct
    public void init(){
        Twilio.init(accountSid, authToken);
    }

    /*
    send a text to a number you input.
     */
    public String sendSMS(String phone){
        Message message = Message.creator(
                new PhoneNumber(phone),
                new PhoneNumber(fromPhone),
                "hello form the other side"
        ).create();
        return message.getSid();
    }

    /*
    every hour sends text to all users with their alarm time on the hour
     */
    @Scheduled(cron = "0 0 * * * *")
    public void text(){
        List<User> users = userRepository.findByAlarmTime(LocalDateTime.now().getHour());
        users.stream().forEach(user -> sendSMS("+1"+user.getNumber()));
    }
}
