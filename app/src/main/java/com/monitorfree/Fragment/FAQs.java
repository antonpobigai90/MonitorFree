package com.monitorfree.Fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.monitorfree.CustomAdapter.ExpendableListAdapter;
import com.monitorfree.R;
import com.monitorfree.databinding.FragmentFaqsBinding;
import com.monitorfree.databinding.FragmentSettingsBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by jassi on 30-07-2016.
 */

public class FAQs extends Fragment {

    FragmentFaqsBinding binding;

    String[] caption = {"1. Can I get alerts even when my device is shut down?", "2. Why do you say my site is down, when it is up?", "3. I love this free app, what can I do to help"};
    String[] subDetails = {"a. No, but this is the magic. We are able to provide free limitless service, because we rely on your device rather than our own (high cost) servers. Sure, you can buy paid monitoring service with one of our competitors, but what would happen when your site is down? You’ll get notified by text message to your phone. The same phone you say is now shut down anyhow.", "a. The internet is dynamic. One second a site is up, and another it is down. Also there are common outage reasons which most times are a results of \n"+
            "b. Timeouts which may imply your device was blocked by your website server or your website loading really slow.\n"+
            "c. HTTP Error 403, Your device is being forbidden from visiting the website. \n"+
            "d. HTTP Errors 500/502/503. This may imply something is wrong with this server. Didn’t you install our app just to find out things like that", "a. We created this free app on our spare time. We wanted to save money for people that had to buy expensive monitoring services. The thing that makes us wake up in the morning and keep improving it, is seeing your positive reviews. If you can leave a positive review, and after that share the app on facebook or Twitter, we’ll be honored."};

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_faqs, container, false);

        ExpendableListAdapter listAdapter;

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add(caption[0]);
        listDataHeader.add(caption[1]);
        listDataHeader.add(caption[2]);

        List<String> first = new ArrayList<String>();
        first.add(subDetails[0]);

        List<String> second = new ArrayList<String>();
        second.add(subDetails[1]);

        List<String> third = new ArrayList<String>();
        third.add(subDetails[2]);

        listDataChild.put(listDataHeader.get(0), first);
        listDataChild.put(listDataHeader.get(1), second);
        listDataChild.put(listDataHeader.get(2), third);

        listAdapter = new ExpendableListAdapter(getActivity(), listDataHeader, listDataChild);
        binding.expendLV.setAdapter(listAdapter);

        return binding.getRoot();
    }
}
