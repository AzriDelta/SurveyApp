package com.example.jassyap.first_try;

public class Sign_up_info {
    private String name;
    private String gmail;
    private String password;
    private String confirmPassword;

    public Sign_up_info(){

    }

    public Sign_up_info(String name, String gmail, String password, String confirmPassword) {
        this.name = name;
        this.gmail = gmail;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getName() {
        return name;
    }

    public String getGmail() {
        return gmail;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
