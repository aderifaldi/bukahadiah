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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonObject;
import com.playground.bukahadiah.R;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.helper.image.CopyFile;
import com.playground.bukahadiah.helper.GlobalVariable;
import com.playground.bukahadiah.model.bukahadiah.ModelBase;

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

public class AddPostActivity extends BaseActivity {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String FILE_EXTENTION = ".png";
    private static final String FILE_NAME = "event_image";

    private static final int RESULT_LOAD_IMAGE_FROM_CAMERA = 10;
    private static final int RESULT_LOAD_IMAGE_FROM_GALLERY = 11;

    @BindView(R.id.pageTitle)
    CustomTextView pageTitle;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.sendPost)
    RelativeLayout sendPost;
    @BindView(R.id.imagePost)
    ImageView imagePost;
    @BindView(R.id.uploadImage)
    CustomTextView uploadImage;
    @BindView(R.id.edtCaption)
    EditText edtCaption;
    @BindView(R.id.tilCaption)
    TextInputLayout tilCaption;

    private File fileUploadPost = null;
    private File fileTempUploadPost = null;
    private SimpleDateFormat dateFormat;
    private Calendar calendar;
    private String fileUploadUrlPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        ButterKnife.bind(this);

        pageTitle.setText("Share Gift");

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

        edtCaption.setTypeface(tfRegular);
        tilCaption.setTypeface(tfLight);

        registerForContextMenu(uploadImage);

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
                    openCameraForPost();
                }
            } else {
                openCameraForPost();
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
                    openCameraForPost();
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
            processImageFromCameraForPost(requestCode, resultCode, data);
        } else if (requestCode == RESULT_LOAD_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            processImageFromGalleryForPost(requestCode, resultCode, data);
        }
    }

    private void openCameraForPost() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileTempUploadPost = getCameraCaptureImagePost();
        Uri imgUri = Uri.fromFile(fileTempUploadPost);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(cameraIntent, RESULT_LOAD_IMAGE_FROM_CAMERA);

    }

    private File getCameraCaptureImagePost() {
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

    private void processImageFromCameraForPost(int requestCode, int resultCode, Intent data) {
        if (fileTempUploadPost != null && fileTempUploadPost.exists()) {
            loadImagePost();
        } else {
            if (data != null && data.getExtras() != null && data.getExtras().containsKey("data") && data.getExtras().get("data") instanceof Bitmap) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                try {
                    fileTempUploadPost = getCameraCaptureImagePost();
                    FileOutputStream fos = new FileOutputStream(fileTempUploadPost);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                    loadImagePost();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap.recycle();
            }
        }
    }

    private void processImageFromGalleryForPost(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT < 19) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            fileTempUploadPost = new File(picturePath);

            loadImagePost();
        } else {
            ParcelFileDescriptor parcelFileDescriptor;
            Uri selectedImage = data.getData();
            try {
                parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImage, "r");
                fileTempUploadPost = getCameraCaptureImagePost();
                CopyFile copyFile = new CopyFile(parcelFileDescriptor, fileTempUploadPost.getAbsolutePath()) {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        loadImagePost();
                    }
                };
                copyFile.execute();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadImagePost() {
        fileUploadPost = Compressor.getDefault(this).compressToFile(fileTempUploadPost);
        Glide.with(this).
                load(fileUploadPost).
                crossFade().
                into(imagePost);
        uploadImage.setVisibility(View.GONE);

        UploadImagePost();
    }

    private void UploadImagePost() {
        showLoading();

        Uri file = Uri.fromFile(new File(fileUploadPost.getPath()));

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference riversRef = storageRef.child("post_images/" + file.getLastPathSegment());
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
                fileUploadUrlPost = downloadUrl.toString();
                Log.d("TAG", "file path: " + downloadUrl);
            }
        });

    }

    private void AddPost(){

        showLoading();

        SimpleDateFormat sdf = new SimpleDateFormat(GlobalVariable.DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();

        jsonPost = new JsonObject();
        jsonPost.addProperty("user_id", GlobalVariable.getUserId(getApplicationContext()));
        jsonPost.addProperty("post_desc", edtCaption.getText().toString());
        jsonPost.addProperty("post_pic", fileUploadUrlPost);
        jsonPost.addProperty("created_date", sdf.format(calendar.getTime()));

        Call<ModelBase> call = apiServiceBH.AddPost(jsonPost);
        call.enqueue(new Callback<ModelBase>() {
            @Override
            public void onResponse(Call<ModelBase> call, Response<ModelBase> response) {
                dismissLoading();

                if (!response.body().isError()){
                    Toast.makeText(getApplicationContext(), "Your post sent", Toast.LENGTH_SHORT).show();
                    exitByBackByPresses();
                }else {
                    Toast.makeText(getApplicationContext(), "Post failed, try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelBase> call, Throwable t) {

            }
        });

    }

    @OnClick({R.id.back, R.id.sendPost, R.id.uploadImage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                exitByBackByPresses();
                break;
            case R.id.sendPost:
                if (fileUploadPost == null){
                    Toast.makeText(getApplicationContext(), "Image still empty", Toast.LENGTH_SHORT).show();
                    }else if (edtCaption.getText().toString() == "" || edtCaption.getText().toString().isEmpty()){
                        edtCaption.setError("Caption still empty");
                    }else {
                        AddPost();
                }

                break;
            case R.id.uploadImage:
                openContextMenu(uploadImage);
                break;
        }
    }
}
