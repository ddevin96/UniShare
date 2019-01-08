package com.example.dani.unishare;

import java.util.Date;

public class Commento {
    private String id;
    private String description;
    private String author;
    private String authorId;
    private Date data;

    public Commento() {

    }

    public Commento(String id, String description, String author, String authorId, Date data) {
        this.id = id;
        this.description = description;
        this.author = author;
        this.authorId= authorId;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

}
