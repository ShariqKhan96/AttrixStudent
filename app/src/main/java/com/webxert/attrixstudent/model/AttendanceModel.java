package com.webxert.attrixstudent.model;



import java.util.List;

public class AttendanceModel {


    private List<Attendance> attendanceList;
    private String sectionName;
    private String shift;
    private String program;
    private String year;
    private String courseNo;

    public AttendanceModel() {
    }

    public AttendanceModel(List<Attendance> attendanceList, String sectionName, String shift, String program, String year, String courseNo) {
        this.attendanceList = attendanceList;
        this.sectionName = sectionName;
        this.shift = shift;
        this.program = program;
        this.year = year;
        this.courseNo = courseNo;
    }

    public List<Attendance> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<Attendance> attendanceList) {
        this.attendanceList = attendanceList;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
