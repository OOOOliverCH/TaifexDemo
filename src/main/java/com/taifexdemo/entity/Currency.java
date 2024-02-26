package com.taifexdemo.entity;

//成交匯率實體類
public class Currency {

    private String id;
    private String date;
    private String usd_ntd;
    private String currency;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsd_ntd() {
        return usd_ntd;
    }

    public void setUsd_ntd(String usd_ntd) {
        this.usd_ntd = usd_ntd;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
