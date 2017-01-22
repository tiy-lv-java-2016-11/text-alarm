package com.theironyard;

import com.theironyard.entities.User;
import com.theironyard.repositories.UserRepository;
import com.theironyard.utilities.PasswordStorage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTests {
    User user;
    String password;

    @Autowired
    WebApplicationContext wap;

    @Autowired
    UserRepository userRepo;

    MockMvc mockMvc;

    @Before
    public void before() throws PasswordStorage.CannotPerformOperationException {
        mockMvc = MockMvcBuilders.webAppContextSetup(wap).build();
        String username = "testUsername";
        password = "testPassword";
        String email = "test@email.com";
        String phone = "555555555";
        String name = "Test Name";
        user = new User(name, email, phone, username, PasswordStorage.createHash(password));
        userRepo.save(user);
    }

    @After
    public void after(){
        userRepo.delete(user);
    }

    @Test
    public void testLoginValidChargeNull() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .param("username", user.getUsername())
                        .param("password", password)
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection()
        ).andExpect(redirectedUrl("/payment"));

    }

    @Test
    public void testLoginValidChargeNotNull() throws Exception {
        user.setChargeId("something");
        userRepo.save(user);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .param("username", user.getUsername())
                        .param("password", password)
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection()
        ).andExpect(redirectedUrl("/alarm"));

    }

    @Test
    public void testLoginInvalidUsername() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .param("username", user.getUsername()+"2")
                        .param("password", password)
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection()
        ).andExpect(flash().attribute("message", "Invalid Username/Password")
        ).andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testLoginInvalidPassword() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .param("username", user.getUsername())
                        .param("password", password+"2")
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection()
        ).andExpect(flash().attribute("message", "Invalid Username/Password")
        ).andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testRegisterValid() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .param("username", user.getUsername()+"2")
                        .param("password", password)
                        .param("email", user.getEmail())
                        .param("phone", user.getPhone())
                        .param("name", user.getName())
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection()
        ).andExpect(redirectedUrl("/payment"));

        User savedUser = userRepo.findFirstByUsername(user.getUsername()+"2");
        assertNotNull("User object not saved correctly", savedUser);
        assertEquals("Username not saved correctly", user.getUsername()+"2", savedUser.getUsername());

    }

    @Test
    public void testRegisterUsernameExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .param("username", user.getUsername())
                        .param("password", password)
                        .param("email", user.getEmail())
                        .param("phone", user.getPhone())
                        .param("name", user.getName())
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection()
        ).andExpect(flash().attribute("message", "That username is taken.")
        ).andExpect(redirectedUrl("/register"));
    }
}
