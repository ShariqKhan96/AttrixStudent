package com.webxert.attrixstudent;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    LinearLayout image_picker_view;
    int image_count = 0;
    Bitmap[] bitmaps = new Bitmap[3];
    LinearLayout auth_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
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
                startActivity(new Intent(LoginActivity.this, Home.class));
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

                        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
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


            //images_count_tv.setText(String.format("%s images are picked!", data.getClipData().getItemCount() + " "));


            /// / ArrayList<Uri> arrayList = new ArrayList<>();


        }


    }
}
