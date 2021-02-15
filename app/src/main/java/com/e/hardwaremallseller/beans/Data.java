package com.e.hardwaremallseller.beans;

public class Data {

    private String Title;
    private String Message; //body
    private String Image;

    public Data(String title, String message, String image) {
        Title = title;
        Message = message;
        Image = image;
    }

    public Data(){}

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
