package com.monitorfree.Activities;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.monitorfree.MyApp;
import com.monitorfree.R;
import com.monitorfree.RequestModel.UserRequests;
import com.monitorfree.UserModel.User;
import com.monitorfree.databinding.ActivityForgotPasswordBinding;

import java.lang.reflect.Field;

import javax.inject.Inject;

public class ForgotPassword extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;

    @Inject
    MyApp myApp;

    @Inject
    UserRequests userRequests;

    @Inject
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        MyApp.component().inject(this);

        binding.setUser(user);

        Field f = null;
        try {
            f =TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(binding.etEmail, R.drawable.cursor);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void clickForgot(View view) {

        switch (view.getId()) {

            case R.id.btnSend:
                userRequests.funForgotPass(myApp,user,binding,ForgotPassword.this);
                break;

            case R.id.txtSignup:
                myApp.go(ForgotPassword.this, SignUp.class);
                break;
        }
    }
}
