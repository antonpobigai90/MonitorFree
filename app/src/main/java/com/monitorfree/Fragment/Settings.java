package com.monitorfree.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.monitorfree.Activities.CallBackSuccess;
import com.monitorfree.Activities.Login;
import com.monitorfree.Activities.Main2Activity;
import com.monitorfree.Activities.MonitorInfo;
import com.monitorfree.MyApp;
import com.monitorfree.R;
import com.monitorfree.RequestModel.UserRequests;
import com.monitorfree.databinding.FragmentSettingsBinding;

import javax.inject.Inject;

import static com.monitorfree.Util.GlobalKeys.USER_EMAIL;


/**
 * Created by jassi on 30-07-2016.
 */

public class Settings extends Fragment implements CallBackSuccess {

    UserRequests userRequests = new UserRequests();

    FragmentSettingsBinding binding;

    CallBackSuccess callBackSuccess;

    Main2Activity parent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);

        callBackSuccess = this;

        binding.txtemail.setText(MyApp.instance.getKey(USER_EMAIL));

        binding.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog("Delete ?", "Do you want to delete this account?");
            }
        });

        return binding.getRoot();
    }

    private void alertDialog(String title, String message) {
        AlertDialog.Builder newDialog = new AlertDialog.Builder(getContext());
        newDialog.setTitle(title);
        newDialog.setMessage(message);
        newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                MyApp.instance.jobDispatcher.cancelAll();

                userRequests.sendAccountDelete(MyApp.instance, getContext(), callBackSuccess);
            }
        });
        newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        newDialog.show();
    }

    @Override
    public void success(Object object) {
        Log.d("delete ---", "account");

        MyApp.instance.logOut();

        Intent intent = new Intent(getContext(), Login.class);
        startActivity(intent);
    }
}
