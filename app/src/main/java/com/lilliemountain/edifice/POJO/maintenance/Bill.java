
package com.lilliemountain.edifice.POJO.maintenance;


import android.os.Parcel;
import android.os.Parcelable;

public class Bill implements Parcelable {

    private Integer amount;
    private String particular;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Bill() {
    }

    /**
     * 
     * @param amount
     * @param particular
     */
    public Bill(Integer amount, String particular) {
        super();
        this.amount = amount;
        this.particular = particular;
    }

    protected Bill(Parcel in) {
        if (in.readByte() == 0) {
            amount = null;
        } else {
            amount = in.readInt();
        }
        particular = in.readString();
    }

    public static final Creator<Bill> CREATOR = new Creator<Bill>() {
        @Override
        public Bill createFromParcel(Parcel in) {
            return new Bill(in);
        }

        @Override
        public Bill[] newArray(int size) {
            return new Bill[size];
        }
    };

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getParticular() {
        return particular;
    }

    public void setParticular(String particular) {
        this.particular = particular;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (amount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(amount);
        }
        dest.writeString(particular);
    }
}
