package com.webxert.attrixstudent;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.webxert.attrixstudent.adapter.ViewPagerAdapter;
import com.webxert.attrixstudent.common.Common;
import com.webxert.attrixstudent.common.FirebaseHelper;
import com.webxert.attrixstudent.model.ClassModel;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements FirebaseHelper.GetClassCallback {

    ViewPager viewPager;
    TabLayout tabLayout;
    List<ClassModel> cms;
    ImageView logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new);
        logout = findViewById(R.id.logout);

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

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), Home.this, cms));
        tabLayout.setupWithViewPager(viewPager);
    }
}
