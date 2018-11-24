package com.example.qiaoxian.myfbchat.bean;

public class Chat1 {

    private String sender;
    private String receiver;
    private String message;
    private String time;
    private String url;
    private String username;

    public Chat1() {
    }

    public Chat1(String sender, String message, String time, String username) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time = time;
        this.username = username;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
