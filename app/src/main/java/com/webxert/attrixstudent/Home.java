package com.webxert.attrixstudent;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.webxert.attrixstudent.adapter.ViewPagerAdapter;
import com.webxert.attrixstudent.common.FirebaseHelper;
import com.webxert.attrixstudent.model.ClassModel;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements FirebaseHelper.GetClassCallback {

    ViewPager viewPager;
    TabLayout tabLayout;
    List<ClassModel> cms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new);

        tabLayout = findViewById(R.id.tabs);

        FirebaseHelper fb = new FirebaseHelper(Home.this);
        fb.setGetClassCallback(this);
        fb.getClasses();

        viewPager = findViewById(R.id.pager);

    }

    @Override
    public void onSuccess(List<ClassModel> list) {
        cms = list;

        for(ClassModel cm:cms){
            if(cm.getEnrolledStudents() != null)
            {
                if(cm.getEnrolledStudents().contains(AppGenericClass.getInstance(Home.this).
                        getPrefs(AppGenericClass.TOKEN)))
                    cm.setRegister(true);
                else
                    cm.setRegister(false);

            }
        }

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),Home.this,cms));
        tabLayout.setupWithViewPager(viewPager);
    }
}
