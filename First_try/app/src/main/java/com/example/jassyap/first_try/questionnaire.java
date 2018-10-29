package com.example.jassyap.first_try;

public class questionnaire {
    private String questionnaireId;
    private String creator_name;
    private String title;
    private String created_date;
    private String status;
    private String plan;
    private String age_min;
    private String age_max;
    private String faculty;


    public questionnaire() {
    }

    public questionnaire(String questionnaireId, String creator_name, String title, String created_date, String status, String plan, String age_min, String age_max, String faculty) {
        this.questionnaireId = questionnaireId;
        this.creator_name = creator_name;
        this.title = title;
        this.created_date = created_date;
        this.status = status;
        this.plan = plan;
        this.age_min = age_min;
        this.age_max = age_max;
        this.faculty = faculty;
    }

    public String getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(String questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
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

    public String getPlan(){ return plan; }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getAge_min() {
        return age_min;
    }

    public void setAge_min(String age_min) {
        this.age_min = age_min;
    }

    public String getAge_max() {
        return age_max;
    }

    public void setAge_max(String age_max) {
        this.age_max = age_max;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
}
