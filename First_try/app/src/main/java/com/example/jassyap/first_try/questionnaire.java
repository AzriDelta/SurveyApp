package com.example.jassyap.first_try;

public class questionnaire {

    private String title;
    private String created_date;
    private String status;

    public questionnaire() {
    }

    public questionnaire(String title, String created_date, String status) {
        this.title = title;
        this.created_date = created_date;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
