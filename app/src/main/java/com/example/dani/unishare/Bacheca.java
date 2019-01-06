package com.example.dani.unishare;

import java.util.Date;

public class Bacheca {
    private String id;
    private String title;
    private String description;
    private String author;
    private String authorId;
    private Date date;

    public Bacheca() {

    }

    public Bacheca(String id, String title, String description, String author, String authorId, Date data) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.authorId= authorId;
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

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
