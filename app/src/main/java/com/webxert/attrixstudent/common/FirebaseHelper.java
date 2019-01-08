package com.webxert.attrixstudent.common;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.webxert.attrixstudent.model.SignInUpModel;

import dmax.dialog.SpotsDialog;

public class FirebaseHelper {

    DatabaseReference dbRef;
    StorageReference storageRef;
    Context context;
    SignInCallBack signInCallBack;


    public void setStoreImageCallBack(StoreImageCallBack storeImageCallBack) {
        this.storeImageCallBack = storeImageCallBack;
    }

    StoreImageCallBack storeImageCallBack;
    RegisterCallBack registerCallBack;


    public interface StoreImageCallBack{
        void onSuccess(Uri uri);
    }

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


    public void uploadImage(final Uri image){
        storageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference ref = storageRef.child("Student Images"+ image.getLastPathSegment());
        final AlertDialog dialog = new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Uploading File. Please wait...");
        dialog.show();

        Task<Uri> uploadTask = ref.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Log.d("Exception",e.toString());
            }
        }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    storeImageCallBack.onSuccess(downloadUri);
                } else {
                    Log.d("Failed Download","something went wrong");
                }
            }
        });
    }
    public void uploadImageOfStudent(final Uri image) {
        storageRef = FirebaseStorage.getInstance().getReference().child("Student Images");
        final AlertDialog dialog = new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Uploading File. Please wait...");
        dialog.show();

        storageRef.child(image.toString()).putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                dialog.dismiss();

                final AlertDialog dialog1 = new SpotsDialog.Builder().setContext(context).build();
                dialog1.setMessage("Preparing download Url. Please wait...");
                dialog1.show();

                storageRef.child(image.toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        dialog1.dismiss();
                        storeImageCallBack.onSuccess(uri);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog1.dismiss();
                        Log.d("Exception",e.toString());
                    }
                });
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                dialog.dismiss();
                Log.d("Canceled Uploading", image.toString());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Log.d("Failed Uploading", e.toString());
            }
        });
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
