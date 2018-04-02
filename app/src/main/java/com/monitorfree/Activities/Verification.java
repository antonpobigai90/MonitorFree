package com.monitorfree.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.monitorfree.MyApp;
import com.monitorfree.R;
import com.monitorfree.Util.GlobalKeys;
import com.monitorfree.databinding.ActivityVerificationBinding;

import javax.inject.Inject;


public class Verification extends AppCompatActivity implements GlobalKeys {

    ActivityVerificationBinding binding;

    @Inject
    MyApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verification);
        MyApp.component().inject(this);


        RxTextView.textChangeEvents(binding.etEmail)
                .subscribe(e -> {

                    String data = e.text().toString();

                    if (data == null || data.equals("")) {


                    } else {

                        if (data.equals("1234")) {

                            myApp.setKey(IS_VERIFIED, "true");

                            Intent intent = new Intent(myApp.getContext(), Main2Activity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            binding.etEmail.setError("Wrong code");
                        }
                    }
                });

    }


}
