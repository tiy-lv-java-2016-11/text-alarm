package com.theironyard.controllers;

import com.theironyard.commands.LoginCommand;
import com.theironyard.commands.RegisterCommand;
import com.theironyard.entities.User;
import com.theironyard.repositories.UserRepository;
import com.theironyard.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    public static final String SESSION_USERNAME = "currentUsername";

    @Autowired
    UserRepository userRepo;

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage(){
        return "login";
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage(){
        return "register";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String postLogin(LoginCommand command, HttpSession session, RedirectAttributes redAtt) throws PasswordStorage.InvalidHashException, PasswordStorage.CannotPerformOperationException {
        User user = userRepo.findFirstByUsername(command.getUsername());
        if (user == null || !PasswordStorage.verifyPassword(command.getPassword(), user.getPassword())){
            String message = "Invalid Username/Password";
            redAtt.addFlashAttribute("message", message);
            return "redirect:/login";
        }
        session.setAttribute(SESSION_USERNAME, user.getUsername());
        if (user.getChargeId() == null){
            return "redirect:/payment";
        }
        return "redirect:/alarm";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String createUser(RegisterCommand command, HttpSession session, RedirectAttributes redAtt) throws PasswordStorage.CannotPerformOperationException {
        User user = userRepo.findFirstByUsername(command.getUsername());
        if (user != null){
            String message = "That username is taken.";
            redAtt.addFlashAttribute("message", message);
            return "redirect:/register";
        }
        user = new User(command.getName(), command.getEmail(), command.getPhone(), command.getUsername(), PasswordStorage.createHash(command.getPassword()));
        userRepo.save(user);
        session.setAttribute(SESSION_USERNAME, user.getUsername());
        return "redirect:/payment";
    }


}
