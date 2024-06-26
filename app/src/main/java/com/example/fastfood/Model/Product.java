package com.example.fastfood.Model;

public class Product {
    private String id, Describe, Img_Product, Name, Product_Type_Id;
    private int Price;

    public  Product(){}

    public Product(String id, String describe, String img_Product, String name, String product_Type_Id, int price) {
        this.id = id;
        Describe = describe;
        Img_Product = img_Product;
        Name = name;
        Product_Type_Id = product_Type_Id;
        Price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescribe() {
        return Describe;
    }

    public void setDescribe(String describe) {
        Describe = describe;
    }

    public String getImg_Product() {
        return Img_Product;
    }

    public void setImg_Product(String img_Product) {
        Img_Product = img_Product;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProduct_Type_Id() {
        return Product_Type_Id;
    }

    public void setProduct_Type_Id(String product_Type_Id) {
        Product_Type_Id = product_Type_Id;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }
}
