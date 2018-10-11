package com.bignerdranch.android.myreceipts;

import java.util.Date;
import java.util.UUID;

public class Receipt {

    private UUID mId;
    private String mTitle;
    private String mShop;
    private String mComment;
    private Date mDate;
    private Double mLong;
    private Double mLat;

    public Receipt() {
        this(UUID.randomUUID());
    }

    public Receipt(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }
    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }
    public Date getDate() {
        return mDate;
    }
    public void setDate(Date date) {
        mDate = date;
    }
    public String getShop() {
        return mShop;
    }
    public String getComment() {
        return mComment;
    }
    public void setShop(String shop) {
        mShop = shop;
    }
    public void setComment(String comment) {
        mComment = comment;
    }
    public Double getLong() { return mLong; }
    public Double getLat() { return mLat; }
    public void setLong(Double aLong) { mLong = aLong; }
    public void setLat(Double lat) { mLat = lat; }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
