package com.webxert.attrixstudent.model;

/**
 * Created by hp on 12/17/2018.
 */

public class ClassModel {
    String course_id;
    String course_name;
    boolean isRegister;

    public ClassModel(String course_id, String course_name, boolean isRegister) {
        this.course_id = course_id;
        this.course_name = course_name;
        this.isRegister = isRegister;
    }

    public ClassModel() {
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public boolean isRegister() {
        return isRegister;
    }

    public void setRegister(boolean register) {
        isRegister = register;
    }
}
