package com.lilliemountain.edifice.POJO;


public class Salutations {

    private int id;
    private String salutation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public Salutations() {
    }

    public Salutations(int id, String salutation) {
        this.id = id;
        this.salutation = salutation;
    }
}