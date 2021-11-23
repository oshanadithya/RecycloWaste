package com.example.recyclowaste.model;

public class Advertisment {

    private String Title;
    private String Description;
    private String Image;
    private float Price;
    private int Quantity;
    private int Ad_ID;
 //   private String Username;

   /* public String getUsername() {
        return Username;
    }*/

/*    public void setUsername(String username) {
        Username = username;
    }*/
    public Advertisment(){

    }

    public Advertisment(String title, String description, String image, float price, int quantity /*String username */) {
        Title = title;
        Description = description;
        Image = image;
        Price = price;
        Quantity = quantity;
      //  Username = username;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
