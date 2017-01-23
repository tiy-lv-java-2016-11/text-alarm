package com.comtherionyard.Contollers;

import com.comtherionyard.CommandObjects.UserCommand;
import com.comtherionyard.Entities.User;
import com.comtherionyard.Repositories.UserRepository;
import com.comtherionyard.Utilities.PassHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * Created by darionmoore on 1/20/17.
 */
@Controller
public class UserController {
    public static final String CURRENT_USER = "username";

    @Autowired
    UserRepository userRepository;

    /**
     * maps my home.html to "/" and gets the page
     */
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String getHome(HttpSession session, Model model){
        return "home";

    }

    /**
     * maps registration.html and gets the page
     */
    @RequestMapping(path = "/registration", method = RequestMethod.GET)
    public String getRegistration(HttpSession session){
        return "registration";
    }

    /**
     * registers and saves the registration information
     * then sends the user to the login page to verify
     * that the information was saved and the user can login
     * If someone tries to login and there is no record of
     * the user in the database then it redirects them to
     * the registration page
     */
    @RequestMapping(path = "/registration", method = RequestMethod.POST)
    public String getRegistration(HttpSession session, UserCommand command) throws PassHash.CannotPerformOperationException, PassHash.InvalidHashException {
        User newUser = userRepository.findByUsername(command.getUsername());

        if(newUser == null){
            newUser = new User(command.getName(), command.getEmail(), command.getPhone(), command.getUsername(), PassHash.createHash(command.getPassword()));
            userRepository.save(newUser);
        } else if(!PassHash.verifyPassword(command.getPassword(), newUser.getPassword())){
            return "redirect:/registration";
        }
        session.setAttribute(CURRENT_USER, newUser.getUsername());
        return "redirect:/login";

    }

    /**
     * gets the login page
     */
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLogin(HttpSession session){
        return "login";
    }

    /**
     *Allows a user to login if they have already registered
     * if the users name and password don't match what is in
     * the database then they are redirected to registration
     * if the information does match, the user is redirected
     * to the text purchase page
     */
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(HttpSession session, UserCommand command) throws PassHash.InvalidHashException, PassHash.CannotPerformOperationException {
        User newUser = userRepository.findByUsername(command.getUsername());

        if(newUser == null){
            newUser = new User(command.getUsername(), PassHash.createHash(command.getPassword()));
        } else if(!PassHash.verifyPassword(command.getPassword(), newUser.getPassword())){
            return "redirect:/registration";
        }
        session.setAttribute(CURRENT_USER, newUser.getUsername());
        return "redirect:/text-purchase";
    }

}
