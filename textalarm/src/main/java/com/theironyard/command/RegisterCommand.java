package com.theironyard.command;

import javax.validation.constraints.Size;

public class RegisterCommand {
    @Size(max = 30)
    private String name;
    @Size(max = 50)
    private String email;
    @Size(min = 6, max = 12)
    private String number;
    @Size(max = 30)
    private String username;
    @Size(max = 30)
    private String password;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

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
