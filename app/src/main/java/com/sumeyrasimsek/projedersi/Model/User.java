package com.sumeyrasimsek.projedersi.Model;

public class User {
    private String Name;
    private String Password;
    private String id;
    private String imgUrl;
    private String mail;
    private String konum;

    public String getKonum() {
        return konum;
    }

    public void setKonum(String konum) {
        this.konum = konum;
    }

    public User(String name, String password, String id, String imgUrl, String mail, String konum) {
        Name = name;
        Password = password;
        this.id = id;
        this.imgUrl = imgUrl;
        this.mail = mail;
        this.konum=konum;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User() {
    }



    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
