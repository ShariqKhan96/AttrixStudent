package com.webxert.attrixstudent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webxert.attrixstudent.interfaces.*;
import com.webxert.attrixstudent.adapter.ViewPagerAdapter;
import com.webxert.attrixstudent.common.Common;
import com.webxert.attrixstudent.common.FirebaseHelper;
import com.webxert.attrixstudent.model.ClassModel;
import com.webxert.attrixstudent.model.SignInUpModel;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class Home extends AppCompatActivity implements FirebaseHelper.GetClassCallback, ClassSelectedListener {

    ViewPager viewPager;
    TabLayout tabLayout;
    List<ClassModel> cms;
    ImageView logout,info;
    List<SignInUpModel> list;
    public static ClassModel SELECTED_CLASS = null;

    private void getStudents() {
        list = new ArrayList<>();
        Paper.book().delete("Students");
        list.clear();
        FirebaseDatabase.getInstance().getReference("Student").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        SignInUpModel student = data.getValue(SignInUpModel.class);
                        list.add(student);
                    }
                    Paper.book().write("Students", list);
                    Common.setStudents(list);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new);
        logout = findViewById(R.id.logout);
        info = findViewById(R.id.info);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudentDailog sd = new StudentDailog(Home.this);
//                sd.getWindow().setLayout(600,400);
                sd.show();
            }
        });

        getStudents();
        AppGenericClass.getInstance(this).setPrefs(AppGenericClass.LOGGED_IN, "true");
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllStaticLeaks();
                //FirebaseAuth.getInstance().signOut();
                AppGenericClass.getInstance(Home.this).clearPrefs();
                Intent intent = new Intent(Home.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });

        tabLayout = findViewById(R.id.tabs);

        FirebaseHelper fb = new FirebaseHelper(Home.this);
        fb.setGetClassCallback(this);
        fb.getClasses();

        viewPager = findViewById(R.id.pager);

    }

    private void clearAllStaticLeaks() {
        FirstYearFragment.list.clear();
        SecondYearFragment.list.clear();
        ThirdYearFragment.list.clear();
        FourthYearFragment.list.clear();
    }

    @Override
    public void onSuccess(List<ClassModel> list) {
        cms = new ArrayList<>();
        cms.addAll(list);
//        cms = list;
//
//        for (ClassModel cm : cms) {
//            if (cm.getEnrolledStudents() != null) {
//                if (cm.getEnrolledStudents().contains(AppGenericClass.getInstance(Home.this).
//                        getPrefs(AppGenericClass.TOKEN)))
//                    cm.setRegister(true);
//                else
//                    cm.setRegister(false);
//
//            }
//        }

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), Home.this, cms, this));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClassSelected(ClassModel _class) {
        SELECTED_CLASS = _class;
    }
}
