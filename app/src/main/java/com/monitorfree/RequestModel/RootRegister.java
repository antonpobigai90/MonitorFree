package com.monitorfree.RequestModel;

/**
 * Created by jaspreet on 11/22/17.
 */

public class RootRegister {


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    Status status;

    String monitorId;

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }
}
