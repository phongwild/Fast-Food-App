package com.example.fastfood.Model;

public class Cart {
    private String cart_Id, user_Id, id;
    private int cart_quantity;

    public Cart() {
    }

    public Cart(String cart_Id, String user_Id, String id, int cart_quantity) {
        this.cart_Id = cart_Id;
        this.user_Id = user_Id;
        this.id = id;
        this.cart_quantity = cart_quantity;
    }

    public String getCart_Id() {
        return cart_Id;
    }

    public void setCart_Id(String cart_Id) {
        this.cart_Id = cart_Id;
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
}
