package com.webxert.attrixstudent;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.webxert.attrixstudent.model.SignInUpModel;

import io.paperdb.Paper;

public class StudentDailog extends Dialog {


    int itemPos = -1;
    TextView tv_name, tv_mobileNo,tv_classId,tv_seatNo;

    public interface AttendanceInterface{
        public void onAction(boolean isPresent, int itemPos);
    }

    public StudentDailog(@NonNull Context context) {
        super(context);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_student_dialog);
        tv_name = findViewById(R.id.tv_name);
        tv_mobileNo = findViewById(R.id.tv_mobileNo);
        tv_classId = findViewById(R.id.tv_classId);
        tv_seatNo = findViewById(R.id.tv_seatNo);


        SignInUpModel student = Paper.book().read("User");

        try {
            tv_seatNo.setText(student.getSeatNo());

            tv_classId.setText(student.getProgram() + " " +
                    student.getShift() + " " +
                    student.getSection());

            tv_name.setText(student.getName());
            tv_mobileNo.setText(student.getMobile());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
