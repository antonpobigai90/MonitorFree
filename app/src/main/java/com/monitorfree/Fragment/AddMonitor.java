package com.monitorfree.Fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monitorfree.Activities.SaveMonitor;
import com.monitorfree.CustomAdapter.CustomAdapterCheckpoints;
import com.monitorfree.CustomAdapter.CustomAdapterEvents;
import com.monitorfree.R;
import com.monitorfree.databinding.FragmentMonitorBinding;


/**
 * Created by jassi on 30-07-2016.
 */

public class AddMonitor extends Fragment implements View.OnClickListener {


    FragmentMonitorBinding binding;

    Fragment fragment = null;
    Class fragmentClass = null;
    FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_monitor, container, false);


        binding.linHttp.setOnClickListener(this);
        binding.linPing.setOnClickListener(this);
        binding.linPort.setOnClickListener(this);
        binding.linKeyword.setOnClickListener(this);
        binding.txtFaq.setOnClickListener(this);

        return binding.getRoot();
    }

    public void clickAddMonitor(View view) {

    }

    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()) {

            case R.id.linHttp:
                intent = new Intent(getActivity(), SaveMonitor.class);
                intent.putExtra("type", "1");
                startActivity(intent);
                break;

            case R.id.linPing:
                intent = new Intent(getActivity(), SaveMonitor.class);
                intent.putExtra("type", "2");
                startActivity(intent);

                break;
            case R.id.linPort:
                intent = new Intent(getActivity(), SaveMonitor.class);
                intent.putExtra("type", "4");
                startActivity(intent);

                break;
            case R.id.linKeyword:
                intent = new Intent(getActivity(), SaveMonitor.class);
                intent.putExtra("type", "3");
                startActivity(intent);

                break;

            case R.id.txtFaq:
                fragmentClass = FAQs.class;
                replaceFragment();
                break;

        }
    }

    public void replaceFragment() {
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.relFragment, fragment)
                    .commit();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
