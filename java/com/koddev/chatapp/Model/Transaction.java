package com.koddev.chatapp.Model;

public class Transaction {
    private String date, total, buyerID, books;
    private long transID;

    public Transaction(){

    }

    public Transaction(long id, String date, String total, String buyerID, String booklist) {
        transID =id;
        this.date=date;
        this.total=total;
        this.buyerID = buyerID;
        books = booklist;
    }

    public String getBooks() {
        return books;
    }

    public void setBooks(String books) {
        this.books = books;
    }

    public String getBuyerID() {
        return buyerID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public long getTransID() {
        return transID;
    }


}
