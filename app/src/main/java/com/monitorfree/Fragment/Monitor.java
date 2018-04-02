package com.monitorfree.Fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monitorfree.R;
import com.monitorfree.databinding.FragmentAddMonitorBinding;
import com.monitorfree.databinding.FragmentMonitorBinding;
import com.monitorfree.databinding.FragmentSettingsBinding;


/**
 * Created by jassi on 30-07-2016.
 */

public class Monitor extends Fragment implements View.OnClickListener {

    FragmentAddMonitorBinding binding;

    Fragment fragment = null;
    Class fragmentClass = null;
    FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_monitor, container, false);

        binding.addmonitor.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addmonitor) {
            getActivity().setTitle("Monitor");
            fragmentClass = AddMonitor.class;
            replaceFragment();
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
