package com.webxert.attrixstudent.common;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webxert.attrixstudent.model.SignInUpModel;

import dmax.dialog.SpotsDialog;

public class FirebaseHelper {

    DatabaseReference dbRef;
    Context context;
    SignInCallBack signInCallBack;
    RegisterCallBack registerCallBack;

    public interface SignInCallBack {
        void onSignIn(int code);
    }

    public interface RegisterCallBack {
        void onRegister(boolean success);
    }

    public void setSignInCallBack(SignInCallBack signInCallBack) {
        this.signInCallBack = signInCallBack;
    }

    public void setRegisterCallBack(RegisterCallBack registerCallBack) {
        this.registerCallBack = registerCallBack;
    }

    public FirebaseHelper(Context context) {
        this.dbRef = FirebaseDatabase.getInstance().getReference();
        this.context = context;
    }

    public void registerStudent(final SignInUpModel signInUpModel) {

        final AlertDialog dialog = new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Registering. Please wait...");
        dialog.show();

        dbRef.child("Student").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dialog.dismiss();
                boolean exists = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SignInUpModel model = snapshot.getValue(SignInUpModel.class);
                    exists = signInUpModel.getMobile().equals(model.getMobile());
                    if (exists)
                        break;
                }

                if (!exists) {
                    dbRef.child("Student").push().setValue(signInUpModel);
                    registerCallBack.onRegister(true);
                } else
                    registerCallBack.onRegister(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                Log.d("dbError", databaseError.getMessage());
            }
        });
    }

    public void signInStudent(final String mobileNo, final String pass) {

        final AlertDialog dialog = new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Logging In. Please wait...");
        dialog.show();
        dbRef.child("Student").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dialog.dismiss();

                boolean mobileMatch;
                boolean passMatch;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    SignInUpModel model = snapshot.getValue(SignInUpModel.class);

                    mobileMatch = mobileNo.equals(model.getMobile());
                    passMatch = pass.equals(model.getPass());

                    if (mobileMatch && passMatch) {
                        signInCallBack.onSignIn(200);
                        return;
                    } else if (mobileMatch && !passMatch) {
                        signInCallBack.onSignIn(201);
                        return;
                    }
                }

                signInCallBack.onSignIn(202);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                Log.d("dbError", databaseError.getMessage());
            }
        });
    }
}
