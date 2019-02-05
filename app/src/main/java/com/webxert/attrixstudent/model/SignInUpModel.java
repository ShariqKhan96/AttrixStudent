package com.webxert.attrixstudent.model;

import java.util.List;

public class SignInUpModel {
    private String seatNo;
    private String pass;
    private String mobile;
    private String batchNo;
    private String shift;
    private String section;
    private String faceId;
    private String name;
    private String year;
    private String program;


    public SignInUpModel() {
    }

    public SignInUpModel(String seatNo, String pass, String mobile, String batchNo, String shift, String section, String faceId, String name, String year, String program) {
        this.seatNo = seatNo;
        this.pass = pass;
        this.mobile = mobile;
        this.batchNo = batchNo;
        this.shift = shift;
        this.section = section;
        this.faceId = faceId;
        this.name = name;
        this.year = year;
        this.program = program;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }


    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }



}
