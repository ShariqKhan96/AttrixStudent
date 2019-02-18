package com.webxert.attrixstudent.common;

import com.webxert.attrixstudent.model.ClassModel;
import com.webxert.attrixstudent.model.SignInUpModel;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

/**
 * Created by hp on 12/17/2018.
 */

public class Common {
    public static SignInUpModel CURRENT_STUDENT = null;
    static List<ClassModel> arrayList = new ArrayList<>();
    public static String TOKEN = "";
    public static List<SignInUpModel> students = new ArrayList<>();

    public static void setStudents(List<SignInUpModel> list) {
        students.clear();
        students.addAll(list);
    }

    public static SignInUpModel findStudent(String face_id) {
        if (students.size() == 0)
            students.addAll(Paper.book().read("Students", new ArrayList<SignInUpModel>()));

        for (SignInUpModel model : students) {
            if (face_id.equals(model.getFaceId()))
                return model;
        }
        return null;
    }

    public static boolean isStudentExist(String face_id) {
        if (students.size() == 0)
            students.addAll(Paper.book().read("Students", new ArrayList<SignInUpModel>()));
        boolean found = false;
        for (SignInUpModel model : students) {
            if (model.getFaceId().equals(face_id))
                found = true;
        }
        return found;
    }


//    public static List<ClassModel> getArrayList() {
//        boolean tf = true;
//        String[] array = {"ICS-II", "IP", "AI", "CALCULUS I", "COMPILER CON.", "MMS"};
//        for (int i = 0; i <= 5; i++) {
//            arrayList.add(new ClassModel(i + " ", array[i], !tf));
//            tf = i % 2 == 0;
//        }
//        return Common.arrayList;
//    }

}


