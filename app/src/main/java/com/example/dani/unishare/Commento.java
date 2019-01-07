package com.example.dani.unishare;

import java.util.Date;

public class Commento {
    private String id;
    private String description;
    private String author;
    private String authorId;
    private Date data;
    private String idPost;

    public Commento() {

    }

    public Commento(String id, String description, String author, String authorId, Date data, String idPost) {
        this.id = id;
        this.description = description;
        this.author = author;
        this.authorId= authorId;
        this.data = data;
        this.idPost= idPost;
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

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }
}
