package com.webxert.attrixstudent.adapter;

import android.app.AlertDialog;
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

import com.webxert.attrixstudent.R;
import com.webxert.attrixstudent.StatsActivity;
import com.webxert.attrixstudent.common.Common;

/**
 * Created by hp on 12/17/2018.
 */


public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.MyVH> {

    Context context;

    public ClassAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.class_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyVH holder, int position) {

        holder.course_name.setText(Common.getArrayList().get(holder.getAdapterPosition()).getCourse_name());
        if (Common.getArrayList().get(holder.getAdapterPosition()).isRegister()) {
            holder.lock.setVisibility(View.GONE);
            holder.show.setVisibility(View.VISIBLE);
        } else {
            holder.lock.setVisibility(View.VISIBLE);
            holder.show.setVisibility(View.GONE);
        }

        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,StatsActivity.class));
            }
        });
        holder.lock.setOnClickListener(new View.OnClickListener() {
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
                        if (editText.getText().toString().equals(Common.getArrayList().get(holder.getAdapterPosition()).getCourse_name())) {
                            holder.show.setVisibility(View.VISIBLE);
                            holder.lock.setVisibility(View.GONE);
                            Common.getArrayList().get(holder.getAdapterPosition()).setRegister(true);
                        } else {
                            holder.show.setVisibility(View.GONE);
                            holder.lock.setVisibility(View.VISIBLE);
                        }

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

    @Override
    public int getItemCount() {
        return Common.getArrayList().size();
    }

    public class MyVH extends RecyclerView.ViewHolder {
        TextView course_name;
        ImageView lock;
        ImageView show;


        public MyVH(View itemView) {
            super(itemView);
            course_name = itemView.findViewById(R.id.course_name);
            lock = itemView.findViewById(R.id.lock_unlock);
            show = itemView.findViewById(R.id.watch);



        }
    }
}
