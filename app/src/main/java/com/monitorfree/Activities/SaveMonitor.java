package com.monitorfree.Activities;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.monitorfree.MyApp;
import com.monitorfree.R;
import com.monitorfree.RequestModel.UserRequests;
import com.monitorfree.UserModel.AddMonitor;
import com.monitorfree.databinding.ActivitySaveMonitorBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import static com.monitorfree.Util.GlobalKeys.USER_HASH;
import static com.monitorfree.Util.GlobalKeys.USER_ID;

public class SaveMonitor extends AppCompatActivity implements CallBackSuccess {

    ActivitySaveMonitorBinding binding;

    @Inject
    MyApp myApp;

//    @Inject
    UserRequests userRequests = new UserRequests();

    AddMonitor addMonitor = new AddMonitor();

    CallBackSuccess callBackSuccess;

    String type, strProtocol;
    String[] protocol = {"http://", "https://"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_save_monitor);
//        MyApp.component().inject(this);

        callBackSuccess = this;
        type = getIntent().getStringExtra("type");

        binding.include.ivPaused.setVisibility(View.GONE);
        binding.include.ivDelete.setVisibility(View.GONE);

        if (type.equals("3")) {
            binding.txtMonitor.setText("Creating keyword monitor");
            binding.edittext0.setVisibility(View.VISIBLE);

        } else if (type.equals("2")) {
            binding.linLastTime.setVisibility(View.GONE);
            binding.spnr.setVisibility(View.GONE);
            binding.edtWebAddress.setVisibility(View.GONE);
            binding.txtMonitor.setText("Creating ping monitor");
            binding.edittext1.setVisibility(View.VISIBLE);
        } else if (type.equals("4")){
            binding.txtMonitor.setText("Creating port monitor");
            binding.edittext2.setVisibility(View.VISIBLE);
        }

        binding.edtWebAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.edtFriendlyName.setText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, protocol);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnr.setAdapter(aa);
        binding.spnr.setSelection(0);

        binding.linLastTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.spnr.performClick();

            }
        });

        strProtocol = "http";
        binding.spnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                binding.txtTimeSlot.setText(protocol[position]);
                strProtocol = protocol[position];
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

        binding.include.txtVw.setText("Add Monitor");

        addMonitor.setInterval(String.valueOf(5));

        Field f = null;
        try {
            f =TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(binding.edtFriendlyName, R.drawable.cursor);
            f.set(binding.edtWebAddress, R.drawable.cursor);
            f.set(binding.edtMeta, R.drawable.cursor);
            f.set(binding.edtPort, R.drawable.cursor);
            f.set(binding.edtPing, R.drawable.cursor);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void clickSave(View view) {

        switch (view.getId()) {

            case R.id.btnSaveMonitor:

                Calendar c = Calendar.getInstance();

                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String moibleDateTime = df.format(c.getTime());

                addMonitor.setName(binding.edtFriendlyName.getText().toString());

                if(type.equals("2")) {
                    addMonitor.setAddress(binding.edtPing.getText().toString());
                } else {
                    addMonitor.setAddress(strProtocol + binding.edtWebAddress.getText().toString());
                }
                addMonitor.setType(type);
                addMonitor.setStartDate(df1.format(c.getTime()));
                addMonitor.setUserId(MyApp.instance.getKey(USER_ID));
                addMonitor.setKeywords("");
                addMonitor.setPort("");
                addMonitor.setMobileDateTime(moibleDateTime);

                if (binding.rad1.isChecked()) {
                    addMonitor.setInterval(String.valueOf(5));
                } else if (binding.rad2.isChecked()) {
                    addMonitor.setInterval(String.valueOf(30));
                } else if (binding.rad3.isChecked()) {
                    addMonitor.setInterval(String.valueOf(60));
                }

                if (addMonitor.getName() == null || addMonitor.getName().equals("")) {
                    binding.edtFriendlyName.setError("Please Enter Name");
                } else if (addMonitor.getAddress() == null || addMonitor.getAddress().equals("")) {
                    binding.edtWebAddress.setError("Please Enter Address");
                } else if (checkWebAddress(addMonitor.getAddress()) == false) {
                    binding.edtWebAddress.setError("Web Address Invalid");
                } else {

                    if (type.equals("3")) {
                        addMonitor.setKeywords(binding.edtMeta.getText().toString());
                    } else if (type.equals("4")) {
                        addMonitor.setPort(binding.edtPort.getText().toString());
                    }

                    userRequests.funAddMonitor(MyApp.instance, addMonitor, binding, SaveMonitor.this, callBackSuccess);

                }
                break;
        }
    }

    @Override
    public void success(Object object) {
        MyApp.instance.isFirstLogin = false;

        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }


    boolean checkWebAddress(String address) {
        return Patterns.WEB_URL.matcher(address).matches();
    }

    boolean checkPortocal(String address) {

        if (address.toLowerCase().contains("http".toLowerCase()) || address.toLowerCase().contains("https".toLowerCase())){
            return true;
        }

        return false;
    }


}
