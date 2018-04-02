package com.monitorfree.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.monitorfree.MyApp;
import com.monitorfree.R;
import com.monitorfree.RequestModel.UserRequests;
import com.monitorfree.UserModel.User;
import com.monitorfree.Util.GlobalKeys;
import com.monitorfree.databinding.ActivitySignUpBinding;

import java.lang.reflect.Field;

import javax.inject.Inject;


public class SignUp extends AppCompatActivity implements CallBackSuccess {

    @Inject
    MyApp myApp;
//
//    @Inject
//    User user;

//    @Inject
    UserRequests userRequests = new UserRequests();

    ActivitySignUpBinding binding;

    CallBackSuccess callBackSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
//        MyApp.component().inject(this);

        callBackSuccess = this;

        Field f = null;
        try {
            f =TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(binding.etName, R.drawable.cursor);
            f.set(binding.etEmail, R.drawable.cursor);
            f.set(binding.etPassword, R.drawable.cursor);
            f.set(binding.etConfirmPassword, R.drawable.cursor);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void clickSignUp(View view) {

        switch (view.getId()) {
            case R.id.tvLogin:
                MyApp.instance.go(SignUp.this, Login.class);
                break;

            case R.id.btnRegister:

                if (binding.etName.getText().toString() == null || binding.etName.getText().equals("")) {
                    binding.etName.setError("Please Enter Name");
                } else if (binding.etEmail.getText().toString() == null || binding.etEmail.getText().equals("")) {
                    binding.etEmail.setError("Please Enter Email");
                } else if (MyApp.instance.isValidEmail(binding.etEmail.getText().toString()) == false) {
                    binding.etEmail.setError("Invalid Email");
                } else if (binding.etPassword.getText().toString() == null || binding.etPassword.getText().equals("")) {
                    binding.etPassword.setError("Please Enter Password");
                } else if (binding.etPassword.getText().length() < 8) {
                    binding.etPassword.setError("Password must be 8-20 chars");
                } else if (binding.etConfirmPassword.getText().toString() == null || binding.etConfirmPassword.getText().toString().equals("")) {
                    binding.etConfirmPassword.setError("Please Enter Confirm Password");
                } else if (!binding.etPassword.getText().toString().equals(binding.etConfirmPassword.getText().toString())) {
                    binding.etPassword.setError("Password don't match.");
                } else {
                    userRequests.funSignUp(MyApp.instance, binding, SignUp.this, callBackSuccess);
                }

                break;
        }
    }

    @Override
    public void success(Object object) {
        finish();
    }
}
