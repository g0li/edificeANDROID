package com.lilliemountain.edifice.POJO;


import java.util.ArrayList;
import java.util.List;

public class Events {

    private String residentName;
    private String title;
    private String description;
    private String flat;
    private String comments;
    private String building;
    private String date;
    private String email;
    private List<String> guests=new ArrayList<>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Events() {
    }

    /**
     *
     * @param building
     * @param title
     * @param residentName
     * @param description
     * @param flat
     * @param date
     * @param comments
     * @param email
     * @param guests
     */

    public Events(String residentName, String title, String description, String flat, String comments, String building, String date, String email, List<String> guests) {
        this.residentName = residentName;
        this.title = title;
        this.description = description;
        this.flat = flat;
        this.comments = comments;
        this.building = building;
        this.date = date;
        this.email = email;
        this.guests = guests;
    }



    public List<String> getGuests() {
        return guests;
    }

    public void setGuests(List<String> guests) {
        this.guests = guests;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
