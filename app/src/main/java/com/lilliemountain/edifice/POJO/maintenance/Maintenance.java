
package com.lilliemountain.edifice.POJO.maintenance;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Maintenance implements Parcelable {

    private List<Bill> bill = null;
    private Header header;
    private String id;
    private Integer payable;
    private Integer pending;
    private String status;
    private long subtotal;
    private long totalbill;
    /**
     * No args constructor for use in serialization
     * 
     */
    public Maintenance() {
    }

    /**
     *
     * @param id
     * @param payable
     * @param bill
     * @param status
     * @param pending
     * @param subtotal
     * @param header
     * @param totalbill
     */
    public Maintenance(List<Bill> bill, Header header, String id, Integer payable, Integer pending, String status, long subtotal, long totalbill) {
        this.bill = bill;
        this.header = header;
        this.id = id;
        this.payable = payable;
        this.pending = pending;
        this.status = status;
        this.subtotal = subtotal;
        this.totalbill = totalbill;
    }

    protected Maintenance(Parcel in) {
        bill = in.createTypedArrayList(Bill.CREATOR);
        header = in.readParcelable(Header.class.getClassLoader());
        id = in.readString();
        if (in.readByte() == 0) {
            payable = null;
        } else {
            payable = in.readInt();
        }
        if (in.readByte() == 0) {
            pending = null;
        } else {
            pending = in.readInt();
        }
        status = in.readString();
        subtotal = in.readLong();
        totalbill = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(bill);
        dest.writeParcelable(header, flags);
        dest.writeString(id);
        if (payable == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(payable);
        }
        if (pending == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(pending);
        }
        dest.writeString(status);
        dest.writeLong(subtotal);
        dest.writeLong(totalbill);
    }

    public static final Creator<Maintenance> CREATOR = new Creator<Maintenance>() {
        @Override
        public Maintenance createFromParcel(Parcel in) {
            return new Maintenance(in);
        }

        @Override
        public Maintenance[] newArray(int size) {
            return new Maintenance[size];
        }
    };

    public List<Bill> getBill() {
        return bill;
    }

    public void setBill(List<Bill> bill) {
        this.bill = bill;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPayable() {
        return payable;
    }

    public void setPayable(Integer payable) {
        this.payable = payable;
    }

    public Integer getPending() {
        return pending;
    }

    public void setPending(Integer pending) {
        this.pending = pending;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(long subtotal) {
        this.subtotal = subtotal;
    }

    public long getTotalbill() {
        return totalbill;
    }

    public void setTotalbill(long totalbill) {
        this.totalbill = totalbill;
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
