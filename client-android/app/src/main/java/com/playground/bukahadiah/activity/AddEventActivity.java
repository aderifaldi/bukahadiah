package com.playground.bukahadiah.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.playground.bukahadiah.R;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.helper.image.CopyFile;
import com.playground.bukahadiah.helper.GlobalVariable;
import com.playground.bukahadiah.model.bukahadiah.BHGift;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEventActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm";

    private static final String FILE_EXTENTION = ".png";
    private static final String FILE_NAME = "event_image";

    private static final int RESULT_LOAD_IMAGE_FROM_CAMERA = 10;
    private static final int RESULT_LOAD_IMAGE_FROM_GALLERY = 11;

    @BindView(R.id.pageTitle)
    CustomTextView pageTitle;
    @BindView(R.id.imageEvent)
    ImageView imageEvent;
    @BindView(R.id.uploadImage)
    CustomTextView uploadImage;
    @BindView(R.id.edtEventName)
    EditText edtEventName;
    @BindView(R.id.tilEventName)
    TextInputLayout tilEventName;
    @BindView(R.id.edtDesc)
    EditText edtDesc;
    @BindView(R.id.tilDesc)
    TextInputLayout tilDesc;
    @BindView(R.id.eventDate)
    CustomTextView eventDate;

    private File fileUpload = null;
    private File fileTempUpload = null;
    private SimpleDateFormat dateFormat, timeFormat;
    private Calendar calendar;
    private String dateEvent = "";
    private String timeEvent = "";
    private String fileUploadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);

        pageTitle.setText("New Event");

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

        tilEventName.setTypeface(tfRegular);
        tilDesc.setTypeface(tfRegular);
        edtEventName.setTypeface(tfRegular);
        edtDesc.setTypeface(tfRegular);

        registerForContextMenu(uploadImage);
        registerForContextMenu(imageEvent);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.upload_picture, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.camera) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                            GlobalVariable.REQUEST_CAMERA_FOR_TAKE_PHOTO);
                } else {
                    openCameraForProfile();
                }
            } else {
                openCameraForProfile();
            }

            return true;
        } else if (item.getItemId() == R.id.gallery2) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            GlobalVariable.REQUEST_GALERY);
                } else {
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE_FROM_GALLERY);
                }
            } else {
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE_FROM_GALLERY);
            }

            return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case GlobalVariable.REQUEST_CAMERA_FOR_TAKE_PHOTO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCameraForProfile();
                }

                break;

            case GlobalVariable.REQUEST_GALERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE_FROM_GALLERY);
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            processImageFromCameraForProfile(requestCode, resultCode, data);
        } else if (requestCode == RESULT_LOAD_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            processImageFromGalleryForProfile(requestCode, resultCode, data);
        }
    }

    private void openCameraForProfile() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileTempUpload = getCameraCaptureImageProfile();
        Uri imgUri = Uri.fromFile(fileTempUpload);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(cameraIntent, RESULT_LOAD_IMAGE_FROM_CAMERA);

    }

    private File getCameraCaptureImageProfile() {
        File outputDir = getExternalCacheDir();
        File file = null;
        try {
            file = new File(outputDir, FILE_NAME + dateFormat.format(calendar.getTime()) + FILE_EXTENTION);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private void processImageFromCameraForProfile(int requestCode, int resultCode, Intent data) {
        if (fileTempUpload != null && fileTempUpload.exists()) {
            loadImage();
        } else {
            if (data != null && data.getExtras() != null && data.getExtras().containsKey("data") && data.getExtras().get("data") instanceof Bitmap) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                try {
                    fileTempUpload = getCameraCaptureImageProfile();
                    FileOutputStream fos = new FileOutputStream(fileTempUpload);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                    loadImage();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap.recycle();
            }
        }
    }

    private void processImageFromGalleryForProfile(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT < 19) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            fileTempUpload = new File(picturePath);

            loadImage();
        } else {
            ParcelFileDescriptor parcelFileDescriptor;
            Uri selectedImage = data.getData();
            try {
                parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImage, "r");
                fileTempUpload = getCameraCaptureImageProfile();
                CopyFile copyFile = new CopyFile(parcelFileDescriptor, fileTempUpload.getAbsolutePath()) {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        loadImage();
                    }
                };
                copyFile.execute();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadImage() {
        fileUpload = Compressor.getDefault(this).compressToFile(fileTempUpload);
        Glide.with(this).
                load(fileUpload).
                crossFade().
                into(imageEvent);
        uploadImage.setVisibility(View.GONE);

        UploadImage();
    }

    private void UploadImage() {
        showLoading();

        Uri file = Uri.fromFile(new File(fileUpload.getPath()));

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference riversRef = storageRef.child("event_images/" + file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                dismissLoading();
                Toast.makeText(getApplicationContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                dismissLoading();
                Toast.makeText(getApplicationContext(), "Upload Success", Toast.LENGTH_SHORT).show();

                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                fileUploadUrl = downloadUrl.toString();
                Log.d("TAG", "file path: " + downloadUrl);
            }
        });

    }

    @OnClick({R.id.back, R.id.uploadImage, R.id.showCallendar, R.id.saveEvent, R.id.imageEvent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                exitByBackByPresses();
                break;
            case R.id.uploadImage:
                openContextMenu(uploadImage);
                break;
            case R.id.imageEvent:
//                openContextMenu(imageEvent);
                break;
            case R.id.showCallendar:
                DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                        .show(getFragmentManager(), "datePicker");
                break;
            case R.id.saveEvent:

                String eventsName = edtEventName.getText().toString();
                String eventsDesc =  edtDesc.getText().toString();

                if (fileUpload != null){

                    if (dateEvent.equals("") || dateEvent == ""){
                        Toast.makeText(this, "Event's date still empty", Toast.LENGTH_SHORT).show();
                    }else {
                        if (eventsName.equals("")){
                            edtEventName.setError("Event's name still empty");
                        } else if (eventsDesc.equals("")){
                            edtDesc.setError("Event's description still empty");
                        } else {
                            //Upload GiftData

                            showLoading();

                            jsonPost.addProperty("user_id", GlobalVariable.getUserId(this));
                            jsonPost.addProperty("event_name", eventsName);
                            jsonPost.addProperty("event_description", eventsDesc);
                            jsonPost.addProperty("event_photo", fileUploadUrl);
                            jsonPost.addProperty("created_date", dateEvent + " " + timeEvent);
                            jsonPost.addProperty("gift_count", 0);

                            Call<BHGift> call = apiServiceBH.CreateGiftBox(jsonPost);
                            call.enqueue(new Callback<BHGift>() {
                                @Override
                                public void onResponse(Call<BHGift> call, Response<BHGift> response) {
                                    dismissLoading();
                                    exitByBackByPresses();
                                    Toast.makeText(AddEventActivity.this, response.body().getAlerts().message, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<BHGift> call, Throwable t) {

                                }
                            });

                        }
                    }

                }else {
                    Toast.makeText(this, "Event's image still empty", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        dateEvent = dateFormat.format(calendar.getTime());

        TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true).
                show(getFragmentManager(), "timePicker");

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        timeEvent = timeFormat.format(calendar.getTime());

        eventDate.setText(dateEvent + " | " + timeEvent);

    }
}
