package com.lilliemountain.edifice.POJO;


public class Committee {

    private String building;
    private String email;
    private String flat;
    private String name;
    private String phone;
    private String role;

    /**
     * No args constructor for use in serialization
     *
     */
    public Committee() {
    }

    /**
     *
     * @param phone
     * @param building
     * @param email
     * @param name
     * @param flat
     * @param role
     */

    public Committee(String building, String email, String flat, String name, String phone, String role) {
        this.building = building;
        this.email = email;
        this.flat = flat;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
