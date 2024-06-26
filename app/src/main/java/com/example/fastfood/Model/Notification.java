package com.example.fastfood.Model;

public class Notification {
    private String notification_Id, content_Notification, send_Date, user_Id;

    public Notification() {
    }

    public Notification(String notification_Id, String content_Notification, String send_Date, String user_Id) {
        this.notification_Id = notification_Id;
        this.content_Notification = content_Notification;
        this.send_Date = send_Date;
        this.user_Id = user_Id;
    }

    public String getNotification_Id() {
        return notification_Id;
    }

    public void setNotification_Id(String notification_Id) {
        this.notification_Id = notification_Id;
    }

    public String getContent_Notification() {
        return content_Notification;
    }

    public void setContent_Notification(String content_Notification) {
        this.content_Notification = content_Notification;
    }

    public String getSend_Date() {
        return send_Date;
    }

    public void setSend_Date(String send_Date) {
        this.send_Date = send_Date;
    }

    public String getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(String user_Id) {
        this.user_Id = user_Id;
    }
}
