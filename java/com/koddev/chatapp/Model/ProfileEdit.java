package com.koddev.chatapp.Model;

public class ProfileEdit {

    private String email, contactNo, street, suburb, city, ZIP, username, password;

    //Default constructor
    public ProfileEdit(){

    }

    public ProfileEdit(String email, String contactNo, String street, String suburb, String city, String ZIP, String username, String password){
        this.email = email;
        this.contactNo = contactNo;
        this.street = street;
        this.suburb = suburb;
        this.city = city;
        this.ZIP = ZIP;
        this.username = username;
        this.password = password;
    }

    //Getter methods
    public String getEmail(){return email;}
    public String getContactNo(){return contactNo;}
    public String getStreet(){return street;}
    public String getSuburb(){return suburb;}
    public String getCity(){return city;}
    public String getZIP(){return ZIP;}
    public String getUsername(){return username;}
    public String getPassword() {return password;}

    //Setter methods
    public void setEmail(String email) {
        this.email =email;
    }

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

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

