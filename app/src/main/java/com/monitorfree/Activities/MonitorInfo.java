package com.monitorfree.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import com.monitorfree.CustomAdapter.CustomAdapterCheckpoints;
import com.monitorfree.CustomAdapter.CustomAdapterEvents;
import com.monitorfree.Fragment.Home;
import com.monitorfree.MyApp;
import com.monitorfree.R;
import com.monitorfree.RequestModel.RootMonitorStatus;
import com.monitorfree.RequestModel.UserRequests;
import com.monitorfree.UserModel.AddMonitor;
import com.monitorfree.UserModel.ChartModel;
import com.monitorfree.UserModel.MonitorStatus;
import com.monitorfree.Util.FontManager;
import com.monitorfree.databinding.ActivityMonitorInfoBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

public class MonitorInfo extends AppCompatActivity implements CallBackSuccess {


    ActivityMonitorInfoBinding binding;

    @Inject
    MyApp myApp;

    ArrayList<String> labels = new ArrayList<String>();

    String[] timeHourSlot = {"1", "2", "6", "12", "24"};
    String[] timeMinuteSlot = {"60", "120", "360", "720", "1440"};
    String timeFrame = timeMinuteSlot[4];   //1 hour as default

    String monitorId, monitorName, startDate, monitorStatus, monitorActive, interval, address, port, keywords, type;
    int position;
    boolean isNotification = false;

    @Inject
    UserRequests userRequests;

    CallBackSuccess callBackSuccess;

    ArrayList<ChartModel> chartDataList = new ArrayList<ChartModel>();

    List<Integer> arrBarColor = new ArrayList<Integer>();

    boolean isDelete = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_monitor_info);
        MyApp.component().inject(this);

        if (!myApp.isConnectingToInternet()) {
            return;
        }

        callBackSuccess = this;

        Intent intent = getIntent();
        boolean isMonitorDetails = intent.getBooleanExtra("monitor_details", false);
        if (isMonitorDetails) {
            monitorId = intent.getStringExtra("id");
            monitorName = intent.getStringExtra("name");
            startDate = intent.getStringExtra("startDate");
            monitorStatus = intent.getStringExtra("status");
            monitorActive = intent.getStringExtra("active");
            position = intent.getIntExtra("position", -1);
            interval = intent.getStringExtra("interval");
            address = intent.getStringExtra("address");
            port = intent.getStringExtra("port");
            keywords = intent.getStringExtra("keywords");
            type = intent.getStringExtra("type");
        } else {
            Bundle bundle = intent.getExtras();

            isNotification = bundle.getBoolean("notification", false);
            monitorId = bundle.getString("noti_id");
            monitorName = bundle.getString("noti_name");
            startDate = bundle.getString("noti_startDate");
            monitorStatus = bundle.getString("noti_status");
            monitorActive = bundle.getString("noti_active");
            interval = bundle.getString("noti_interval");
            address = bundle.getString("noti_address");
            port = bundle.getString("noti_port");
            keywords = bundle.getString("noti_keywords");
            type = bundle.getString("noti_type");
        }

        String monitorType = "";
        if (type.equals("1")) {
            monitorType = "http";

            binding.imgTypeIcon.setBackgroundResource(R.drawable.http);
        } else if (type.equals("2")) {
            monitorType = "ping";
            binding.imgTypeIcon.setBackgroundResource(R.drawable.ping);
        } else if (type.equals("3")) {
            monitorType = "keyword";
            binding.temp.setVisibility(View.VISIBLE);

            binding.imgTypeIcon.setBackgroundResource(R.drawable.keyword);
            binding.tempLabel.setText("Keywords");
            binding.txtKeywordsPort.setText(keywords);
        } else if (type.equals("4")) {
            monitorType = "port";
            binding.temp.setVisibility(View.VISIBLE);

            binding.imgTypeIcon.setBackgroundResource(R.drawable.port);
            binding.tempLabel.setText("Port");
            binding.txtKeywordsPort.setText(port);
        }

        binding.include.txtVw.setText("Monitor Details");

//        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
//        binding.include.ivDelete.setTypeface(iconFont);
//        binding.include.ivPaused.setTypeface(iconFont);

        binding.txtCheckType.setText(monitorType);
        binding.txtUrl.setText(address);

        if (interval.equals("5")) binding.txtCheckEvery.setText("288 times a day (approx. every 5 minutes)");
        else if (interval.equals("30")) binding.txtCheckEvery.setText("48 times a day (approx. every 30 minutes)");
        else if (interval.equals("60")) binding.txtCheckEvery.setText("24 times a day (approx. once every hour)");

        binding.include.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog("delete", "Delete ?", "Do you want to delete this monitor?", monitorId);
            }
        });

        binding.include.ivPaused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (monitorActive.equals("1")) {
                    alertDialog("pause", "Pause ?", "Do you want to pause this monitor?", monitorId);
                } else {
                    isDelete = true;
                    userRequests.sendMonitorPause(myApp, monitorId, -1, monitorName, address, keywords, port, type, interval, startDate, MonitorInfo.this, callBackSuccess);
                }
            }
        });

        binding.tvMonitorName.setText(monitorName);

        String strDate = startDate.split(" ")[0];
        binding.txtVwStartDate.setText(strDate);

        if (monitorActive.equals("1")) {        //active
            if (monitorStatus.equals("1")) {    //up
                binding.ivMonitor.setBackgroundResource(R.drawable.svgic_monitor_btn_hover_up);
                binding.imgStatus.setBackgroundResource(R.drawable.ic_up_arrow);
            } else {        //down
                binding.ivMonitor.setBackgroundResource(R.drawable.svgic_monitor_btn_hover_down);
                binding.imgStatus.setBackgroundResource(R.drawable.ic_down);
            }
        } else {        //pause
            binding.ivMonitor.setBackgroundResource(R.drawable.svgic_monitor_btn_hover_pause);
            binding.imgStatus.setBackgroundResource(R.drawable.ic_pause1);
        }

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, timeHourSlot);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        binding.spnr.setAdapter(aa);
        binding.spnr.setSelection(4);   //24 hours

        binding.linLastTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.spnr.performClick();

            }
        });

        binding.spnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                binding.txtTimeSlot.setText("For Last " + timeHourSlot[position] + " hours");
                timeFrame = timeMinuteSlot[position];

                getStatusList(timeFrame);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.include.imgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.include.txtVw.setText("Monitor");

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(myApp.getContext(), LinearLayoutManager.HORIZONTAL, false);

        LinearLayoutManager layoutManager2
                = new LinearLayoutManager(myApp.getContext(), LinearLayoutManager.HORIZONTAL, false);

        binding.recyVwCheckPoint.setLayoutManager(layoutManager);
        binding.recyVwEvents.setLayoutManager(layoutManager2);
    }

    private void alertDialog(String type, String title, String message, String monitorId) {
        AlertDialog.Builder newDialog = new AlertDialog.Builder(MonitorInfo.this);
        newDialog.setTitle(title);
        newDialog.setMessage(message);
        newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (type.equals("delete")) {
                    isDelete = true;
                    userRequests.sendMonitorDelete(myApp, monitorId, MonitorInfo.this, callBackSuccess);

                } else if (type.equals("pause")) {
                    isDelete = false;
                    userRequests.sendMonitorPause(myApp, monitorId, 1, "", "", "", "", "", "", "", MonitorInfo.this, callBackSuccess);
                }

                //JobSchdeuler Cancel
                myApp.jobDispatcher.cancel(monitorId);
            }
        });
        newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        newDialog.show();
    }

    private void getStatusList(String timeFrame) {
        userRequests.getStatusList(myApp, monitorId, timeFrame, this, callBackSuccess);
    }

    @Override
    public void success(Object object) {

        if (object != null) {
            RootMonitorStatus rootStatusList = (RootMonitorStatus) object;

            ArrayList<MonitorStatus> allStatusList = new ArrayList<MonitorStatus>();
            allStatusList = rootStatusList.getData();

            ArrayList<MonitorStatus> sameStatusRemoveList = new ArrayList<MonitorStatus>();
            for (int i = 0 ; i < allStatusList.size() ; i++) {

                if (i == allStatusList.size() - 1) {
                    sameStatusRemoveList.add(allStatusList.get(i));
                    break;
                }

                String[] prevDate = allStatusList.get(i).getMobileDateTime().split(" ");
                String[] prevtime = prevDate[1].split(":");
                String prevStr = prevtime[0] + ":" + prevtime[1];

                String[] nextDate = allStatusList.get(i + 1).getMobileDateTime().split(" ");
                String[] nexttime = nextDate[1].split(":");
                String nextStr = nexttime[0] + ":" + nexttime[1];

                if (!prevStr.equals(nextStr)) {
                    sameStatusRemoveList.add(allStatusList.get(i));
                }
            }

            ArrayList<MonitorStatus> checkPointList = new ArrayList<MonitorStatus>();
            for (int i = sameStatusRemoveList.size() - 1 ; i >= 0 ; i--) {
                checkPointList.add(sameStatusRemoveList.get(i));
            }

            binding.recyVwCheckPoint.setAdapter(new CustomAdapterCheckpoints(myApp.getContext(), checkPointList));
            binding.recyVwCheckPoint.setNestedScrollingEnabled(false);

            chartDataList.clear();
            labels.clear();
            arrBarColor.clear();

            for (int i = 0; i < sameStatusRemoveList.size(); i++) {

                MonitorStatus temp1 = sameStatusRemoveList.get(i);
                int cnt = 1;
                for (int j = i + 1; j < sameStatusRemoveList.size(); j++) {

                    MonitorStatus temp2 = sameStatusRemoveList.get(j);

                    if (temp1.getStatus().equals(temp2.getStatus())) {
                        cnt++;
                    } else {
                        i = j - 1;
                        break;
                    }
                }

                ChartModel item = new ChartModel();
                item.setHeight(cnt);
                item.setStatus(temp1.getStatus());
                item.setStartTime(temp1.getMobileDateTime());

                chartDataList.add(item);

                if (cnt == sameStatusRemoveList.size() - i) {
                    break;
                }
            }

            ArrayList<ChartModel> latetEventDataList = new ArrayList<ChartModel>();
            for (int sort = chartDataList.size() - 1 ; sort >=0 ; sort--) {
                latetEventDataList.add(chartDataList.get(sort));
            }

            binding.recyVwEvents.setAdapter(new CustomAdapterEvents(myApp.getContext(), latetEventDataList, interval));
            binding.recyVwEvents.setNestedScrollingEnabled(false);

            ArrayList<BarEntry> bargroup1 = new ArrayList<>();
//            for (int i = 0 ; i < chartDataList.size() ; i++) {

            int index = 0;
            for (int i = chartDataList.size() - 1 ; i >= 0; i--) {

                String[] startDate = chartDataList.get(i).getStartTime().split(" ");
                String[] time = startDate[1].split(":");
                labels.add(time[0] + ":" + time[1]);

                bargroup1.add(new BarEntry(chartDataList.get(i).getHeight() * Float.valueOf(interval), index));

                if (chartDataList.get(i).getStatus().equals("1")) {     //ok
                    arrBarColor.add(Color.parseColor("#24BE54"));
                } else {           //not ok
                    arrBarColor.add(Color.parseColor("#F34146"));
                }

                index++;
            }

            BarDataSet bardataset = new BarDataSet(bargroup1, "");
            bardataset.setColors(arrBarColor);

            BarData data = new BarData(labels, bardataset);
            binding.barchart.setData(data);

            binding.barchart.getLegend().setEnabled(false);

            binding.barchart.invalidate();
        }
        else {
            if (isDelete) {
                Home.m_bPause = false;
            } else {
                Home.m_bPause = true;
            }

            myApp.isFirstLogin = false;

            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        myApp.isFirstLogin = false;

        if (isNotification) {
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }
}
