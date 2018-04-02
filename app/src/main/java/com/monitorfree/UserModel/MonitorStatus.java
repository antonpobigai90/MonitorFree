package com.monitorfree.UserModel;

/**
 * Created by jaspreet on 11/22/17.
 */

public class MonitorStatus {

    String id;
    String monitorId;
    String status;
    String addedDate;
    String mobileDateTime;
    String deviceName;
    String ipAddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getMobileDateTime() {
        return mobileDateTime;
    }

    public void setMobileDateTime(String mobileDateTime) {
        this.mobileDateTime = mobileDateTime;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
