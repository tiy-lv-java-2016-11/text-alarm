package com.theironyard.controllers;

import com.theironyard.command.LoginCommand;
import com.theironyard.command.RegisterCommand;
import com.theironyard.entities.User;
import com.theironyard.repositories.UserRepository;
import com.theironyard.utillities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;


@Controller
public class UserController {

    public static final String SESSION_USERNAME = "username";

    @Autowired
    UserRepository userRepository;

    /*
    checks to see if the user exist and they have the
    right password/username.then logs him/her in.
     */
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(HttpSession session, LoginCommand command) throws PasswordStorage.InvalidHashException, PasswordStorage.CannotPerformOperationException {
        User sUser =userRepository.findFirstByUsername(command.getUsername());
        if(sUser == null){
            return "redirect:/login";
        }
        else if(!PasswordStorage.verifyPassword(command.getPassword(), sUser.getPassword())){
            return "redirect:/login";
        }

        session.setAttribute(SESSION_USERNAME, sUser.getUsername());
        return "redirect:/";
    }

    /*
    Logs user out
     */
    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    /*
    addes a new user if they don't have the same username as another.
     */
    @RequestMapping(path = "/registration", method = RequestMethod.POST)
    public String createUser(HttpSession session, RegisterCommand command) throws PasswordStorage.CannotPerformOperationException {
        User sUser = userRepository.findFirstByUsername(command.getUsername());
        if(sUser != null){
            return "redirect:/register";
        }
        sUser = new User(command.getName(), command.getEmail(), command.getNumber(), command.getUsername(),
                PasswordStorage.createHash(command.getPassword()));
        userRepository.save(sUser);
        session.setAttribute(SESSION_USERNAME, sUser.getUsername());
        return "redirect:/";
    }
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLogin(HttpSession session){
        if (session.getAttribute(SESSION_USERNAME) != null){
            return "redirect:/";
        }
        return "login";
    }
    @RequestMapping(path = "/registration", method = RequestMethod.GET)
    public String getReg(){
        return "registration";
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(HttpSession session, Model model){
        if(session.getAttribute(SESSION_USERNAME) == null){
            return "redirect:/login";
        }
        model.addAttribute("username", session.getAttribute(SESSION_USERNAME));
        return "home";
    }

}
