package com.example.fastfood.Model;

public class Bill {
    private String bill_Id, user_Id, purchase_Date, Address;
    private int total_Amount,status;
    private String note, time;
    public Bill() {
    }

    public Bill(String bill_Id, String user_Id, String purchase_Date, String address, int total_Amount, int status, String note, String time) {
        this.bill_Id = bill_Id;
        this.user_Id = user_Id;
        this.purchase_Date = purchase_Date;
        Address = address;
        this.total_Amount = total_Amount;
        this.status = status;
        this.note = note;
        this.time = time;
    }

    public String getBill_Id() {
        return bill_Id;
    }

    public void setBill_Id(String bill_Id) {
        this.bill_Id = bill_Id;
    }

    public String getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(String user_Id) {
        this.user_Id = user_Id;
    }

    public String getPurchase_Date() {
        return purchase_Date;
    }

    public void setPurchase_Date(String purchase_Date) {
        this.purchase_Date = purchase_Date;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getTotal_Amount() {
        return total_Amount;
    }

    public void setTotal_Amount(int total_Amount) {
        this.total_Amount = total_Amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
