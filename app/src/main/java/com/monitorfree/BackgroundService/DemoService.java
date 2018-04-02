package com.monitorfree.BackgroundService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.facebook.network.connectionclass.*;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.monitorfree.Activities.Login;
import com.monitorfree.Activities.Main2Activity;
import com.monitorfree.Activities.MonitorInfo;
import com.monitorfree.MyApp;
import com.monitorfree.R;
import com.monitorfree.RequestModel.UserRequests;
import com.monitorfree.Util.MonitorUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Dmitriy on 2/26/2018.
 */

public class DemoService extends JobService {

    private static final int NOTI_PRIMARY1 = 1100;
    private static final String TAG = "DemoService";

    @Inject
    MyApp myApp;

    @Inject
    UserRequests userRequests;

    String mobileDateTime;
    String deviceName;
    String ipAddress;

    private ConnectionClassManager mConnectionClassManager;
    private DeviceBandwidthSampler mDeviceBandwidthSampler;
    private ConnectionChangedListener mListener;

    private String mURL = "http://photoapp.expertsbuilder.com/upload/photo/FoSpnFpHDSxMYrMv.jpeg";
    private ConnectionQuality mConnectionClass = ConnectionQuality.UNKNOWN;

    private int mTries = 0;
    String strConnectSpeed;

    @Override
    public boolean onStartJob(@NonNull JobParameters job) {
        Log.d("DemoService", "true");

        MyApp.component().inject(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mConnectionClassManager = ConnectionClassManager.getInstance();
        mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
        mListener = new ConnectionChangedListener();
        mConnectionClassManager.register(mListener);

        Bundle extras = job.getExtras();
        assert extras != null;

        String monitorID = extras.getString("job_monitorID");
        String monitorName = extras.getString("job_monitorName");
        String address = extras.getString("job_address");
        String keywords = extras.getString("job_keywords");
        String port = extras.getString("job_port");
        String type = extras.getString("job_type");
        String active = extras.getString("job_active");

        String interval = extras.getString("job_interval");
        String startDate = extras.getString("job_startDate");

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mobileDateTime = df1.format(c.getTime());

        String previousStatus = myApp.getKey(monitorID);

        deviceName = Build.MODEL;
        ipAddress = MonitorUtil.getIPAddress();
        strConnectSpeed = "UNKNOWN";

        if (myApp.isConnectingToInternet()) {

            mTries = 0;

//            getConnectionSpeedCheck();
            checkNetworkQuality(type, active, monitorID, monitorName, address, keywords, port, type, interval, startDate, previousStatus);

//            if (type.equals("1")) {    //Http monitor
//
//                if (active.equals("1")) {
//
//                    boolean isGet = MonitorUtil.isHttpConnection(address, "1", "");
//
//                    if (isGet) {
//                        Log.d("Http monitor", "true");
//
//                        sendStatus("1", monitorID, monitorName, address, keywords, port, type, interval, startDate);
//                    } else {
//                        Log.d("Http monitor", "false");
//
//                        isGet = MonitorUtil.isHttpConnection(address, "1", "");
//                        if (isGet) {
//                            sendStatus("1", monitorID, monitorName, address, keywords, port, type, interval, startDate);
//                        } else {
//                            sendStatus("2", monitorID, monitorName, address, keywords, port, type, interval, startDate);
//                        }
//                    }
//                } else {
//                    Log.d("Http monitor", "No still start");
//                }
//
//            } else if (type.equals("2")) {     //Ping monitor
//
//                if (active.equals("1")) {
//
//                    boolean isGet = MonitorUtil.isPingMonitor(address);
//                    if (isGet) {
//                        Log.d("Ping monitor", "true");
//
//                        sendStatus("1", monitorID, monitorName, address, keywords, port, type, interval, startDate);
//                    } else {
//                        Log.d("Ping monitor", "false");
//
//                        isGet = MonitorUtil.isPingMonitor(address);
//
//                        if (isGet) {
//                            sendStatus("1", monitorID, monitorName, address, keywords, port, type, interval, startDate);
//                        } else {
//                            sendStatus("2", monitorID, monitorName, address, keywords, port, type, interval, startDate);
//                        }
//                    }
//                } else {
//                    Log.d("Ping monitor", "No still start");
//                }
//
//            } else if (type.equals("3")) {     //Keyword monitor
//
//                if (active.equals("1")) {
//
//                    boolean isGetKeyword = MonitorUtil.isHttpConnection(address, "3", keywords);
//                    if (isGetKeyword) {
//                        Log.d("Keyword monitor", "true");
//
//                        sendStatus("1", monitorID, monitorName, address, keywords, port, type, interval, startDate);
//                    } else {
//                        Log.d("Keyword monitor", "false");
//
//                        isGetKeyword = MonitorUtil.isHttpConnection(address, "3", keywords);
//
//                        if (isGetKeyword) {
//                            sendStatus("1", monitorID, monitorName, address, keywords, port, type, interval, startDate);
//                        } else {
//                            sendStatus("2", monitorID, monitorName, address, keywords, port, type, interval, startDate);
//                        }
//                    }
//
//                } else {
//                    Log.d("Keyword monitor", "No still start");
//                }
//
//            } else if (type.equals("4")) {     //Port monitor
//                if (active.equals("1")) {
//
//                    boolean isGet = MonitorUtil.isPortMonitor(address, port);
//                    if (isGet) {
//                        Log.d("Port monitor", "true");
//
//                        sendStatus("1", monitorID, monitorName, address, keywords, port, type, interval, startDate);
//                    } else {
//                        Log.d("Port monitor", "false");
//
//                        isGet = MonitorUtil.isPortMonitor(address, port);
//                        if (isGet) {
//                            sendStatus("1", monitorID, monitorName, address, keywords, port, type, interval, startDate);
//                        } else {
//                            sendStatus("2", monitorID, monitorName, address, keywords, port, type, interval, startDate);
//                        }
//                    }
//                } else {
//                    Log.d("Port monitor", "No still start");
//                }
//            }
        }

        return false; // No more work to do
    }

    @Override
    public boolean onStopJob(@NonNull JobParameters job) {
        return false; // No more work to do
    }

    public void monitorCheck(String connectionQuality, String strtype, String active, String monitorID, String monitorName, String address, String keywords, String port, String type, String interval, String startDate, String previousStatus) {
        if (!connectionQuality.equals("UNKNOWN")) {  //Not UNKNOWN, POOR

            if (strtype.equals("1")) {    //Http monitor

                if (active.equals("1")) {

                    boolean isGet = MonitorUtil.isHttpConnection(address, "1", "");

                    if (isGet) {
                        Log.d("Http monitor", "true");

                        sendStatus("1", monitorID, monitorName, address, keywords, port, type, interval, startDate, previousStatus);
                    } else {
                        Log.d("Http monitor", "false");

                        isGet = MonitorUtil.isHttpConnection(address, "1", "");
                        if (isGet) {
                            sendStatus("1", monitorID, monitorName, address, keywords, port, type, interval, startDate, previousStatus);
                        } else {
                            sendStatus("2", monitorID, monitorName, address, keywords, port, type, interval, startDate, previousStatus);
                        }
                    }
                } else {
                    Log.d("Http monitor", "No still start");
                }

            } else if (strtype.equals("2")) {     //Ping monitor

                if (active.equals("1")) {

                    boolean isGet = MonitorUtil.isPingMonitor(address);
                    if (isGet) {
                        Log.d("Ping monitor", "true");

                        sendStatus("1", monitorID, monitorName, address, keywords, port, type, interval, startDate, previousStatus);
                    } else {
                        Log.d("Ping monitor", "false");

                        isGet = MonitorUtil.isPingMonitor(address);

                        if (isGet) {
                            sendStatus("1", monitorID, monitorName, address, keywords, port, type, interval, startDate, previousStatus);
                        } else {
                            sendStatus("2", monitorID, monitorName, address, keywords, port, type, interval, startDate, previousStatus);
                        }
                    }
                } else {
                    Log.d("Ping monitor", "No still start");
                }

            } else if (strtype.equals("3")) {     //Keyword monitor

                if (active.equals("1")) {

                    boolean isGetKeyword = MonitorUtil.isHttpConnection(address, "3", keywords);
                    if (isGetKeyword) {
                        Log.d("Keyword monitor", "true");

                        sendStatus("1", monitorID, monitorName, address, keywords, port, type, interval, startDate, previousStatus);
                    } else {
                        Log.d("Keyword monitor", "false");

                        isGetKeyword = MonitorUtil.isHttpConnection(address, "3", keywords);

                        if (isGetKeyword) {
                            sendStatus("1", monitorID, monitorName, address, keywords, port, type, interval, startDate, previousStatus);
                        } else {
                            sendStatus("2", monitorID, monitorName, address, keywords, port, type, interval, startDate, previousStatus);
                        }
                    }

                } else {
                    Log.d("Keyword monitor", "No still start");
                }

            } else if (strtype.equals("4")) {     //Port monitor
                if (active.equals("1")) {

                    boolean isGet = MonitorUtil.isPortMonitor(address, port);
                    if (isGet) {
                        Log.d("Port monitor", "true");

                        sendStatus("1", monitorID, monitorName, address, keywords, port, type, interval, startDate, previousStatus);
                    } else {
                        Log.d("Port monitor", "false");

                        isGet = MonitorUtil.isPortMonitor(address, port);
                        if (isGet) {
                            sendStatus("1", monitorID, monitorName, address, keywords, port, type, interval, startDate, previousStatus);
                        } else {
                            sendStatus("2", monitorID, monitorName, address, keywords, port, type, interval, startDate, previousStatus);
                        }
                    }
                } else {
                    Log.d("Port monitor", "No still start");
                }
            }
        }
    }

    public void sendStatus(String status, String monitorID, String monitorName, String address, String keywords, String port, String type, String interval, String startDate, String previousStatus) {

        if (myApp.isConnectingToInternet()) {

            userRequests.funSendStatus(monitorID, myApp, status, mobileDateTime, deviceName, ipAddress, previousStatus, monitorName, address, keywords, port, type, interval, startDate, strConnectSpeed);
        }
    }

    private class ConnectionChangedListener
            implements ConnectionClassManager.ConnectionClassStateChangeListener {

        @Override
        public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
            mConnectionClass = bandwidthState;
        }
    }

//    public String getConnectionSpeedCheck() {
//
//        mDeviceBandwidthSampler.startSampling();
//
//        try {
//            // Open a stream to download the image from our URL.
//            URL obj = new URL(mURL);
//            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
//
////            URLConnection connection = new URL(mURL).openConnection();
//            connection.setReadTimeout(10000);
//            connection.setUseCaches(false);
//            connection.connect();
//
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(connection.getInputStream()));
//            String inputLine;
//
//            while ((inputLine = in.readLine()) != null) {
//
//            }
//
//            in.close();
//
////            InputStream input = connection.getInputStream();
////            try {
////                byte[] buffer = new byte[1024];
////
////                // Do some busy waiting while the stream is open.
////                while (input.read(buffer) != -1) {
////                }
////            } finally {
////                input.close();
////            }
//        } catch (IOException e) {
//            Log.e(TAG, "Error while downloading image.");
//        }
//
//        mDeviceBandwidthSampler.stopSampling();
//
//        if (mConnectionClass == ConnectionQuality.UNKNOWN && mTries < 10 ) {
//            mTries++;
//            Log.d("mTrier", String.valueOf(mTries));
//
//            getConnectionSpeedCheck();
//        }
//
//        Log.d("connection status", mConnectionClass.toString());
//
//        return mConnectionClass.toString();
//    }

    public void checkNetworkQuality(String strtype, String active, String monitorID, String monitorName, String address, String keywords, String port, String type, String interval, String startDate, String previousStatus) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mURL)
                .build();

        mDeviceBandwidthSampler.startSampling();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                mDeviceBandwidthSampler.stopSampling();
                // Retry for up to 10 times until we find a ConnectionClass.
                if (mConnectionClass == ConnectionQuality.UNKNOWN && mTries < 10) {
                    mTries++;
                    checkNetworkQuality(strtype, active, monitorID, monitorName, address, keywords, port, type, interval, startDate, previousStatus);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Log.d(TAG, mConnectionClassManager.getCurrentBandwidthQuality().toString());

                strConnectSpeed = mConnectionClassManager.getCurrentBandwidthQuality().toString();

                monitorCheck(strConnectSpeed, strtype, active, monitorID, monitorName, address, keywords, port, type, interval, startDate, previousStatus);

                mDeviceBandwidthSampler.stopSampling();
            }
        });
    }
}
