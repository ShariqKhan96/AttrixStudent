package com.webxert.attrixstudent.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hp on 12/17/2018.
 */

public class ClassModel {
    String title, detail, courseNo;
    private String teacherId;
    private String batchNo;
    private String section;
    private String classCode;
    private String yearOfTeaching;
    private String shift;
    private String program;
    private String shiftSectionProgram;
    private String classId;
    private List<String> enrolledStudents;


    public ClassModel() {
    }

    public ClassModel(String title, String detail, String courseNo, String teacherId, String batchNo, String section, String classCode, String yearOfTeaching, String shift, String program, String shiftSectionProgram, String classId, List<String> enrolledStudents) {
        this.title = title;
        this.detail = detail;
        this.courseNo = courseNo;
        this.teacherId = teacherId;
        this.batchNo = batchNo;
        this.section = section;
        this.classCode = classCode;
        this.yearOfTeaching = yearOfTeaching;
        this.shift = shift;
        this.program = program;
        this.shiftSectionProgram = shiftSectionProgram;
        this.classId = classId;
        this.enrolledStudents = enrolledStudents;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getYearOfTeaching() {
        return yearOfTeaching;
    }

    public void setYearOfTeaching(String yearOfTeaching) {
        this.yearOfTeaching = yearOfTeaching;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getShiftSectionProgram() {
        return shiftSectionProgram;
    }

    public void setShiftSectionProgram(String shiftSectionProgram) {
        this.shiftSectionProgram = shiftSectionProgram;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public List<String> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(List<String> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }
}
