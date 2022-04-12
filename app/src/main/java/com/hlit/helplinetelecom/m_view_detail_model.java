package com.hlit.helplinetelecom;

public class m_view_detail_model {

    private String mdate;
    private String role;

    public m_view_detail_model(String mdate, String role) {
        this.mdate = mdate;
        this.role = role;
    }

    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
