package com.lilliemountain.edifice.POJO;

public class LocalServices{
    public String getWorkerNumber1() {
        return workerNumber1;
    }

    public void setWorkerNumber1(String workerNumber1) {
        this.workerNumber1 = workerNumber1;
    }

    public String getWorkerNumber2() {
        return workerNumber2;
    }

    public void setWorkerNumber2(String workerNumber2) {
        this.workerNumber2 = workerNumber2;
    }

    public String getWorkerCategory() {
        return workerCategory;
    }

    public void setWorkerCategory(String workerCategory) {
        this.workerCategory = workerCategory;
    }

    public String getWorkerOfficeAddress() {
        return workerOfficeAddress;
    }

    public void setWorkerOfficeAddress(String workerOfficeAddress) {
        this.workerOfficeAddress = workerOfficeAddress;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public LocalServices(String workerName, String workerNumber1, String workerNumber2, String workerCategory, String workerOfficeAddress) {
        this.workerName = workerName;
        this.workerNumber1 = workerNumber1;
        this.workerNumber2 = workerNumber2;
        this.workerCategory = workerCategory;
        this.workerOfficeAddress = workerOfficeAddress;
    }

    String workerName,workerNumber1,workerNumber2,workerCategory,workerOfficeAddress;

    public LocalServices() {
    }
}
