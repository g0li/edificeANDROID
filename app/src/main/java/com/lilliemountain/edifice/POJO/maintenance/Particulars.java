package com.lilliemountain.edifice.POJO.maintenance;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Particulars implements Parcelable {
String particular;
Long baseprice;

    protected Particulars(Parcel in) {
        particular = in.readString();
        if (in.readByte() == 0) {
            baseprice = null;
        } else {
            baseprice = in.readLong();
        }
    }

    public static final Creator<Particulars> CREATOR = new Creator<Particulars>() {
        @Override
        public Particulars createFromParcel(Parcel in) {
            return new Particulars(in);
        }

        @Override
        public Particulars[] newArray(int size) {
            return new Particulars[size];
        }
    };

    public Long getBaseprice() {
        return baseprice;
    }

    public void setBaseprice(Long baseprice) {
        this.baseprice = baseprice;
    }

    public String getName() {
        return particular;
    }

    public void setName(String particular) {
        this.particular = particular;
    }

    public Particulars() {
    }

    public Particulars(String particular, Long baseprice) {
        this.particular = particular;
        this.baseprice = baseprice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(particular);
        if (baseprice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(baseprice);
        }
    }
}
