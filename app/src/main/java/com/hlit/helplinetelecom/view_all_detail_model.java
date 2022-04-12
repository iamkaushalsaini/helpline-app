package com.hlit.helplinetelecom;

public class view_all_detail_model {

    private String mdate;
    private String cname;
    private String cmobile;
    private String caddress;
    private String cproduct;
    private String nextmdate;
    private String cstatus;
    private String actaken;
    private String tt;
    private String cus_type;

    public view_all_detail_model(String mdate, String cname, String cmobile, String caddress, String cproduct, String nextmdate, String cstatus, String actaken, String tt, String cus_type) {
        this.mdate = mdate;
        this.cname = cname;
        this.cmobile = cmobile;
        this.caddress = caddress;
        this.cproduct = cproduct;
        this.nextmdate = nextmdate;
        this.cstatus = cstatus;
        this.actaken = actaken;
        this.tt = tt;
        this.cus_type = cus_type;
    }

    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCmobile() {
        return cmobile;
    }

    public void setCmobile(String cmobile) {
        this.cmobile = cmobile;
    }

    public String getCaddress() {
        return caddress;
    }

    public void setCaddress(String caddress) {
        this.caddress = caddress;
    }

    public String getCproduct() {
        return cproduct;
    }

    public void setCproduct(String cproduct) {
        this.cproduct = cproduct;
    }

    public String getNextmdate() {
        return nextmdate;
    }

    public void setNextmdate(String nextmdate) {
        this.nextmdate = nextmdate;
    }

    public String getCstatus() {
        return cstatus;
    }

    public void setCstatus(String cstatus) {
        this.cstatus = cstatus;
    }

    public String getActaken() {
        return actaken;
    }

    public void setActaken(String actaken) {
        this.actaken = actaken;
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public String getCus_type() {
        return cus_type;
    }

    public void setCus_type(String cus_type) {
        this.cus_type = cus_type;
    }
}
