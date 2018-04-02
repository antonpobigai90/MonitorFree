package com.monitorfree.RequestModel;

import com.monitorfree.UserModel.AddMonitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaspreet on 11/22/17.
 */

public class RootMonitorList {

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    Status status;

    public ArrayList<AddMonitor> getData() {
        return data;
    }

    public void setData(ArrayList<AddMonitor> data) {
        this.data = data;
    }

    ArrayList<AddMonitor> data;
}
