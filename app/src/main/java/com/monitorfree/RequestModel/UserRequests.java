package com.monitorfree.RequestModel;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.monitorfree.Activities.CallBackSuccess;
import com.monitorfree.Activities.Login;
import com.monitorfree.Activities.Main2Activity;
import com.monitorfree.Activities.MonitorInfo;
import com.monitorfree.Activities.SignUp;
import com.monitorfree.BackgroundService.DemoService;
import com.monitorfree.MyApp;
import com.monitorfree.Networking.iRepository;
import com.monitorfree.R;
import com.monitorfree.UserModel.AddMonitor;
import com.monitorfree.UserModel.MonitorStatus;
import com.monitorfree.UserModel.User;
import com.monitorfree.Util.GlobalKeys;
import com.monitorfree.Util.MonitorUtil;
import com.monitorfree.databinding.ActivityForgotPasswordBinding;
import com.monitorfree.databinding.ActivityLoginBinding;
import com.monitorfree.databinding.ActivitySaveMonitorBinding;
import com.monitorfree.databinding.ActivitySignUpBinding;
import com.monitorfree.databinding.FragmentHomeBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jaspreet on 11/22/17.
 */

public class UserRequests implements GlobalKeys {

    public void funSignUp(MyApp boloTalk, ActivitySignUpBinding binding, Context classContext, CallBackSuccess callBackSuccess) {

        boloTalk.showProgress(classContext);
        Call<RootLogin> book = boloTalk.getRetrofitService().signUp(binding.etName.getText().toString(), binding.etPassword.getText().toString(), binding.etEmail.getText().toString(), "Uqj7RG6WqNfCqxvht8MhnkFSv", "f3bd2eda5604b98fdf287f55e789f7ec", "0", "", "");
        book.enqueue(new Callback<RootLogin>() {
            @Override
            public void onResponse(Call<RootLogin> call, Response<RootLogin> response) {

                boloTalk.closeProgress();
                RootLogin userLogin2 = response.body();
                int errorType = userLogin2.getStatus().getError();

                if (errorType == 0) {
                    Toast.makeText(boloTalk.getContext(), "Register Succesfully", Toast.LENGTH_SHORT).show();

                    callBackSuccess.success(userLogin2);

                } else if (errorType == 101) {
                    Toast.makeText(boloTalk.getContext(), userLogin2.getStatus().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(boloTalk.getContext(), userLogin2.getStatus().getMsg(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<RootLogin> call, Throwable t) {

                boloTalk.closeProgress();

                Toast.makeText(boloTalk.getContext(), "Email already exist", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void socialLogin(MyApp boloTalk, String socialId, String email, String fullname, String photoUrl, String accountType, Context classContext) {

        boloTalk.showProgress(classContext);
        Call<RootLogin> book = boloTalk.getRetrofitService().signUp(fullname, "", email, "Uqj7RG6WqNfCqxvht8MhnkFSv", "f3bd2eda5604b98fdf287f55e789f7ec", accountType, socialId, photoUrl);
        book.enqueue(new Callback<RootLogin>() {
            @Override
            public void onResponse(Call<RootLogin> call, Response<RootLogin> response) {

                boloTalk.closeProgress();
                RootLogin userLogin2 = response.body();
                int errorType = userLogin2.getStatus().getError();

                if (errorType == 0) {

                    boloTalk.setKey(USER_EMAIL, userLogin2.getData().getUser().getEmail());
                    boloTalk.setKey(USER_NAME, userLogin2.getData().getUser().getFullName());
                    boloTalk.setKey(USER_HASH, userLogin2.getData().getUser().getHash());
                    boloTalk.setKey(USER_ID, userLogin2.getData().getUser().getId());
                    boloTalk.setKey(ACCOUNT_TYPE, userLogin2.getData().getUser().getAccountType());
                    boloTalk.setKey(PHOTO_URL, userLogin2.getData().getUser().getExternalPhotoUrl());

                    boloTalk.isFirstLogin = true;

                    Intent intent = new Intent(boloTalk.getContext(), Main2Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    classContext.startActivity(intent);

                } else if (errorType == 101) {
                    Toast.makeText(boloTalk.getContext(), userLogin2.getStatus().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(boloTalk.getContext(), userLogin2.getStatus().getMsg(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<RootLogin> call, Throwable t) {

                boloTalk.closeProgress();
            }
        });
    }


    public void funAddMonitor(MyApp boloTalk, AddMonitor addMonitor, ActivitySaveMonitorBinding binding, Context classContext, CallBackSuccess callBackSuccess) {

        if (addMonitor.getName() == null || addMonitor.getName().equals("")) {
            binding.edtFriendlyName.setError("Please Enter Name");
        } else if (addMonitor.getAddress() == null || addMonitor.getAddress().equals("")) {
            binding.edtWebAddress.setError("Please Enter Web Address");
        } else {

            boloTalk.showProgress(classContext);
            Call<RootRegister> book = boloTalk.getRetrofitService().addMonitor(addMonitor.getStartDate(), addMonitor.getName(), addMonitor.getAddress(), String.valueOf(addMonitor.getInterval()), String.valueOf(addMonitor.getType()), addMonitor.getPort(), addMonitor.getKeywords(), addMonitor.getMobileDateTime(), "f3bd2eda5604b98fdf287f55e789f7ec", "Uqj7RG6WqNfCqxvht8MhnkFSv", boloTalk.getKey(USER_HASH));
            book.enqueue(new Callback<RootRegister>() {
                @Override
                public void onResponse(Call<RootRegister> call, Response<RootRegister> response) {

                    boloTalk.closeProgress();
                    RootRegister userLogin2 = response.body();
                    int errorType = userLogin2.getStatus().getError();

                    if (errorType == 0) {

                        addMonitor.setId(userLogin2.getMonitorId());
                        addMonitor.setActive("1");
                        getMonitor(addMonitor);

                        boloTalk.globalMonitorList.add(addMonitor);
                        boloTalk.globalBGMonitorList.add(addMonitor);

                        Bundle myExtrasBundle = new Bundle();
                        myExtrasBundle.putString("job_monitorID", addMonitor.getId());
                        myExtrasBundle.putString("job_monitorName", addMonitor.getName());
                        myExtrasBundle.putString("job_address", addMonitor.getAddress());
                        myExtrasBundle.putString("job_keywords", addMonitor.getKeywords());
                        myExtrasBundle.putString("job_port", addMonitor.getPort());
                        myExtrasBundle.putString("job_type", addMonitor.getType());
                        myExtrasBundle.putString("job_active", addMonitor.getActive());

                        myExtrasBundle.putString("job_interval", addMonitor.getInterval());
                        myExtrasBundle.putString("job_startDate", addMonitor.getStartDate());

                        int interval_time = Integer.valueOf(addMonitor.getInterval());

                        final Job.Builder builder =
                                boloTalk.jobDispatcher
                                        .newJobBuilder()
                                        .setTag(addMonitor.getId())
                                        .setLifetime(Lifetime.FOREVER)
                                        .setService(DemoService.class)
                                        .setTrigger(Trigger.executionWindow(60, 60 * interval_time))
                                        .setRecurring(true)
//                                        .setReplaceCurrent(false)
//                                        .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                                        .setConstraints(
                                                // only run on an unmetered network
                                                Constraint.ON_ANY_NETWORK
                                        );

                        builder.setExtras(myExtrasBundle);

                        boloTalk.jobDispatcher.mustSchedule(builder.build());

                        callBackSuccess.success(userLogin2);
                    }
                    else if (errorType == 202)
                    {
                        Toast.makeText(boloTalk.getContext(), ""+userLogin2.getStatus().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<RootRegister> call, Throwable t) {
                    boloTalk.closeProgress();
                    Snackbar mSnackbar = Snackbar.make(binding.btnSaveMonitor, "Unable to connect", Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    funAddMonitor(boloTalk, addMonitor, binding, classContext, callBackSuccess);
                                }
                            });
                    mSnackbar.setActionTextColor(Color.RED).show();
                }
            });

        }
    }

    public void funSendStatus(String monitorId, MyApp myApp, String status, String mobileDateTime, String deviceName, String ipAddress, String previousStatus, String monitorName, String address, String keywords, String port, String type, String interval, String startDate, String speedStatus) {

        String key = "Uqj7RG6WqNfCqxvht8MhnkFSv";
        String sig = "f3bd2eda5604b98fdf287f55e789f7ec";
        String hash = myApp.getKey(USER_HASH);
        Log.d("hash-key", hash);

        Call<RootRegister> book = myApp.getRetrofitService().sendStatus(sig, key, hash, monitorId, status, mobileDateTime, deviceName, ipAddress);
        book.enqueue(new Callback<RootRegister>() {
            @Override
            public void onResponse(Call<RootRegister> call, Response<RootRegister> response) {

                RootRegister userLogin2 = response.body();
                int errorType = userLogin2.getStatus().getError();
                if (errorType == 0) {
                    //   Toast.makeText(myApp.getContext(), "Monitor added Succesfully", Toast.LENGTH_SHORT).show();
                    Log.d("monID", monitorId);
                    Log.d("monIDD","m "+ monitorId);

                    myApp.setKey(String.valueOf(monitorId), status);

                    String speedString = (speedStatus.equals("")) ? "" : "[" + speedStatus + "] ";

                    if (previousStatus.equals("2") && status.equals("1")) {         //set new status
                        startNotification(speedString + "monitor " + monitorName + " is back Online", monitorId, monitorName, address, keywords, port, type, interval, startDate, "1");
                        myApp.setKey(String.valueOf(monitorId), "1");
                    } else if (previousStatus.equals("1") && status.equals("2")) {         //set new status
                        startNotification(speedString + "monitor " + monitorName + " is Offline", monitorId, monitorName, address, keywords, port, type, interval, startDate, "2");
                        myApp.setKey(String.valueOf(monitorId), "2");
                    }
                }
            }

            @Override
            public void onFailure(Call<RootRegister> call, Throwable t) {
                Log.d("Monitor added fail", monitorId);
            }

        });
    }


    public void funLogin(MyApp boloTalk, ActivityLoginBinding binding, Context classContext) {

        boloTalk.showProgress(classContext);
        Call<RootLogin> book = boloTalk.getRetrofitService().login(binding.etPassword.getText().toString(), binding.etEmail.getText().toString(), "Uqj7RG6WqNfCqxvht8MhnkFSv", "f3bd2eda5604b98fdf287f55e789f7ec");
        book.enqueue(new Callback<RootLogin>() {
            @Override
            public void onResponse(Call<RootLogin> call, Response<RootLogin> response) {

                boloTalk.closeProgress();
                RootLogin userLogin2 = response.body();
                int errorType = userLogin2.getStatus().getError();

                if (errorType == 0) {
                    boloTalk.setKey(USER_EMAIL, userLogin2.getData().getUser().getEmail());
                    boloTalk.setKey(USER_NAME, userLogin2.getData().getUser().getFullName());
                    boloTalk.setKey(USER_PASSWORD, userLogin2.getData().getUser().getPassword());
                    boloTalk.setKey(USER_HASH, userLogin2.getData().getUser().getHash());
                    boloTalk.setKey(USER_ID, userLogin2.getData().getUser().getId());
                    boloTalk.setKey(ACCOUNT_TYPE, userLogin2.getData().getUser().getAccountType());
                    boloTalk.setKey(PHOTO_URL, "");

                    boloTalk.isFirstLogin = true;

                    Intent intent = new Intent(boloTalk.getContext(), Main2Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    classContext.startActivity(intent);

                } else if (errorType == 102) {
                    AlertDialog.Builder newDialog = new AlertDialog.Builder(classContext);
                    newDialog.setTitle("Error ?");
                    newDialog.setMessage(userLogin2.getStatus().getMsg());
                    newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    newDialog.show();
                } else {
                    Toast.makeText(boloTalk.getContext(), userLogin2.getStatus().getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<RootLogin> call, Throwable t) {
                boloTalk.closeProgress();
                AlertDialog.Builder newDialog = new AlertDialog.Builder(classContext);
                newDialog.setTitle("Error ?");
                newDialog.setMessage("User don't exist");
                newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                newDialog.show();
            }
        });
    }


    public void funForgotPass(MyApp boloTalk, User user, ActivityForgotPasswordBinding binding, Context classContext) {

        if (user.getEmail() == null || user.getEmail().equals("")) {
            binding.etEmail.setError("Please Enter Email");
        } else if (boloTalk.isValidEmail(user.getEmail().toString()) == false) {
            binding.etEmail.setError("Invalid Email");
        } else {

            boloTalk.setKey(USER_EMAIL, user.getEmail());
            boloTalk.setKey(USER_NAME, user.getFullName());
            boloTalk.setKey(USER_PASSWORD, user.getPassword());

            user.setKey("Uqj7RG6WqNfCqxvht8MhnkFSv");
            user.setSig("f3bd2eda5604b98fdf287f55e789f7ec");

            boloTalk.showProgress(classContext);
            Call<RootRegister> book = boloTalk.getRetrofitService().forgotPassword(user.getEmail(), user.getKey(), user.getSig());
            book.enqueue(new Callback<RootRegister>() {
                @Override
                public void onResponse(Call<RootRegister> call, Response<RootRegister> response) {

                    boloTalk.closeProgress();
                    RootRegister userLogin2 = response.body();
                    int errorType = userLogin2.getStatus().getError();

                    if (errorType == 0) {
                        Toast.makeText(boloTalk.getContext(), userLogin2.getStatus().getMsg(), Toast.LENGTH_SHORT).show();

                    } else if (errorType == 106) {
                        Toast.makeText(boloTalk.getContext(), userLogin2.getStatus().getMsg(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<RootRegister> call, Throwable t) {
                    boloTalk.closeProgress();
                    Snackbar mSnackbar = Snackbar.make(binding.etEmail, "Unable to connect", Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    funForgotPass(boloTalk, user, binding, classContext);
                                }
                            });
                    mSnackbar.setActionTextColor(Color.RED).show();
                }
            });

        }
    }


    public void getMonitor(MyApp boloTalk, FragmentHomeBinding binding, Context classContext, CallBackSuccess callBackSuccess) {

        String key = "Uqj7RG6WqNfCqxvht8MhnkFSv";
        String sig = "f3bd2eda5604b98fdf287f55e789f7ec";
        String hash = boloTalk.getKey(USER_HASH);

        boloTalk.showProgress(classContext);
        Call<RootMonitorList> book = boloTalk.getRetrofitService().getMonitorList(sig, key, hash);
        book.enqueue(new Callback<RootMonitorList>() {
            @Override
            public void onResponse(Call<RootMonitorList> call, Response<RootMonitorList> response) {

                boloTalk.closeProgress();
                RootMonitorList userLogin2 = response.body();
                int errorType = userLogin2.getStatus().getError();

                if (errorType == 0) {
                    callBackSuccess.success(userLogin2);

                } else if (errorType == 102) {
                    Toast.makeText(boloTalk.getContext(), userLogin2.getStatus().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(boloTalk.getContext(), userLogin2.getStatus().getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<RootMonitorList> call, Throwable t) {
                boloTalk.closeProgress();
//                Snackbar mSnackbar = Snackbar.make(binding.ivDown, "Unable to connect", Snackbar.LENGTH_LONG)
//                        .setAction("RETRY", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                getMonitor(boloTalk, binding, classContext, callBackSuccess);
//                            }
//                        });
//                mSnackbar.setActionTextColor(Color.RED).show();
            }
        });

    }

    public void sendMonitorDelete(MyApp boloTalk, String monitorId, Context classContext, CallBackSuccess callBackSuccess) {

        String key = "Uqj7RG6WqNfCqxvht8MhnkFSv";
        String sig = "f3bd2eda5604b98fdf287f55e789f7ec";
        String hash = boloTalk.getKey(USER_HASH);

        boloTalk.showProgress(classContext);
        Call<RootRegister> book = boloTalk.getRetrofitService().sendMonitorDelete(sig, key, hash, monitorId);
        book.enqueue(new Callback<RootRegister>() {
            @Override
            public void onResponse(Call<RootRegister> call, Response<RootRegister> response) {

                boloTalk.closeProgress();
                RootRegister userLogin2 = response.body();
                int errorType = userLogin2.getStatus().getError();

                if (errorType == 0) {
                    callBackSuccess.success(null);
                } else if (errorType == 102) {
                    Toast.makeText(boloTalk.getContext(), userLogin2.getStatus().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(boloTalk.getContext(), userLogin2.getStatus().getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<RootRegister> call, Throwable t) {
                boloTalk.closeProgress();
            }
        });

    }

    public void sendMonitorPause(MyApp boloTalk, String monitorId, int monitorStatus, String monitor_name, String address, String keywords, String port, String type, String invterval_time, String startDate, Context classContext, CallBackSuccess callBackSuccess) {

        String key = "Uqj7RG6WqNfCqxvht8MhnkFSv";
        String sig = "f3bd2eda5604b98fdf287f55e789f7ec";
        String hash = boloTalk.getKey(USER_HASH);

        boloTalk.showProgress(classContext);
        Call<RootRegister> book = boloTalk.getRetrofitService().sendMonitorPause(sig, key, hash, monitorId);
        book.enqueue(new Callback<RootRegister>() {
            @Override
            public void onResponse(Call<RootRegister> call, Response<RootRegister> response) {

                boloTalk.closeProgress();
                RootRegister userLogin2 = response.body();
                int errorType = userLogin2.getStatus().getError();

                if (errorType == 0) {
                    if (monitorStatus == -1) {

                        Bundle myExtrasBundle = new Bundle();
                        myExtrasBundle.putString("job_monitorID", monitorId);
                        myExtrasBundle.putString("job_monitorName", monitor_name);
                        myExtrasBundle.putString("job_address", address);
                        myExtrasBundle.putString("job_keywords", keywords);
                        myExtrasBundle.putString("job_port", port);
                        myExtrasBundle.putString("job_type", type);
                        myExtrasBundle.putString("job_active", "1");

                        myExtrasBundle.putString("interval", invterval_time);
                        myExtrasBundle.putString("startDate", startDate);

                        int intervalTime = Integer.valueOf(invterval_time);

                        final Job.Builder builder =
                                boloTalk.jobDispatcher
                                        .newJobBuilder()
                                        .setTag(monitorId)
                                        .setLifetime(Lifetime.FOREVER)
                                        .setService(DemoService.class)
                                        .setTrigger(Trigger.executionWindow(60, 60 * intervalTime))
                                        .setRecurring(true)
//                                        .setReplaceCurrent(false)
//                                        .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                                        .setConstraints(
                                                // only run on an unmetered network
                                                Constraint.ON_ANY_NETWORK
                                        );

                        builder.setExtras(myExtrasBundle);

                        boloTalk.jobDispatcher.mustSchedule(builder.build());

                    }
                    callBackSuccess.success(null);

                } else if (errorType == 102) {
                    Toast.makeText(boloTalk.getContext(), userLogin2.getStatus().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(boloTalk.getContext(), userLogin2.getStatus().getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<RootRegister> call, Throwable t) {
                boloTalk.closeProgress();
            }
        });

    }

    public void getStatusList(MyApp boloTalk, String monitorId, String timeFrame, Context classContext, CallBackSuccess callBackSuccess) {

        String key = "Uqj7RG6WqNfCqxvht8MhnkFSv";
        String sig = "f3bd2eda5604b98fdf287f55e789f7ec";
        String hash = boloTalk.getKey(USER_HASH);

        boloTalk.showProgress(classContext);
        Call<RootMonitorStatus> book = boloTalk.getRetrofitService().getStatusList(sig, key, hash, monitorId, timeFrame);
        book.enqueue(new Callback<RootMonitorStatus>() {
            @Override
            public void onResponse(Call<RootMonitorStatus> call, Response<RootMonitorStatus> response) {

                boloTalk.closeProgress();
                RootMonitorStatus userLogin2 = response.body();
                int errorType = userLogin2.getStatus().getError();

                if (errorType == 0) {
                    callBackSuccess.success(userLogin2);
                } else if (errorType == 102) {
                    Toast.makeText(boloTalk.getContext(), userLogin2.getStatus().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(boloTalk.getContext(), userLogin2.getStatus().getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<RootMonitorStatus> call, Throwable t) {
                boloTalk.closeProgress();
            }
        });

    }

    public void sendAccountDelete(MyApp boloTalk, Context classContext, CallBackSuccess callBackSuccess) {

        String key = "Uqj7RG6WqNfCqxvht8MhnkFSv";
        String sig = "f3bd2eda5604b98fdf287f55e789f7ec";
        String hash = boloTalk.getKey(USER_HASH);

        boloTalk.showProgress(classContext);
        Call<Status> book = boloTalk.getRetrofitService().accountDelete(key, sig, hash);
        book.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {

                boloTalk.closeProgress();
                Status userLogin2 = response.body();
                int errorType = userLogin2.getError();

                if (errorType == 0) {
                    callBackSuccess.success(userLogin2);
                } else if (errorType == 102) {
                    Toast.makeText(boloTalk.getContext(), userLogin2.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(boloTalk.getContext(), userLogin2.getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

                boloTalk.closeProgress();

                callBackSuccess.success(null);
            }
        });

    }

    private void getMonitor(AddMonitor newMonitor) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String mobileDateTime = df1.format(c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String monitorDate = newMonitor.getStartDate().split(" ")[0];
        Date strDate = null;
        try {
            strDate = df.parse(monitorDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String deviceName = Build.MODEL;
        String ipAddress = MonitorUtil.getIPAddress();

        if (newMonitor.getType().equals("1")) {    //Http monitor

            if (System.currentTimeMillis() >= strDate.getTime())
            {
                String strMonitor_id = String.valueOf(newMonitor.getId());
                boolean isGet = MonitorUtil.isHttpConnection(newMonitor.getAddress(), "1", "");

                if (isGet) {
                    Log.d("Http monitor", "true");
                    funSendStatus(strMonitor_id, MyApp.instance, "1", mobileDateTime, deviceName, ipAddress, "", "", "", "", "", "", "", "", "");
                } else {
                    Log.d("Http monitor", "false");

                    isGet = MonitorUtil.isHttpConnection(newMonitor.getAddress(), "1", "");

                    if (isGet) {

                        funSendStatus(strMonitor_id, MyApp.instance, "1", mobileDateTime, deviceName, ipAddress, "", "", "", "", "", "", "", "", "");
                    } else {
                        funSendStatus(strMonitor_id, MyApp.instance, "2", mobileDateTime, deviceName, ipAddress, "", "", "", "", "", "", "", "", "");
                    }
                }
            }
            else {
                Log.d("Http monitor", "No still start");
            }

        } else if (newMonitor.getType().equals("2")) {     //Ping monitor

            if (System.currentTimeMillis() >= strDate.getTime()) {

                String strMonitor_id = String.valueOf(newMonitor.getId());

                boolean isGet = MonitorUtil.isPingMonitor(newMonitor.getAddress());
                if (isGet) {
                    Log.d("Ping monitor", "true");
                    funSendStatus(strMonitor_id, MyApp.instance, "1", mobileDateTime, deviceName, ipAddress, "", "", "", "", "", "", "", "", "");
                } else {
                    Log.d("Ping monitor", "false");

                    isGet = MonitorUtil.isPingMonitor(newMonitor.getAddress());

                    if (isGet) {
                        funSendStatus(strMonitor_id, MyApp.instance, "1", mobileDateTime, deviceName, ipAddress, "", "", "", "", "", "", "", "", "");
                    } else {
                        funSendStatus(strMonitor_id, MyApp.instance, "2", mobileDateTime, deviceName, ipAddress, "", "", "", "", "", "", "", "", "");
                    }
                }
            }
            else {
                Log.d("Ping monitor", "No still start");
            }

        } else if (newMonitor.getType().equals("3")) {     //Keyword monitor

            if (System.currentTimeMillis() >= strDate.getTime()) {

                String search_keyword = newMonitor.getKeywords();
                String strMonitor_id = String.valueOf(newMonitor.getId());

                boolean isGetKeyword = MonitorUtil.isHttpConnection(newMonitor.getAddress(), "3", search_keyword);
                if (isGetKeyword) {
                    Log.d("Keyword monitor", "true");
                    funSendStatus(strMonitor_id, MyApp.instance, "1", mobileDateTime, deviceName, ipAddress, "", "", "", "", "", "", "", "", "");

                } else {
                    Log.d("Keyword monitor", "false");

                    isGetKeyword = MonitorUtil.isHttpConnection(newMonitor.getAddress(), "3", search_keyword);

                    if (isGetKeyword) {
                        funSendStatus(strMonitor_id, MyApp.instance, "1", mobileDateTime, deviceName, ipAddress, "", "", "", "", "", "", "", "", "");

                    } else {
                        funSendStatus(strMonitor_id, MyApp.instance, "2", mobileDateTime, deviceName, ipAddress, "", "", "", "", "", "", "", "", "");

                    }
                }
            }
            else {
                Log.d("Keyword monitor", "No still start");
            }

        } else if (newMonitor.getType().equals("4")) {     //Port monitor
            if (System.currentTimeMillis() >= strDate.getTime()) {

                String port = String.valueOf(newMonitor.getPort());
                String strMonitor_id = String.valueOf(newMonitor.getId());

                boolean isGet = MonitorUtil.isPortMonitor(newMonitor.getAddress(), port);
                if (isGet) {
                    Log.d("Port monitor", "true");
                    funSendStatus(strMonitor_id, MyApp.instance, "1", mobileDateTime, deviceName, ipAddress, "", "", "", "", "", "", "", "", "");

                } else {
                    Log.d("Port monitor", "false");

                    isGet = MonitorUtil.isPortMonitor(newMonitor.getAddress(), port);
                    if (isGet) {
                        funSendStatus(strMonitor_id, MyApp.instance, "1", mobileDateTime, deviceName, ipAddress, "", "", "", "", "", "", "", "", "");

                    } else {
                        funSendStatus(strMonitor_id, MyApp.instance, "2", mobileDateTime, deviceName, ipAddress, "", "", "", "", "", "", "", "", "");

                    }
                }
            }
            else {
                Log.d("Port monitor", "No still start");
            }
        }
    }

    public void startNotification(String message, String monitorID, String monitorName, String address, String keywords, String port, String type, String interval, String startDate, String status) {

        Intent notificationIntent = new Intent(MyApp.instance.getContext(), MonitorInfo.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("notification", true);
        bundle.putString("noti_name", monitorName);
        bundle.putString("noti_id", monitorID);

        bundle.putString("noti_startDate", startDate);
        bundle.putString("noti_status", status);
        bundle.putString("noti_active", "1");

        bundle.putString("noti_interval", interval);
        bundle.putString("noti_address", address);
        bundle.putString("noti_type", type);
        bundle.putString("noti_keywords", keywords);
        bundle.putString("noti_port", port);

        notificationIntent.putExtras(bundle);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(MyApp.instance.getContext(), Integer.valueOf(monitorID), notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
        NotificationManager notificationManager =
                (NotificationManager) MyApp.instance.getContext().getSystemService(MyApp.instance.getContext().NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Notification notification = new NotificationCompat.Builder(MyApp.instance.getContext(), NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Monitorfree")
                .setContentText(message)
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .build();

        notificationManager.notify(Integer.valueOf(monitorID), notification);

    }
}



