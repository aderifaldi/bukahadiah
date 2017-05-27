package com.playground.bukahadiah.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonObject;
import com.playground.bukahadiah.R;
import com.playground.bukahadiah.adapter.ProfilePagerAdapter;
import com.playground.bukahadiah.customui.imageview.CircleImageView;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.helper.image.CopyFile;
import com.playground.bukahadiah.helper.GlobalVariable;
import com.playground.bukahadiah.model.bukahadiah.BHUser;
import com.playground.bukahadiah.model.bukahadiah.ModelBase;
import com.radyalabs.irfan.util.AppUtility;

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

import static android.app.Activity.RESULT_OK;

/**
 * Created by aderifaldi on 24/08/2016.
 */
@SuppressLint("ValidFragment")
public class ProfileFragment extends BaseFragment {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String FILE_EXTENTION = ".png";
    private static final String FILE_NAME = "event_image";

    private static final int RESULT_LOAD_IMAGE_FROM_CAMERA = 10;
    private static final int RESULT_LOAD_IMAGE_FROM_GALLERY = 11;

    @BindView(R.id.imageUser)
    CircleImageView imageUser;
    @BindView(R.id.follower)
    CustomTextView follower;
    @BindView(R.id.following)
    CustomTextView following;
    @BindView(R.id.nameUser)
    CustomTextView nameUser;
    @BindView(R.id.username)
    CustomTextView username;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.txtActivities)
    CustomTextView txtActivities;
    @BindView(R.id.txtEvents)
    CustomTextView txtEvents;
    @BindView(R.id.pagerProfile)
    ViewPager pagerProfile;

    private View view;

    private CharSequence pagerTitle[] = {"", ""};
    private int numbOfTabs;
    private ProfilePagerAdapter adapter;
    private MyActivitiesFragment activities;
    private MyEventsFragment events;

    private File fileUploadProfile = null;
    private File fileTempUploadProfile = null;
    private SimpleDateFormat dateFormat;
    private Calendar calendar;
    private String fileUploadUrlProfile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

        numbOfTabs = pagerTitle.length;

        activities = new MyActivitiesFragment();
        events = new MyEventsFragment();

        adapter = new ProfilePagerAdapter(getFragmentManager(), numbOfTabs, activities, events,
                GlobalVariable.getUserId(getActivity()));

        pagerProfile.setAdapter(adapter);
        pagerProfile.setOffscreenPageLimit(numbOfTabs);
        tabLayout.setupWithViewPager(pagerProfile);

        pagerProfile.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    txtActivities.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                    txtEvents.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                }else {
                    txtActivities.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    txtEvents.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        registerForContextMenu(imageUser);
        GetMemberInfo();

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.upload_picture, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.camera) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
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
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            processImageFromCameraForProfile(requestCode, resultCode, data);
        } else if (requestCode == RESULT_LOAD_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            processImageFromGalleryForProfile(requestCode, resultCode, data);
        }
    }

    private void openCameraForProfile() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileTempUploadProfile = getCameraCaptureImageProfile();
        Uri imgUri = Uri.fromFile(fileTempUploadProfile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(cameraIntent, RESULT_LOAD_IMAGE_FROM_CAMERA);

    }

    private File getCameraCaptureImageProfile() {
        File outputDir = getActivity().getExternalCacheDir();
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
        if (fileTempUploadProfile != null && fileTempUploadProfile.exists()) {
            loadImage();
        } else {
            if (data != null && data.getExtras() != null && data.getExtras().containsKey("data") && data.getExtras().get("data") instanceof Bitmap) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                try {
                    fileTempUploadProfile = getCameraCaptureImageProfile();
                    FileOutputStream fos = new FileOutputStream(fileTempUploadProfile);
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
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            fileTempUploadProfile = new File(picturePath);

            loadImage();
        } else {
            ParcelFileDescriptor parcelFileDescriptor;
            Uri selectedImage = data.getData();
            try {
                parcelFileDescriptor = getActivity().getContentResolver().openFileDescriptor(selectedImage, "r");
                fileTempUploadProfile = getCameraCaptureImageProfile();
                CopyFile copyFile = new CopyFile(parcelFileDescriptor, fileTempUploadProfile.getAbsolutePath()) {
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
        fileUploadProfile = Compressor.getDefault(getActivity()).compressToFile(fileTempUploadProfile);
        Glide.with(this).
                load(fileUploadProfile).
                crossFade().
                into(imageUser);

        UploadImage();
    }

    private void UploadImage() {
        showLoading();

        Uri file = Uri.fromFile(new File(fileUploadProfile.getPath()));

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference riversRef = storageRef.child("profile_images/" + file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                dismissLoading();
                Toast.makeText(getActivity(), "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                dismissLoading();
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                fileUploadUrlProfile = downloadUrl.toString();
                GlobalVariable.saveIsProfileUpdated(getActivity(), true);
                GlobalVariable.saveUserProfileIMage(getActivity(), fileUploadUrlProfile);
                Log.d("TAG", "file path: " + downloadUrl);

                Toast.makeText(getActivity(), "Upload Success", Toast.LENGTH_SHORT).show();
                UpdateProfilePicture(fileUploadUrlProfile);

            }
        });

    }

    private void GetMemberInfo(){
        Call<BHUser> call = apiServiceBH.MyProfile(GlobalVariable.getUserId(getActivity()));
        call.enqueue(new Callback<BHUser>() {
            @Override
            public void onResponse(Call<BHUser> call, Response<BHUser> response) {
                BHUser user = response.body();
                if (!user.isError()){
                    nameUser.setText(user.data.user_name);
                    username.setText(user.data.user_screename);
                    follower.setText("" + user.data.followers_count);
                    following.setText("" + user.data.following_count);

                    Glide.with(getActivity())
                            .load(user.data.user_photo)
                            .crossFade()
                            .into(imageUser);
                }
            }

            @Override
            public void onFailure(Call<BHUser> call, Throwable t) {

            }
        });
    }

    private void UpdateProfilePicture(String photoUrl){
//        showLoading();
        jsonPost = new JsonObject();
        jsonPost.addProperty("user_photo", photoUrl);

        Call<ModelBase> call = apiServiceBH.UpdatePhoto(jsonPost, GlobalVariable.getUserId(getActivity()));
        call.enqueue(new Callback<ModelBase>() {
            @Override
            public void onResponse(Call<ModelBase> call, Response<ModelBase> response) {
                dismissLoading();
                if (!response.body().isError()){
                    AppUtility.logD("UpdateProfile", "Update profile photo success");
                    GetMemberInfo();
                }
            }

            @Override
            public void onFailure(Call<ModelBase> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        Glide.with(this).
//                load(GlobalVariable.getUserProfileImage(getActivity())).
//                crossFade().
//                into(imageUser);
//        GetMemberInfo();
    }

    @OnClick({R.id.menuActivities, R.id.menuEvents, R.id.imageUser})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuActivities:
                pagerProfile.setCurrentItem(0);
                txtActivities.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
                txtEvents.setTextColor(getActivity().getResources().getColor(R.color.colorAccent));
                break;
            case R.id.menuEvents:
                pagerProfile.setCurrentItem(1);
                txtActivities.setTextColor(getActivity().getResources().getColor(R.color.colorAccent));
                txtEvents.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
                break;

            case R.id.imageUser:
                getActivity().openContextMenu(imageUser);
                break;
        }
    }
}
