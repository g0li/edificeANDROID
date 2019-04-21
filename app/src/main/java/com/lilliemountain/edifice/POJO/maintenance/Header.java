package com.lilliemountain.edifice.POJO.maintenance;


import android.os.Parcel;
import android.os.Parcelable;

public class Header  implements Parcelable {

    private String billfor;
    private String building;
    private Integer carpetarea;
    private String date;
    private String flat;
    private String housetype;
    private String resident;
    private String phoneno;

    public Header(String billfor, String building, Integer carpetarea, String date, String flat, String housetype, String resident, String phoneno) {
        this.billfor = billfor;
        this.building = building;
        this.carpetarea = carpetarea;
        this.date = date;
        this.flat = flat;
        this.housetype = housetype;
        this.resident = resident;
        this.phoneno = phoneno;
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Header() {
    }


    protected Header(Parcel in) {
        billfor = in.readString();
        building = in.readString();
        if (in.readByte() == 0) {
            carpetarea = null;
        } else {
            carpetarea = in.readInt();
        }
        date = in.readString();
        flat = in.readString();
        housetype = in.readString();
        resident = in.readString();
        phoneno = in.readString();
    }

    public static final Creator<Header> CREATOR = new Creator<Header>() {
        @Override
        public Header createFromParcel(Parcel in) {
            return new Header(in);
        }

        @Override
        public Header[] newArray(int size) {
            return new Header[size];
        }
    };

    public String getBillfor() {
        return billfor;
    }

    public void setBillfor(String billfor) {
        this.billfor = billfor;
    }

    public String getBuilding() {
        return building;
    }


    public void setBuilding(String building) {
        this.building = building;
    }

    public Integer getCarpetarea() {
        return carpetarea;
    }

    public void setCarpetarea(Integer carpetarea) {
        this.carpetarea = carpetarea;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getHousetype() {
        return housetype;
    }

    public void setHousetype(String housetype) {
        this.housetype = housetype;
    }

    public String getResident() {
        return resident;
    }

    public void setResident(String resident) {
        this.resident = resident;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(billfor);
        dest.writeString(building);
        if (carpetarea == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(carpetarea);
        }
        dest.writeString(date);
        dest.writeString(flat);
        dest.writeString(housetype);
        dest.writeString(resident);
        dest.writeString(phoneno);
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }
}
