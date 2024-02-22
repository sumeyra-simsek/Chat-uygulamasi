package com.sumeyrasimsek.projedersi.Model;

public class News {
    private String aciklama;
    private String href;
    private String id;
    private String title;
    private String title_date;

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public News() {
    }

    public News(String aciklama,String href, String id, String title, String title_date) {
        this.aciklama=aciklama;
        this.href = href;
        this.id = id;
        this.title = title;
        this.title_date = title_date;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_date() {
        return title_date;
    }

    public void setTitle_date(String title_date) {
        this.title_date = title_date;
    }
}

