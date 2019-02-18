package com.webxert.attrixstudent;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webxert.attrixstudent.R;
import com.webxert.attrixstudent.common.Common;
import com.webxert.attrixstudent.model.Attendance;
import com.webxert.attrixstudent.model.AttendanceModel;

import java.util.List;

public class StatsActivity extends AppCompatActivity {
    TextView tv_total;
    TextView tv_present;
    TextView tv_absent;

    int totalCount = 0;
    int presentCount = 0;
    int absentCount = 0;

    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_activity);
        tv_absent = findViewById(R.id.absent_classes);
        tv_present = findViewById(R.id.present_classes);
        tv_total = findViewById(R.id.total_classes);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading Statistics");
        dialog.setTitle("Please Wait");
        getInformation();
    }

    private void getInformation() {
        totalCount = 0;
        presentCount = 0;
        absentCount = 0;
        dialog.show();
        FirebaseDatabase.getInstance().getReference("Attendance").child(Home.SELECTED_CLASS.getCourseNo()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dialog.dismiss();
                totalCount = (int) dataSnapshot.getChildrenCount();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    AttendanceModel attendanceModel = data.getValue(AttendanceModel.class);
                    List<Attendance> attendanceList = attendanceModel.getAttendanceList();
                    presentOrAbsent(attendanceList);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_total.setText(String.valueOf(totalCount));
                        tv_absent.setText(String.valueOf(absentCount));
                        tv_present.setText(String.valueOf(presentCount));
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void presentOrAbsent(List<Attendance> attendanceList) {
        int comparable = presentCount;
        for (Attendance attendance : attendanceList) {
            if (Common.isStudentExist(attendance.face_id)) {
                presentCount = presentCount + 1;
                break;
            }
        }
        if (comparable == presentCount) {
            absentCount = absentCount + 1;
        }
    }
}
