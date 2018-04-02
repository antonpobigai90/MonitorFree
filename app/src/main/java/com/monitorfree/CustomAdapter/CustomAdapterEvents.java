package com.monitorfree.CustomAdapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monitorfree.R;
import com.monitorfree.UserModel.ChartModel;
import com.monitorfree.UserModel.MonitorStatus;
import com.monitorfree.databinding.CustomEventsBinding;
import com.monitorfree.databinding.CustomMonitorBinding;

import java.util.List;

/**
 * Created by jassi on 01-08-2016.
 */

public class CustomAdapterEvents extends RecyclerView.Adapter<CustomAdapterEvents.Holder>  {

    Context context;
    List<ChartModel> chartList;
    String interval_time;

    public CustomAdapterEvents(Context context, List<ChartModel> monitorStatusList, String interval_time) {
        this.context = context;
        this.chartList = monitorStatusList;
        this.interval_time = interval_time;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.custom_events, parent, false);
        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        ChartModel chartData = chartList.get(position);

        if (chartData.getStatus().equals("1")) {
            holder.binding.ivMonitorStatus.setBackgroundResource(R.drawable.ic_up_arrow);
        } else {
            holder.binding.ivMonitorStatus.setBackgroundResource(R.drawable.ic_down);
        }

        holder.binding.txtVwTime.setText(Integer.valueOf(this.interval_time) * chartData.getHeight() + " mins");
        holder.binding.txtDateTime.setText(chartData.getStartTime());
    }

    @Override
    public int getItemCount() {
        return this.chartList.size();
    }


    class Holder extends RecyclerView.ViewHolder {

        CustomEventsBinding binding;

        public Holder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }


//
}
