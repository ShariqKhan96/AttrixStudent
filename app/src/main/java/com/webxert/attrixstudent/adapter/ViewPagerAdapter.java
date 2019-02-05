package com.webxert.attrixstudent.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.webxert.attrixstudent.AppGenericClass;
import com.webxert.attrixstudent.FirstYearFragment;
import com.webxert.attrixstudent.FourthYearFragment;
import com.webxert.attrixstudent.SecondYearFragment;
import com.webxert.attrixstudent.ThirdYearFragment;
import com.webxert.attrixstudent.model.ClassModel;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private List<ClassModel> classes;

    public ViewPagerAdapter(FragmentManager fm, Context context, List<ClassModel> classes) {
        super(fm);
        this.context = context;
        this.classes = classes;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "1st Year";
            case 1:
                return "2nd Year";
            case 2:
                return "3rd Year";
            case 3:
                return "4rth Year";

        }
        return null;
    }

    private String getClassIdByPos(int pos) {
        String year = AppGenericClass.getInstance(context).getCurrentYear();
        switch (pos) {
            case 0:
                return "1st-" + year;
            case 1:
                return "2nd-" + year;
            case 2:
                return "3rd-" + year;
            case 3:
                return "4rth-" + year;

        }
        return null;
    }


    private List<ClassModel> getFilteredClasses(String filter) {
        List<ClassModel> filteredClass = new ArrayList<>();
        for (ClassModel model : classes) {
            if (model.getClassId().equals(filter)) {
                filteredClass.add(model);
            }
        }
        return filteredClass;
    }

    @Override
    public int getCount() {
        return 4; //4 different years
    }

    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                FirstYearFragment.list.addAll(getFilteredClasses("1"));
                return FirstYearFragment.getInstance();
            case 1:
                SecondYearFragment.list.addAll(getFilteredClasses("2"));
                return SecondYearFragment.getInstance();
            case 2:
                ThirdYearFragment.list.addAll(getFilteredClasses("3"));
                return ThirdYearFragment.getInstance();
            case 3:
                FourthYearFragment.list.addAll(getFilteredClasses("4"));
                return FourthYearFragment.getInstance();

        }

        return FirstYearFragment.getInstance();
    }
}
