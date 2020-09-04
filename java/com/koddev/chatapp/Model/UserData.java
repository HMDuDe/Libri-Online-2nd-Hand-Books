package com.koddev.chatapp.Model;

import java.io.Serializable;

public class UserData implements Serializable {
    private String email, nameSurname, contactNo, street, suburb, city, ZIP, imgPath, username, password;

    //Default Constructor
    public UserData(){

    }

    public UserData(String email,
                    String contactNo,
                    String street,
                    String suburb,
                    String city,
                    String ZIP,
                    String nameSurname,
                    String imgPath,
                    String username,
                    String password){

        this.email = email;
        this.contactNo = contactNo;
        this.street = street;
        this.suburb = suburb;
        this.city = city;
        this.ZIP = ZIP;
        this.nameSurname = nameSurname;
        this.imgPath = imgPath;
        this.username = username;
        this.password = password;

    }

    //Setters
    public void setContactNo(String contactNo){
        this.contactNo = contactNo;
    }

    public void setStreet(String street){
        this.street = street;
    }

    public void setSuburb(String suburb){
        this.suburb = suburb;
    }

    public void setCity(String city){
        this.city = city;
    }

    public void setZIP(String ZIP){
        this.ZIP = ZIP;
    }

    public void setNameSurname(String nameSurname){
        this.nameSurname = nameSurname;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //Getetrs
    public String getEmail() {
        return email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getStreet() {
        return street;
    }

    public String getSuburb() {
        return suburb;
    }

    public String getCity() {
        return city;
    }

    public String getZIP() {
        return ZIP;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
