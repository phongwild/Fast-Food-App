package com.example.fastfood.Model;

public class ProductType {
    private String Product_Type_Id, Type_Name, Img_Product_Type;

    public String getImg_Product_Type() {
        return Img_Product_Type;
    }

    public void setImg_Product_Type(String img_Product_Type) {
        Img_Product_Type = img_Product_Type;
    }

    public String getProduct_Type_Id() {
        return Product_Type_Id;
    }

    public void setProduct_Type_Id(String product_Type_Id) {
        Product_Type_Id = product_Type_Id;
    }

    public String getType_Name() {
        return Type_Name;
    }

    public void setType_Name(String type_Name) {
        Type_Name = type_Name;
    }

    public ProductType(){}

    public ProductType(String product_Type_Id, String type_Name, String Img_Product_Type) {
        Product_Type_Id = product_Type_Id;
        Img_Product_Type = Img_Product_Type;
        Type_Name = type_Name;
    }
}
