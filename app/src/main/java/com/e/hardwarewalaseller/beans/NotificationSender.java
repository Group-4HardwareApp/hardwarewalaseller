package com.e.hardwarewalaseller.beans;

public class NotificationSender {

    private String to;
    private Data data;

    public NotificationSender(String to, Data data) {
        this.to = to;
        this.data = data;
    }


    public NotificationSender(){}

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
