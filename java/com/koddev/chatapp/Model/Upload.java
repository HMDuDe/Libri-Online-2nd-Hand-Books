package com.koddev.chatapp.Model;

public class Upload {
    private String ISBN, Author, Title, Condition, Status, Faculty, ImageURL, Publisher, price, year, date, id, sellerID, seller;
    public Upload() {
    } // required do not delete

    public Upload(long advertId, String isbn, String Author, String title,String sellerID, String seller, String faculty, String imageURL, String price, String year, String condition, String publisher, String date) {
        id = Long.toString(advertId);
        ISBN = isbn;
        Title = title;
        this.Author = Author;
        this.year = year;
        Faculty = faculty;
        ImageURL = imageURL;
        Condition = condition;
        this.price = price;
        Status = "Available";
        Publisher = publisher;
        this.date = date;
        this.sellerID= sellerID;
        this.seller=seller;
    }

    public Upload(String isbn, String Author, String title, String faculty, String year, String publisher) {
        ISBN = isbn;
        Title = title;
        this.Author = Author;
        this.year = year;
        Faculty = faculty;
        Publisher = publisher;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getFaculty() {
        return Faculty;
    }

    public void setFaculty(String faculty) {
        Faculty = faculty;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
