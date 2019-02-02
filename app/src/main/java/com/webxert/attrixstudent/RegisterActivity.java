package com.webxert.attrixstudent;

import android.view.View;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.webxert.attrixstudent.common.FirebaseHelper;
import com.webxert.attrixstudent.model.SignInUpModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements FirebaseHelper.RegisterCallBack,FirebaseHelper.StoreImageCallBack {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    LinearLayout image_picker_view;
    int image_count = 0;
    FirebaseHelper firebaseHelper;
    Bitmap[] bitmaps = new Bitmap[3];
    List<String> downloadUris = new ArrayList<>();
    LinearLayout auth_view;
    EditText name, mobile, pass, section, program,year, batchNo, seatNo, shift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        firebaseHelper = new FirebaseHelper(RegisterActivity.this);


        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        pass = findViewById(R.id.pass);
        seatNo = findViewById(R.id.seat_no);
        section = findViewById(R.id.section);
        shift = findViewById(R.id.shift);
        program = findViewById(R.id.program);
        batchNo = findViewById(R.id.batch_no);
        year = findViewById(R.id.year);


        image_picker_view = findViewById(R.id.image_picker_view);
        auth_view = findViewById(R.id.auth_view);
        auth_view.setVisibility(View.GONE);
        image_picker_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

            }
        });
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseHelper.setRegisterCallBack(RegisterActivity.this);
                firebaseHelper.registerStudent(getRegisterModel());
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data.getClipData() != null && data.getClipData().getItemCount() > 0) {
                //PICK

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                ClipData clipData = data.getClipData();

                //MULTIPICK CHECK

                image_count = data.getClipData().getItemCount();

                if (image_count < 3) {
                    auth_view.setVisibility(View.GONE);
                    image_picker_view.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "You should pick atleast 3 images.", Toast.LENGTH_SHORT).show();
                } else if (image_count > 3) {
                    auth_view.setVisibility(View.GONE);
                    image_picker_view.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "You should'nt be picking more than 3 images.", Toast.LENGTH_SHORT).show();
                } else {
                    image_picker_view.setVisibility(View.GONE);
                    auth_view.setVisibility(View.VISIBLE);
                    bitmaps = new Bitmap[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {

                        ClipData.Item item = clipData.getItemAt(i);

                        Uri uri = item.getUri();

                        firebaseHelper.setStoreImageCallBack(RegisterActivity.this);
                        firebaseHelper.uploadImage(uri);

                        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        try {
                            //Log.e("imageUri", cursor.getString(columnIndex));
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            bitmaps[i] = bitmap;
                            cursor.close();


                        } catch (IOException e) {

                            Log.e(TAG, e.getMessage());
                        }

                    }

                }
            }

        }
    }

    private SignInUpModel getRegisterModel() {
        SignInUpModel model = new SignInUpModel();

        model.setBatchNo(batchNo.getText().toString().trim());
        model.setName(name.getText().toString().trim());
        model.setPass(pass.getText().toString().trim());
        model.setMobile(mobile.getText().toString().trim());
        model.setSeatNo(seatNo.getText().toString().trim());
        model.setSection(section.getText().toString().trim());
        model.setProgram(program.getText().toString().trim());
        model.setShift(shift.getText().toString().trim());
        model.setYear(year.getText().toString() + "-"+ AppGenericClass.getInstance(RegisterActivity.this).getCurrentYear());
        model.setImgUrls(downloadUris);

        return model;
    }

    @Override
    public void onRegister(boolean success,String id) {
        if (success) {
            AppGenericClass.getInstance(this).setPrefs(AppGenericClass.CLASS_ID,
                    year.getText().toString() + "-"+ AppGenericClass.getInstance(RegisterActivity.this).getCurrentYear());

            AppGenericClass.getInstance(this).setPrefs(AppGenericClass.TOKEN,id);
            startActivity(new Intent(RegisterActivity.this, Home.class));
        }
        else
            Toast.makeText(RegisterActivity.this, "User already present", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSuccess(Uri uri) {
        downloadUris.add(uri.toString());
    }
}
