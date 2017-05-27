package com.playground.bukahadiah.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.playground.bukahadiah.R;
import com.playground.bukahadiah.helper.GlobalVariable;
import com.playground.bukahadiah.model.bukahadiah.ModelBase;
import com.playground.bukahadiah.model.bukalapak.BLAuthentication;
import com.playground.bukahadiah.model.bukalapak.BLUserProfile;
import com.radyalabs.async.Base64;
import com.radyalabs.irfan.util.AppUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edtUsername)
    EditText edtUserName;
    @BindView(R.id.tilUsername)
    TextInputLayout tilUsername;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        context = getApplicationContext();

        tilUsername.setTypeface(tfRegular);
        tilPassword.setTypeface(tfRegular);
        edtUserName.setTypeface(tfRegular);
        edtPassword.setTypeface(tfRegular);

    }

    @OnClick({R.id.btnLogin, R.id.btnRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:

//                loginSuccess();

                final String user = edtUserName.getText().toString();
                String pass = edtPassword.getText().toString();

                if (user == "" || user.equals("")){
                    edtUserName.setError("Username empty");
                }else if (pass == "" || pass.equals("")){
                    edtPassword.setError("Password empty");
                }else {
                    showLoading();
                    String auth = "Basic " + Base64.encodeToString((user + ":" + pass).getBytes(), Base64.NO_WRAP);
                    AppUtility.logD("Auth", auth);

                    Call<BLAuthentication> call = apiServiceBL.Login(auth);
                    call.enqueue(new Callback<BLAuthentication>() {
                        @Override
                        public void onResponse(Call<BLAuthentication> call, Response<BLAuthentication> response) {
                            BLAuthentication userBL = response.body();

                            if (userBL.status.equals("OK") && userBL.confirmed){

                                GlobalVariable.saveUser(context, userBL);
                                loginSuccess();

//                                Toast.makeText(LoginActivity.this, "Welcome " + userBL.user_name, Toast.LENGTH_SHORT).show();
                            } else if (userBL.status.equals("OK") && !userBL.confirmed){
                                Toast.makeText(LoginActivity.this, "Please confirm your registration " + userBL.user_name, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, userBL.message, Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<BLAuthentication> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                break;
            case R.id.btnRegister:

                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                goToAnimation();

                break;
        }
    }

    private void loginSuccess(){

        GetUserInfo();

    }

    private void goToMain(){
        GlobalVariable.saveIsLogin(context, true);
        startActivity(new Intent(this, MainActivity.class));
        goToAnimation();
        finish();
    }

    private void GetUserInfo(){

        final String userId = String.valueOf(GlobalVariable.getUserId(getApplicationContext()));
        String token = GlobalVariable.getBukalapakToken(getApplicationContext());

        if (userId != null){
            String auth = "Basic " + android.util.Base64.encodeToString((userId + ":" + token).getBytes(), android.util.Base64.NO_WRAP);

            Call<BLUserProfile> call = apiServiceBL.GetUserInfo(auth);
            call.enqueue(new Callback<BLUserProfile>() {
                @Override
                public void onResponse(Call<BLUserProfile> call, Response<BLUserProfile> response) {

                    BLUserProfile userBL = response.body();
                    BLUserProfile.User user = userBL.user;
                    if (userBL.status.equals("OK")){

//                        GlobalVariable.saveUserProfileIMage(getApplicationContext(), user.avatar);

                        jsonPost = new JsonObject();
                        jsonPost.addProperty("user_id", user.id);
                        jsonPost.addProperty("user_blid", user.id);
                        jsonPost.addProperty("user_name", user.name);
                        jsonPost.addProperty("user_photo", user.avatar);
                        jsonPost.addProperty("user_screename", user.username);
                        jsonPost.addProperty("fcm_token", GlobalVariable.getFCMToken(getApplicationContext()));

                        Call<ModelBase> call2 = apiServiceBH.AddMember(jsonPost);
                        call2.enqueue(new Callback<ModelBase>() {
                            @Override
                            public void onResponse(Call<ModelBase> call, Response<ModelBase> response) {
                                dismissLoading();
                                if (!response.body().isError()){
                                    goToMain();
                                }
                            }

                            @Override
                            public void onFailure(Call<ModelBase> call, Throwable t) {

                            }
                        });

                    }

                }

                @Override
                public void onFailure(Call<BLUserProfile> call, Throwable t) {

                }
            });
        }

    }


}
