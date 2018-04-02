package com.monitorfree.CustomAdapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monitorfree.R;
import com.monitorfree.UserModel.AddMonitor;
import com.monitorfree.UserModel.MonitorStatus;
import com.monitorfree.databinding.CustomCheckpointsBinding;
import com.monitorfree.databinding.CustomMonitorBinding;

import java.util.List;

/**
 * Created by jassi on 01-08-2016.
 */

public class CustomAdapterCheckpoints extends RecyclerView.Adapter<CustomAdapterCheckpoints.Holder> {

    Context context;
    List<MonitorStatus> monitorStatusList;

    public CustomAdapterCheckpoints(Context context, List<MonitorStatus> monitorStatusList) {
        this.context = context;
        this.monitorStatusList = monitorStatusList;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.custom_checkpoints, parent, false);
        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        MonitorStatus statusData = monitorStatusList.get(position);

        String[] date = statusData.getMobileDateTime().split(" ");
        String[] arrTime = date[1].split(":");

        holder.binding.txtMonitorTime.setText(arrTime[0] + ":" + arrTime[1]);

        if (statusData.getStatus().equals("1")) {
            holder.binding.imgMonitorStatus.setBackgroundResource(R.drawable.ic_up_arrow);
        } else {
            holder.binding.imgMonitorStatus.setBackgroundResource(R.drawable.ic_down);
        }
    }

    @Override
    public int getItemCount() {
        return this.monitorStatusList.size();
    }


    class Holder extends RecyclerView.ViewHolder {

        CustomCheckpointsBinding binding;

        public Holder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
