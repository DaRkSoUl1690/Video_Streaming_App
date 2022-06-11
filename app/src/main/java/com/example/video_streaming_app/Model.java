package com.example.video_streaming_app;

public class Model {
    String title,vurl;

    public Model() {
    }

    public  String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVurl() {
        return vurl;
    }

    public void setVurl(String vurl) {
        this.vurl = vurl;
    }

    public Model(String title, String vurl) {
        this.title = title;
        this.vurl = vurl;
    }
}
