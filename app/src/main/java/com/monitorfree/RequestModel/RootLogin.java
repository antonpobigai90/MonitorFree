package com.monitorfree.RequestModel;

/**
 * Created by jaspreet on 11/22/17.
 */

public class RootLogin {


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    Status status;
    Data data;

}
