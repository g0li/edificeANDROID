package com.lilliemountain.edifice.POJO;

import android.os.Parcel;
import android.os.Parcelable;

public class Notices implements Parcelable {
    private String date;
    private String day;
    private String noticeText;
    private String noticeTitle;
    private String noticeType;

    /**
     * No args constructor for use in serialization
     *
     */
    public Notices() {
    }

    /**
     *
     * @param noticeText
     * @param day
     * @param date
     * @param noticeTitle
     * @param noticeType
     */
    public Notices(String date, String day, String noticeText, String noticeTitle, String noticeType) {
        super();
        this.date = date;
        this.day = day;
        this.noticeText = noticeText;
        this.noticeTitle = noticeTitle;
        this.noticeType = noticeType;
    }

    protected Notices(Parcel in) {
        date = in.readString();
        day = in.readString();
        noticeText = in.readString();
        noticeTitle = in.readString();
        noticeType = in.readString();
    }

    public static final Creator<Notices> CREATOR = new Creator<Notices>() {
        @Override
        public Notices createFromParcel(Parcel in) {
            return new Notices(in);
        }

        @Override
        public Notices[] newArray(int size) {
            return new Notices[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getNoticeText() {
        return noticeText;
    }

    public void setNoticeText(String noticeText) {
        this.noticeText = noticeText;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(day);
        dest.writeString(noticeText);
        dest.writeString(noticeTitle);
        dest.writeString(noticeType);
    }
}
