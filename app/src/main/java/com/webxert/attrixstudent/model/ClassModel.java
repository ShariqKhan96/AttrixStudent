package com.webxert.attrixstudent.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hp on 12/17/2018.
 */

public class ClassModel {
    String course_id;
    String course_name;
    String year;
    private String classKey;
    boolean isRegister;
    private String courseNo;
    private List<String> enrolledStudents;
    private String classId;
    private String teacherId;
    private String title;
    private String classCode;


    public ClassModel() {
    }

    public ClassModel(String course_id, String course_name, String year, String classKey, boolean isRegister, String courseNo, List<String> enrolledStudents, String classId, String teacherId, String title, String classCode) {
        this.course_id = course_id;
        this.course_name = course_name;
        this.year = year;
        this.classKey = classKey;
        this.isRegister = isRegister;
        this.courseNo = courseNo;
        this.enrolledStudents = enrolledStudents;
        this.classId = classId;
        this.teacherId = teacherId;
        this.title = title;
        this.classCode = classCode;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getClassKey() {
        return classKey;
    }

    public void setClassKey(String classKey) {
        this.classKey = classKey;
    }

    public boolean isRegister() {
        return isRegister;
    }

    public void setRegister(boolean register) {
        isRegister = register;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public List<String> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(List<String> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }
}
