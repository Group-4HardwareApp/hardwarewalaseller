package com.e.hardwaremallseller.beans;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Comment implements Serializable {

    @SerializedName("commentId")
    @Expose
    private String commentId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("timestamp")
    @Expose
    private Long timestamp;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userImg")
    @Expose
    private String userImg;

    public Comment() {
    }

    public Comment(String date, Long timestamp, String userId, String comment,
                   String productId, String rating, String userName, String userImg) {
        super();
        this.date = date;
        this.timestamp = timestamp;
        this.userId = userId;
        this.comment = comment;
        this.productId = productId;
        this.rating = rating;
        this.userName = userName;
        this.userImg = userImg;
    }

    public String getUserImg(){ return userImg; }

    public void setUserImg(String userImg){ this.userImg = userImg; }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

}