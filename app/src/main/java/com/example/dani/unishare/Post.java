package com.example.dani.unishare;

import java.util.ArrayList;
import java.util.Date;

public class Post {

    private String id;
    private String title;
    private String description;
    private String author;
    private String authorId;
    private Date date;
    private String idBacheca;


    public Post(){

    }

    public Post(String id, String title, String description, String author,String authorId, Date date, String idBacheca) {
        this.id = id;
        this.title=title;
        this.description = description;
        this.author = author;
        this.authorId = authorId;
        this.date = date;
        this.idBacheca= idBacheca;

    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public Date getDate() {
        return date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }


    public String getIdBacheca() {
        return idBacheca;
    }

    public void setIdBacheca(String idBacheca) {
        this.idBacheca = idBacheca;
    }
}
