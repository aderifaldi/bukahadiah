package com.playground.bukahadiah.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.playground.bukahadiah.R;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.model.bukalapak.BLRegister;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.pageTitle)
    CustomTextView pageTitle;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.register)
    RelativeLayout register;
    @BindView(R.id.edtName)
    EditText edtName;
    @BindView(R.id.tilName)
    TextInputLayout tilName;
    @BindView(R.id.edtUsername)
    EditText edtUsername;
    @BindView(R.id.tilUsername)
    TextInputLayout tilUsername;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword;
    @BindView(R.id.edtPasswordConfirm)
    EditText edtPasswordConfirm;
    @BindView(R.id.tilPasswordConfirm)
    TextInputLayout tilPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        pageTitle.setText("Register to");

        edtName.setTypeface(tfRegular);
        edtUsername.setTypeface(tfRegular);
        edtEmail.setTypeface(tfRegular);
        edtPassword.setTypeface(tfRegular);
        edtPasswordConfirm.setTypeface(tfRegular);

        tilName.setTypeface(tfLight);
        tilUsername.setTypeface(tfLight);
        tilEmail.setTypeface(tfLight);
        tilPassword.setTypeface(tfLight);
        tilPasswordConfirm.setTypeface(tfLight);

    }

    @OnClick({R.id.back, R.id.register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                exitByBackByPresses();
                break;
            case R.id.register:

                String name = edtName.getText().toString();
                String username = edtUsername.getText().toString();
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                String confirmPassword = edtPasswordConfirm.getText().toString();

                if (name == "" || name.equals("")){
                    edtName.setError("Name still empty");
                }else if (username == "" || username.equals("")){
                    edtUsername.setError("Username still empty");
                }else if (email == "" || email.equals("")){
                    edtEmail.setError("Email still empty");
                }else if (password == "" || password.equals("")){
                    edtPassword.setError("Password still empty");
                }else if (confirmPassword == "" || confirmPassword.equals("")){
                    edtPasswordConfirm.setError("Confirm Password still empty");
                }else {
                    if (password == confirmPassword || password.equals(confirmPassword)){

                        jsonPost = new JsonObject();
                        jsonPost.addProperty("email", email);
                        jsonPost.addProperty("username", username);
                        jsonPost.addProperty("name", name);
                        jsonPost.addProperty("password", password);
                        jsonPost.addProperty("password_confirmation", confirmPassword);
                        jsonPost.addProperty("policy", "1");

                        showLoading();
                        Call<BLRegister> call = apiServiceBL.RegisterUser(jsonPost);
                        call.enqueue(new Callback<BLRegister>() {
                            @Override
                            public void onResponse(Call<BLRegister> call, Response<BLRegister> response) {
                                dismissLoading();

                                BLRegister apiResponse = response.body();

                                if (apiResponse.status.equals("OK")){
                                    startActivity(new Intent(getApplicationContext(), RegisterSuccessActivity.class)
                                            .putExtra("message", apiResponse.message));
                                    goToAnimation();
                                    finish();
                                }else {
                                    Toast.makeText(RegisterActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<BLRegister> call, Throwable t) {

                            }
                        });

                    }else {
                        Toast.makeText(this, "Confirm password not match", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }
}
