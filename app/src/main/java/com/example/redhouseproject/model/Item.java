package com.example.redhouseproject.model;

public class Item {

    private int id;
    private String itemName;
    private String itemSize;
    private String itemColour;
    private String itemPrice;
    private String itemImage;


    public Item(int id, String name, String size, String colour, String price,String image) {
        this.id = id;
        this.itemName = name;
        this.itemSize = size;
        this.itemColour = colour;
        this.itemPrice = price;
        this.itemImage = image;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public String getItemColour() {
        return itemColour;
    }

    public void setItemColour(String itemColour) {
        this.itemColour = itemColour;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemImage() {return itemImage;}

    public void setItemImage(String itemImage) {this.itemImage = itemImage;}

}
