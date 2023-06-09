package com.example.bookhouseadmin;

import android.content.Intent;

public class BooksDetails {
    private String bookname,image,writername,catagory,bookid,edition,description;
    private Integer numofbook,totalnumofbook;
    private Boolean available;
    public BooksDetails(String bookname, String image, String writername, String catagory, String bookid, Boolean available,Integer numofbook,String edition,String description,Integer totalnumofbook) {
        this.bookname = bookname;
        this.image = image;
        this.writername = writername;
        this.catagory = catagory;
        this.bookid = bookid;
        this.available = available;
        this.numofbook=numofbook;
        this.edition=edition;
        this.description=description;
        this.totalnumofbook=totalnumofbook;
    }
    public BooksDetails() {
    }
    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWritername() {
        return writername;
    }

    public void setWritername(String writername) {
        this.writername = writername;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Integer getNumofbook() {
        return numofbook;
    }

    public void setNumofbook(Integer numofbook) {
        this.numofbook = numofbook;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalnumofbook() {
        return totalnumofbook;
    }

    public void setTotalnumofbook(Integer totalnumofbook) {
        this.totalnumofbook = totalnumofbook;
    }
}
