package com.example.fastfood.Model;

public class Bill_Detail {
    private String invoice_detail_id, user_Id, id;
    private int cart_quantity, Price, total_details;
    private String bill_Id;
    public Bill_Detail() {
    }
    public Bill_Detail(String invoice_detail_id, String user_Id, String id, int cart_quantity, int price, int total_details) {
        this.invoice_detail_id = invoice_detail_id;
        this.user_Id = user_Id;
        this.id = id;
        this.cart_quantity = cart_quantity;
        Price = price;
        this.total_details = total_details;
    }
    public String getInvoice_detail_id() {
        return invoice_detail_id;
    }

    public void setInvoice_detail_id(String invoice_detail_id) {
        this.invoice_detail_id = invoice_detail_id;
    }

    public String getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(String user_Id) {
        this.user_Id = user_Id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCart_quantity() {
        return cart_quantity;
    }

    public void setCart_quantity(int cart_quantity) {
        this.cart_quantity = cart_quantity;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int getTotal_details() {
        return total_details;
    }

    public void setTotal_details(int total_details) {
        this.total_details = total_details;
    }

    public String getBill_Id() {
        return bill_Id;
    }

    public void setBill_Id(String bill_Id) {
        this.bill_Id = bill_Id;
    }
}
