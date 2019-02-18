package com.webxert.attrixstudent.model;

/**
 * Created by hp on 2/11/2019.
 */

public class Attendance {
    public String face_id;
    public boolean is_present;

    public Attendance() {
    }

    public Attendance(String face_id, boolean is_present) {
        this.face_id = face_id;
        this.is_present = is_present;
    }

    public String getFace_id() {
        return face_id;
    }

    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }

    public boolean isIs_present() {
        return is_present;
    }

    public void setIs_present(boolean is_present) {
        this.is_present = is_present;
    }
}
