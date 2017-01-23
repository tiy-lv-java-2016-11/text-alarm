package com.theironyard.command;

import javax.validation.constraints.Size;


public class LoginCommand {
    @Size(max = 30)
    private String username;
    @Size(max = 30)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
