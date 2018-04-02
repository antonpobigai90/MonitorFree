package com.monitorfree.CustomAdapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import com.monitorfree.Activities.MonitorInfo;
import com.monitorfree.R;
import com.monitorfree.UserModel.AddMonitor;
import com.monitorfree.databinding.CustomMonitorBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jassi on 01-08-2016.
 */

public class CustomAdapterMonitor extends RecyclerView.Adapter<CustomAdapterMonitor.Holder> {

    Context context;
    List<AddMonitor> addMonitorList;

    public CustomAdapterMonitor(Context context, List<AddMonitor> addMonitorList) {
        this.context = context;
        this.addMonitorList = addMonitorList;

    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.custom_monitor, parent, false);
        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        holder.binding.crdVwMonitor.setOnClickListener((View) -> {
            Intent intent = new Intent(context, MonitorInfo.class);

            intent.putExtra("monitor_details", true);
            intent.putExtra("name",addMonitorList.get(position).getName());
            intent.putExtra("id",addMonitorList.get(position).getId());
            intent.putExtra("startDate",addMonitorList.get(position).getStartDate());
            intent.putExtra("status", addMonitorList.get(position).getStatus().get(0).getStatus());
            intent.putExtra("active", addMonitorList.get(position).getActive());
            intent.putExtra("position", position);
            intent.putExtra("interval", addMonitorList.get(position).getInterval());
            intent.putExtra("address", addMonitorList.get(position).getAddress());
            intent.putExtra("type", addMonitorList.get(position).getType());
            intent.putExtra("keywords", addMonitorList.get(position).getKeywords());
            intent.putExtra("port", addMonitorList.get(position).getPort());

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        if (addMonitorList.get(position).getActive().equals("1")) {
            if (addMonitorList.get(position).getStatus().get(0).getStatus().equals("1")) {
                holder.binding.imgStatus.setBackgroundResource(R.drawable.ic_up_arrow);
                holder.binding.txtGoSite.setVisibility(View.GONE);
            } else {
                holder.binding.imgStatus.setBackgroundResource(R.drawable.ic_down);
                holder.binding.txtGoSite.setVisibility(View.VISIBLE);

                holder.binding.txtGoSite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String type = addMonitorList.get(position).getType();
                        String address = addMonitorList.get(position).getAddress();
                        String port = addMonitorList.get(position).getPort();
                        if (type.equals("4")) {
                            String endStr = address.substring(address.length() - 1, address.length());
                            if (endStr.equals("/")) {
                                address = address.substring(0, address.length() - 1) + ":" + port + "/";
                            } else {
                                address = address + ":" + port + "/";
                            }
                        }
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
                        context.startActivity(browserIntent);
                    }
                });
            }
        } else {
            holder.binding.imgStatus.setBackgroundResource(R.drawable.ic_pause1);
        }

        holder.binding.tvLastDate.setText(addMonitorList.get(position).getStatus().get(0).getMobileDateTime());
        holder.binding.tvMonitorName.setText("" + addMonitorList.get(position).getName());
        holder.binding.arrowRight.setBackgroundResource(R.drawable.arrow_right);

        if (addMonitorList.get(position).getType().equals("1")) {       //http
            holder.binding.imgTypeIcon.setBackgroundResource(R.drawable.http);
        } else if (addMonitorList.get(position).getType().equals("2")) {    //ping
            holder.binding.imgTypeIcon.setBackgroundResource(R.drawable.ping);
        } else if (addMonitorList.get(position).getType().equals("3")) {    //keyword
            holder.binding.imgTypeIcon.setBackgroundResource(R.drawable.keyword);
        } else if (addMonitorList.get(position).getType().equals("4")) {    //port
            holder.binding.imgTypeIcon.setBackgroundResource(R.drawable.port);
        }

    }

    @Override
    public int getItemCount() {
        return addMonitorList.size();
    }


    class Holder extends RecyclerView.ViewHolder {

        CustomMonitorBinding binding;

        public Holder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }


//
}
