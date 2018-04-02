package com.monitorfree;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;

import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.monitorfree.Activities.Main2Activity;
import com.monitorfree.Activities.MonitorInfo;
import com.monitorfree.DI.DIComponants;
import com.monitorfree.Networking.iRepository;
import com.monitorfree.UserModel.AddMonitor;
import com.monitorfree.Util.GlobalKeys;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by BeingDeveloper on 20-08-2017.
 */

public class MyApp extends Application implements GlobalKeys{

    static Context context;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static ProgressDialog progressDialog;
    public static iRepository retrofitService;

    private static DIComponants graph;
    public static MyApp instance;
    public static int timerCount = 0;
    public static ArrayList<AddMonitor> globalMonitorList = new ArrayList<AddMonitor>();
    public static ArrayList<AddMonitor> globalBGMonitorList = new ArrayList<AddMonitor>();

    public MyApp() {
        Log.d("constuctor called", "called con");
    }

    public static GoogleApiClient mGoogleApiClient;

    public static boolean isFirstLogin;
    public static FirebaseJobDispatcher jobDispatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        context = this;
        isFirstLogin = false;

        buildComponentGraph();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        retrofitService = retrofit.create(iRepository.class);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        Log.d("App Start", "-------------------------");

        jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));

        //////////////////////////////////////
    }

    public static DIComponants component() {
        return graph;
    }

    public static void buildComponentGraph() {
        graph = DIComponants.Initializer.init(instance);
    }

    public Context getContext() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void logOut() {
        editor.clear();
        editor.commit();
    }


    public void setKey(String key, String value) {
        editor.putString(key, value);
        editor.commit();

    }

    public String getKey(String key) {
        return sharedPreferences.getString(key, null);
    }


    public void showSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }


    public void showProgress(Context context) {

        // progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);

        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

    }

    public void closeProgress() {

        progressDialog.dismiss();

    }

    public boolean funCheckNull(String string) {

        return string == null || string.equals("");
    }

    public void funHideKeyBoard(View view, Context context) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public iRepository getRetrofitService() {
        return retrofitService;
    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void go(Context context, Class object) {

        Intent intent = new Intent(context, object);
        context.startActivity(intent);
        intent = null;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

}


