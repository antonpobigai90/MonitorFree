package com.monitorfree.Activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job.Builder;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.monitorfree.BackgroundService.DemoService;
import com.monitorfree.BackgroundService.SensorRestarterBroadcastReceiver;
import com.monitorfree.Fragment.*;
import com.monitorfree.Fragment.AddMonitor;
import com.monitorfree.MyApp;
import com.monitorfree.R;
import com.monitorfree.UserModel.*;
import com.monitorfree.Util.GlobalKeys;
import com.monitorfree.databinding.ActivityMain2Binding;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.monitorfree.Util.GlobalKeys.ACCOUNT_TYPE;

public class Main2Activity extends AppCompatActivity {

    static Fragment fragment = null;
    static Class fragmentClass = null;
    FragmentManager fm;
    DrawerLayout drawer;
    private boolean doubleBackToExitPressedOnce;

    TextView tvName, tvEmail;
    CircleImageView imgPhoto;

    @Inject
    MyApp myApp;

    ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        MyApp.component().inject(this);

        if (!myApp.isConnectingToInternet()) {
            return;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setTitle("Dashboard");
        fragmentClass = Home.class;
        replaceFragment();

        tvName = (TextView) findViewById(R.id.tvName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);

        tvName.setText(myApp.getKey(GlobalKeys.USER_NAME));
        tvEmail.setText(myApp.getKey(GlobalKeys.USER_EMAIL));

        imgPhoto = (CircleImageView) findViewById(R.id.imgVwUserProfile);

        String photo_url = myApp.getKey("PHOTO_URL");
        if (!photo_url.equals("")) {
            Picasso.get().load(photo_url).into(imgPhoto);
        }

    }

    public void onWizard(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.overlay);

        ImageView addMonitor = dialog.findViewById(R.id.addMonitor);
        addMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                setTitle("Add Monitor");
                fragmentClass = AddMonitor.class;
                replaceFragment();
            }
        });

//        for dismissing anywhere you touch
        LinearLayout masterView = dialog.findViewById(R.id.parentLayout);
        masterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void replaceFragment() {
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.relFragment, fragment)
                    .commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    public void clickMenu(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.txtVwLogout:
                showLogOutDialog();
                break;

            case R.id.profile:
                intent = new Intent(myApp.getContext(), MyProfile.class);
                startActivity(intent);
                break;

            case R.id.txtVwQ:
                setTitle("FAQs");
                fragmentClass = FAQs.class;
                replaceFragment();

                break;

            case R.id.addMonitor:
                myApp.isFirstLogin = false;

                setTitle("Add Monitor");
                fragmentClass = AddMonitor.class;
                replaceFragment();

                break;

            case R.id.tabHome:
                myApp.isFirstLogin = false;

                setTitle("Dashboard");
                fragmentClass = Home.class;
                replaceFragment();
                break;

            case R.id.tabMonitor:
                myApp.isFirstLogin = false;

                setTitle("Monitor");
                fragmentClass = Monitor.class;
                replaceFragment();
                break;

            case R.id.tabSettings:
                myApp.isFirstLogin = false;

                setTitle("Settings");
                fragmentClass = Settings.class;
                replaceFragment();

                break;
        }
    }

    void showLogOutDialog() {

        AlertDialog.Builder newDialog = new AlertDialog.Builder(Main2Activity.this);
        newDialog.setTitle("Logout ?");
        newDialog.setMessage("Logout (you will lose the current session)?");
        newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                myApp.isFirstLogin = false;
                //Cancel all jobschedule monitor
                myApp.jobDispatcher.cancelAll();

                String accountType = myApp.getKey(ACCOUNT_TYPE);

                if (accountType.equals("1")) {
                    //FB logout
                    LoginManager.getInstance().logOut();
                }

                myApp.logOut();

                Intent intent = new Intent(myApp.getContext(), Login.class);
                startActivity(intent);
                finish();
                // update profileInfo from here..
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragment instanceof Home) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }
                //
                this.doubleBackToExitPressedOnce = true;
                myApp.showSnackBar(getWindow().getDecorView(), "Please click back again to exit");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
                //
            } else {
                fragmentClass = Home.class;
                replaceFragment();
                setTitle("Streams");
            }
        }
        //.............................................//
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.add_monitor) {
            setTitle("Add Monitor");
            fragmentClass = AddMonitor.class;
            replaceFragment();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        Log.i("MAINACT", "onDestroy!");
    }

}