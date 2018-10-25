package com.example.jassyap.first_try;

public class users {
    private String name;
    private String gmail;
    private String password;
    private String age;
    private String faculty;

    public users(){

    }

    public users(String name, String gmail, String password, String age, String faculty) {
        this.name = name;
        this.gmail = gmail;
        this.password = password;
        this.age = age;
        this.faculty = faculty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
}
