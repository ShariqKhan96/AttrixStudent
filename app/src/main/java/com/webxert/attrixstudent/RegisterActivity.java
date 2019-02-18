package com.webxert.attrixstudent;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.AddPersistedFaceResult;
import com.microsoft.projectoxford.face.contract.CreatePersonResult;
import com.microsoft.projectoxford.face.contract.PersonGroup;
import com.microsoft.projectoxford.face.contract.TrainingStatus;
import com.webxert.attrixstudent.common.FirebaseHelper;
import com.webxert.attrixstudent.model.SignInUpModel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements FirebaseHelper.RegisterCallBack, FirebaseHelper.StoreImageCallBack {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    LinearLayout image_picker_view;
    int image_count = 0;
    FirebaseHelper firebaseHelper;
    Bitmap[] bitmaps = new Bitmap[3];
    List<String> downloadUris = new ArrayList<>();
    LinearLayout auth_view;
    EditText name, mobile, pass, section, program, year, batchNo, seatNo, shift;
    Uri uri;
    String UNIQUE_ID;
    PersonGroup group;
    int i;
    ProgressDialog dialog;
    ByteArrayInputStream byteArrayInputStream;
    private FaceServiceRestClient faceServiceRestClient = new FaceServiceRestClient("https://westcentralus.api.cognitive.microsoft.com/face/v1.0", "f4d0c60f29634336b2c4c6dd0357bebe");
    String seatNumber, studentName;
    public static String FaceID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Registering");
        dialog.setMessage("Please Wait..");

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
        Toast.makeText(this, "Tap to pick images", Toast.LENGTH_LONG).show();
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
                registerUserFace();

                seatNumber = seatNo.getText().toString();
                studentName = name.getText().toString();

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
                    for (i = 0; i < clipData.getItemCount(); i++) {

                        ClipData.Item item = clipData.getItemAt(i);

                        uri = item.getUri();

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
        model.setYear(year.getText().toString() + "-" + AppGenericClass.getInstance(RegisterActivity.this).getCurrentYear());
        model.setFaceId(FaceID);


        return model;
    }

    @Override
    public void onRegister(boolean success, String id) {
        if (success) {
            AppGenericClass.getInstance(this).setPrefs(AppGenericClass.CLASS_ID,
                    year.getText().toString() + "-" + AppGenericClass.getInstance(RegisterActivity.this).getCurrentYear());

            AppGenericClass.getInstance(this).setPrefs(AppGenericClass.TOKEN, id);

            startActivity(new Intent(RegisterActivity.this, Home.class));
        } else
            Toast.makeText(RegisterActivity.this, "User already present", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSuccess(Uri uri) {
        downloadUris.add(uri.toString());
    }

    private void registerUserFace() {//call createperson here
        new CreatePersonGroupTask().execute();
    }

    private class CreatePersonTask extends AsyncTask<Void, String, CreatePersonResult> {

        //        String batchNo,shift,section,seatNo,name;
        @Override
        protected CreatePersonResult doInBackground(Void... voids) {
            try {
                publishProgress("Creating person in group");
                //dialog.dismiss();
                UNIQUE_ID = year.getText().toString() + "-" + AppGenericClass.getInstance(RegisterActivity.this).getCurrentYear();
                return faceServiceRestClient.createPerson(UNIQUE_ID,
                        seatNumber + " " + studentName, null);

            } catch (Exception e) {
                e.printStackTrace();
                dialog.dismiss();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected void onPostExecute(CreatePersonResult createPersonResult) {
            dialog.dismiss();

            //person id bhi faceid ki tarhan treat hosakti hai might be possible

            //this is the face id which is to be pushed on firebase.
            FaceID = createPersonResult.personId.toString().trim();
            Log.e("personId", String.valueOf(createPersonResult.personId));
            if (createPersonResult != null) {

                Log.e(MainActivity.class.getSimpleName(), "Person id is " + createPersonResult.personId);
                new DetectAndRegister(createPersonResult).execute();
            }
        }

        @Override
        protected void onProgressUpdate(final String... values) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    publishProgress(values[0]);
                }
            });

        }
    }

    private class DetectAndRegister extends AsyncTask<InputStream, String, Boolean> {

        CreatePersonResult createPersonResult;


        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        public DetectAndRegister(CreatePersonResult createPersonResult) {
            this.createPersonResult = createPersonResult;
        }

        @Override
        protected Boolean doInBackground(InputStream... inputStreams) {


            //creating function which returns compressed bitmap

            try {

                for (Bitmap bitmap : bitmaps) {


                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                    publishProgress("Training....");
                    try {
                        //  progressDialog.show();

                        Log.e("Here", "Here");

                        AddPersistedFaceResult faceResult = faceServiceRestClient.addPersonFace(UNIQUE_ID, createPersonResult.personId, byteArrayInputStream, null, null);
                        if (faceResult != null) {
                            // progressDialog.dismiss();
                            publishProgress("Training....");
                            Log.e("persistedFaceId", String.valueOf(faceResult.persistedFaceId));

                        }
                    } catch (Exception e) {

                        //progressDialog.dismiss();
                        publishProgress(e.getLocalizedMessage());
                        Log.e("ImageTrainingException", e.getMessage());
                    }


                    byteArrayOutputStream.close();
                    byteArrayInputStream.close();
                }
            } catch (Exception e) {

                Log.e("AddingFaceException", e.getMessage());
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            dialog.dismiss();
            Log.e("TrainPersonGroup", "TrainPersonGroup is going to run " + bool);
            if (bool)
                new TrainPersonGroup().execute();
            else Log.e("TrainingResult", String.valueOf(bool));

        }
    }

    private class TrainPersonGroup extends AsyncTask<Void, String, Boolean> {


        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean trainingBool) {
            dialog.dismiss();
            if (trainingBool) {
                firebaseHelper.setRegisterCallBack(RegisterActivity.this);
                firebaseHelper.registerStudent(getRegisterModel());
//                Intent intent = new Intent(RegisterActivity.this, Home.class);
//                startActivity(intent);

                //Registration Successful intent to main activity else try to train again
            } else {
                Toast.makeText(RegisterActivity.this, "Unable to train!", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
            dialog.setMessage(values[0]);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            Boolean isTrainingDone = false;

            try {
                publishProgress("Fetching training status");
                faceServiceRestClient.trainPersonGroup(UNIQUE_ID);
                TrainingStatus trainingStatus = null;

                while (true) {
                    trainingStatus = faceServiceRestClient.getPersonGroupTrainingStatus(UNIQUE_ID);
                    Log.e("TrainingStatus", trainingStatus.status + " ");
                    if (trainingStatus.status != TrainingStatus.Status.Running) {
                        publishProgress("Current training status is " + trainingStatus.status);
                        isTrainingDone = true;
                        break;

                    }

                    Thread.sleep(1000);
                }
                Log.e("TrainingAI", "Training Completed !");
                //  finish();


            } catch (Exception e) {
                Log.d("Training Error", e.getMessage());
                isTrainingDone = false;
            }

            return isTrainingDone;
        }
    }

    private class CreatePersonGroupTask extends AsyncTask<Void, String, Boolean> {
        @Override
        protected void onProgressUpdate(String... values) {
            dialog.setMessage(values[0]);
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                //updating old group. If you want to create a new group change to createPerson Group....
                UNIQUE_ID = year.getText().toString() + "-" + AppGenericClass.getInstance(RegisterActivity.this).getCurrentYear();
                faceServiceRestClient.createPersonGroup(UNIQUE_ID, "UBIT Students", null);
            } catch (Exception e) {
                Log.e("Client/IO Exception", e.getMessage());
                publishProgress("Exception caught");
                e.printStackTrace();
                dialog.dismiss();
                return false;
            }


            return true;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            dialog.dismiss();
            try {
                new GetPersonResult().execute();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("UpdatingError", e.getMessage());

            }
        }

    }

    private class GetPersonResult extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... voids) {
            try {
                group = faceServiceRestClient.getPersonGroup(UNIQUE_ID);
                publishProgress("GettingPersonResult");
            } catch (Exception e) {
                e.printStackTrace();
                publishProgress("Eception: " + e.getMessage());
                Log.e("GettingpersonError", e.getMessage());
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            dialog.dismiss();
            if (group != null) {
                Log.e("personCreated", String.valueOf(bool));
                if (bool) {
                    new CreatePersonTask().execute();
                }
            }
        }
    }

}

