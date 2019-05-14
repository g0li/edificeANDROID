package com.lilliemountain.edifice.POJO;

public class Tenant {
    String tenantName;
    String tenantphone;
    String tenantemail;
    String tenantbaseprice;
    String tenantemergencyname;
    String tenantemergencyphone;
    String gridy;

    public Tenant() {
    }

    public Tenant(String tenantName, String tenantphone, String tenantemail, String tenantbaseprice, String tenantemergencyname, String tenantemergencyphone, String gridy) {
        this.tenantName = tenantName;
        this.tenantphone = tenantphone;
        this.tenantemail = tenantemail;
        this.tenantbaseprice = tenantbaseprice;
        this.tenantemergencyname = tenantemergencyname;
        this.tenantemergencyphone = tenantemergencyphone;
        this.gridy = gridy;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantphone() {
        return tenantphone;
    }

    public void setTenantphone(String tenantphone) {
        this.tenantphone = tenantphone;
    }

    public String getTenantemail() {
        return tenantemail;
    }

    public void setTenantemail(String tenantemail) {
        this.tenantemail = tenantemail;
    }

    public String getTenantbaseprice() {
        return tenantbaseprice;
    }

    public void setTenantbaseprice(String tenantbaseprice) {
        this.tenantbaseprice = tenantbaseprice;
    }

    public String getTenantemergencyname() {
        return tenantemergencyname;
    }

    public void setTenantemergencyname(String tenantemergencyname) {
        this.tenantemergencyname = tenantemergencyname;
    }

    public String getTenantemergencyphone() {
        return tenantemergencyphone;
    }

    public void setTenantemergencyphone(String tenantemergencyphone) {
        this.tenantemergencyphone = tenantemergencyphone;
    }

    public String getGridy() {
        return gridy;
    }

    public void setGridy(String gridy) {
        this.gridy = gridy;
    }

    @Override
    public String toString() {

        String ss=
        "tenantName: "+tenantName+"\n"+
        "tenantphone: "+tenantphone+"\n"+
        "tenantemail: "+tenantemail+"\n"+
        "tenantbaseprice: "+tenantbaseprice+"\n"+
        "tenantemergencyname: "+tenantemergencyname+"\n"+
        "tenantemergencyphone: "+tenantemergencyphone+"\n"+
        "gridy: "+gridy+"\n";
        return ss;
    }
}
