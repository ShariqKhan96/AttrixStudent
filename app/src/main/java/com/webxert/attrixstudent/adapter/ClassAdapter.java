package com.webxert.attrixstudent.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.webxert.attrixstudent.AppGenericClass;
import com.webxert.attrixstudent.R;
import com.webxert.attrixstudent.StatsActivity;
import com.webxert.attrixstudent.common.FirebaseHelper;
import com.webxert.attrixstudent.interfaces.ClassSelectedListener;
import com.webxert.attrixstudent.model.ClassModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 12/17/2018.
 */


public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.MyVH> {

    Context context;
    List<ClassModel> cms = new ArrayList<>();
    ClassSelectedListener classSelectedListener;

    public ClassAdapter(Context context, List<ClassModel> cms, ClassSelectedListener classSelectedListener) {
        this.context = context;
        this.cms = cms;
        this.classSelectedListener = classSelectedListener;
    }

    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.class_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyVH holder, int position) {

        // holder.course_name.setText(cms.get(holder.getAdapterPosition()).getTitle());
        ClassModel model = cms.get(position);

        holder.Title.setText(model.getTitle());
        holder.tv_courseNo.setText(model.getCourseNo());
        holder.clteachername.setText(model.getYearOfTeaching() + " Year-" + model.getProgram() + "-" + model.getShift() + "-" + model.getSection());


        if (cms.get(holder.getAdapterPosition()).getEnrolledStudents().contains(AppGenericClass.getInstance(context).getPrefs(AppGenericClass.TOKEN))) {
            holder.lock.setVisibility(View.GONE);
            holder.show.setVisibility(View.VISIBLE);
        } else {
            holder.lock.setVisibility(View.VISIBLE);
            holder.show.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return cms.size();
    }

    public class MyVH extends RecyclerView.ViewHolder {
        TextView Title, tv_courseNo, clteachername;
        ImageView lock;
        ImageView show;


        public MyVH(View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.cltitle);
            tv_courseNo = itemView.findViewById(R.id.tv_courseNo);
            clteachername = itemView.findViewById(R.id.clteachername);
            lock = itemView.findViewById(R.id.lock_unlock);
            show = itemView.findViewById(R.id.watch);

            show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    classSelectedListener.onClassSelected(cms.get(getAdapterPosition()));
                    context.startActivity(new Intent(context, StatsActivity.class));
                }
            });

            lock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Verificaiton");
                    builder.setMessage("Enter your verification code");
                    final EditText editText = new EditText(context);
                    builder.setView(editText);

                    builder.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (editText.getText().toString().equals(cms.get(getAdapterPosition()).getClassCode())) {
                                show.setVisibility(View.VISIBLE);
                                lock.setVisibility(View.GONE);
                                // notifyDataSetChanged();

                                //locally

                                if (cms.get(getAdapterPosition()).getEnrolledStudents().get(0).equals("-1"))
                                    cms.get(getAdapterPosition()).getEnrolledStudents().clear();
                                cms.get(getAdapterPosition()).getEnrolledStudents().add(AppGenericClass.getInstance(context).getPrefs(AppGenericClass.TOKEN));

                                //firebase

                                new FirebaseHelper(context).addStudentInClass(
                                        cms.get(getAdapterPosition()).getClassId(),
                                        AppGenericClass.getInstance(context).getPrefs(AppGenericClass.TOKEN),
                                        cms.get(getAdapterPosition()).getEnrolledStudents());
                            } else {
                                show.setVisibility(View.GONE);
                                lock.setVisibility(View.VISIBLE);
                                Toast.makeText(context, "Entered secret key is wrong!", Toast.LENGTH_SHORT).show();
                            }
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.show();
                }
            });
        }
    }
}
