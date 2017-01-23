package com.theironyard.services;

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
    UserRepository userRepo;

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

    /* *
     * On every hour, retrieves all users with alarmTime set to the current hour,
     * loops through users and calls "sendSMS" with the user's phone number
     * */
    @Scheduled(cron = "* 00 * * * *")
    public void sendTextAlarms(){
        List<User> users = userRepo.findByAlarmTime(LocalDateTime.now().getHour());

        users.stream().forEach(user -> sendSMS("+1"+user.getPhone()));

    }

    /* *
     * Sends a SMS message to the input phone number using the Twilio library
     * */
    public String sendSMS(String phone){

        Message message = Message.creator(
                new PhoneNumber(phone),  // To number
                new PhoneNumber(fromPhone),  // From number
                "This is your Text Alarm!" // SMS body
        ).create();

        return message.getSid();
    }
}
