package com.lilliemountain.edifice.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Users implements Parcelable {

    private String building;
    private Integer carpetarea;
    private String editId;
    private String emailid;
    private String extrafeatures;
    private String flat;
    private String housetype;
    private String landline;
    private String mobile;
    private String name;
    private String parking;
    private String password;
    private Integer salutationid;
    private List<Boolean> selectionFeature = null;
    private String slotno;
    private String stickerno;
    private String twofourwheel;
    private String vmodel;

    /**
     * No args constructor for use in serialization
     *
     */
    public Users() {
    }

    /**
     *
     * @param parking
     * @param building
     * @param flat
     * @param salutationid
     * @param carpetarea
     * @param selectionFeature
     * @param password
     * @param emailid
     * @param vmodel
     * @param twofourwheel
     * @param slotno
     * @param housetype
     * @param landline
     * @param stickerno
     * @param name
     * @param editId
     * @param extrafeatures
     * @param mobile
     */
    public Users(String building, Integer carpetarea, String editId, String emailid, String extrafeatures, String flat, String housetype, String landline, String mobile, String name, String parking, String password, Integer salutationid, List<Boolean> selectionFeature, String slotno, String stickerno, String twofourwheel, String vmodel) {
        super();
        this.building = building;
        this.carpetarea = carpetarea;
        this.editId = editId;
        this.emailid = emailid;
        this.extrafeatures = extrafeatures;
        this.flat = flat;
        this.housetype = housetype;
        this.landline = landline;
        this.mobile = mobile;
        this.name = name;
        this.parking = parking;
        this.password = password;
        this.salutationid = salutationid;
        this.selectionFeature = selectionFeature;
        this.slotno = slotno;
        this.stickerno = stickerno;
        this.twofourwheel = twofourwheel;
        this.vmodel = vmodel;
    }

    protected Users(Parcel in) {
        building = in.readString();
        if (in.readByte() == 0) {
            carpetarea = null;
        } else {
            carpetarea = in.readInt();
        }
        editId = in.readString();
        emailid = in.readString();
        extrafeatures = in.readString();
        flat = in.readString();
        housetype = in.readString();
        landline = in.readString();
        mobile = in.readString();
        name = in.readString();
        parking = in.readString();
        password = in.readString();
        if (in.readByte() == 0) {
            salutationid = null;
        } else {
            salutationid = in.readInt();
        }
        slotno = in.readString();
        stickerno = in.readString();
        twofourwheel = in.readString();
        vmodel = in.readString();
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

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

    public String getEditId() {
        return editId;
    }

    public void setEditId(String editId) {
        this.editId = editId;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getExtrafeatures() {
        return extrafeatures;
    }

    public void setExtrafeatures(String extrafeatures) {
        this.extrafeatures = extrafeatures;
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

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSalutationid() {
        return salutationid;
    }

    public void setSalutationid(Integer salutationid) {
        this.salutationid = salutationid;
    }

    public List<Boolean> getSelectionFeature() {
        return selectionFeature;
    }

    public void setSelectionFeature(List<Boolean> selectionFeature) {
        this.selectionFeature = selectionFeature;
    }

    public String getSlotno() {
        return slotno;
    }

    public void setSlotno(String slotno) {
        this.slotno = slotno;
    }

    public String getStickerno() {
        return stickerno;
    }

    public void setStickerno(String stickerno) {
        this.stickerno = stickerno;
    }

    public String getTwofourwheel() {
        return twofourwheel;
    }

    public void setTwofourwheel(String twofourwheel) {
        this.twofourwheel = twofourwheel;
    }

    public String getVmodel() {
        return vmodel;
    }

    public void setVmodel(String vmodel) {
        this.vmodel = vmodel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(building);
        if (carpetarea == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(carpetarea);
        }
        dest.writeString(editId);
        dest.writeString(emailid);
        dest.writeString(extrafeatures);
        dest.writeString(flat);
        dest.writeString(housetype);
        dest.writeString(landline);
        dest.writeString(mobile);
        dest.writeString(name);
        dest.writeString(parking);
        dest.writeString(password);
        if (salutationid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(salutationid);
        }
        dest.writeString(slotno);
        dest.writeString(stickerno);
        dest.writeString(twofourwheel);
        dest.writeString(vmodel);
    }
}
