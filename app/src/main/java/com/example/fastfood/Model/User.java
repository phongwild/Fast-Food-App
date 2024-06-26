package com.example.fastfood.Model;

public class User {
    private String User_Id, User_Name, Pass_Word;
    private int Role;
    private String Email, Full_Name, Address;
    private int Phone_Number;
    private String User_Image;

    public User() {
    }

    public User(String user_Id, String user_Name, String pass_Word, int role, String email, String full_Name, String address, int phone_Number, String user_Image) {
        User_Id = user_Id;
        User_Name = user_Name;
        Pass_Word = pass_Word;
        Role = role;
        Email = email;
        Full_Name = full_Name;
        Address = address;
        Phone_Number = phone_Number;
        User_Image = user_Image;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getPass_Word() {
        return Pass_Word;
    }

    public void setPass_Word(String pass_Word) {
        Pass_Word = pass_Word;
    }

    public int getRole() {
        return Role;
    }

    public void setRole(int role) {
        Role = role;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFull_Name() {
        return Full_Name;
    }

    public void setFull_Name(String full_Name) {
        Full_Name = full_Name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getPhone_Number() {
        return Phone_Number;
    }

    public void setPhone_Number(int phone_Number) {
        Phone_Number = phone_Number;
    }

    public String getUser_Image() {
        return User_Image;
    }

    public void setUser_Image(String user_Image) {
        User_Image = user_Image;
    }
}
