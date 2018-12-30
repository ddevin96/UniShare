package com.example.dani.unishare;

import java.util.Date;

public class Bacheca {
    private String id;
    private String title;
    private String description;
    private String author; //tipo utente???
    // array di post
    private Date date;

    public Bacheca() {

    }

    public Bacheca(String id, String title, String description, String author, Date data) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.date = data;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getData() {
        return date;
    }

    public void setData(Date data) {
        this.date = data;
    }
}
