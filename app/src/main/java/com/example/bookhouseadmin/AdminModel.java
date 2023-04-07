package com.example.bookhouseadmin;

public class AdminModel {
    String name,email,password,phone,uid,post;

    public AdminModel(String name, String email, String password, String phone, String uid,String post) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.uid = uid;
        this.post=post;
    }

    public AdminModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
