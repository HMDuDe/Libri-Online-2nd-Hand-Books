package com.koddev.chatapp.Model;

public class CartItem {
    private String title, price, imageURL, quantity, ad_id,buyerid;
    private long itemid;

    public CartItem(){}// required do not delete

    public CartItem(long id, String Title, String Price, String imageURL,String ad_id, String buyer ) {
        //buyer = buyerid;
        //seller = sellerid;
        title = Title;
        quantity = Integer.toString(1);
        price = Price;
        this.imageURL = imageURL;
        this.itemid = id;
        this.ad_id = ad_id;
        buyerid = buyer;
    }

    public String getBuyerid() {
        return buyerid;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public long getItemid() {
        return itemid;
    }

    public void setItemid(long itemid) {
        this.itemid = itemid;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getQuantity() {
        return quantity;
    }
}
