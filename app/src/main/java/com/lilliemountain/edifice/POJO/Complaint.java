package com.lilliemountain.edifice.POJO;


import android.os.Parcel;
import android.os.Parcelable;

public class Complaint implements Parcelable {

    private String admincomments;
    private String category;
    private String complaint;
    private String date;
    private String residentName;
    private String status;
    private String title;
    private String uid;

    /**
     * No args constructor for use in serialization
     *
     */
    public Complaint() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     *
     * @param title
     * @param category
     * @param admincomments
     * @param residentName
     * @param status
     * @param date
     * @param complaint
     * @param uid
     */

    public Complaint(String admincomments, String category, String complaint, String date, String residentName, String status, String title, String uid) {
        this.admincomments = admincomments;
        this.category = category;
        this.complaint = complaint;
        this.date = date;
        this.residentName = residentName;
        this.status = status;
        this.title = title;
        this.uid = uid;
    }

    protected Complaint(Parcel in) {
        admincomments = in.readString();
        category = in.readString();
        complaint = in.readString();
        date = in.readString();
        residentName = in.readString();
        status = in.readString();
        title = in.readString();
        uid = in.readString();
    }

    public static final Creator<Complaint> CREATOR = new Creator<Complaint>() {
        @Override
        public Complaint createFromParcel(Parcel in) {
            return new Complaint(in);
        }

        @Override
        public Complaint[] newArray(int size) {
            return new Complaint[size];
        }
    };

    public String getAdmincomments() {
        return admincomments;
    }

    public void setAdmincomments(String admincomments) {
        this.admincomments = admincomments;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(admincomments);
        dest.writeString(category);
        dest.writeString(complaint);
        dest.writeString(date);
        dest.writeString(residentName);
        dest.writeString(status);
        dest.writeString(title);
        dest.writeString(uid);
    }
}
