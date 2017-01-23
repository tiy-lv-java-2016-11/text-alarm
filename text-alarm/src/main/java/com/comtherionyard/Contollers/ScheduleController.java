package com.comtherionyard.Contollers;

import com.comtherionyard.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * Created by darionmoore on 1/22/17.
 */
@Controller
public class ScheduleController {
    @Autowired
    UserRepository userRepository;

    @RequestMapping(path = "/spam-schedule", method = RequestMethod.GET)
    public String getSchedule(HttpSession session){
        return "spam-schedule";
    }




}
