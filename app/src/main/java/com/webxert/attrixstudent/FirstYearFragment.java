package com.webxert.attrixstudent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webxert.attrixstudent.adapter.ClassAdapter;
import com.webxert.attrixstudent.model.ClassModel;

import java.util.ArrayList;
import java.util.List;

public class FirstYearFragment extends Fragment {


    public static List<ClassModel> list = new ArrayList<>();
    List<ClassModel> classes = new ArrayList<>();
    RecyclerView recyclerView;
    public static FirstYearFragment instance = null;

    public static FirstYearFragment getInstance() {
        if (instance == null)
            instance = new FirstYearFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.pager_item_layout, container, false);


        recyclerView = itemView.findViewById(R.id.rv_classes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ClassAdapter(getActivity(), this.classes));

        return itemView;
    }


}
