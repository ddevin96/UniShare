package com.example.dani.unishare;

import java.util.ArrayList;
import java.util.Date;

public class Post {

    private String id;
    private String title;
    private String description;
    private String author;
    private Date date;
    private ArrayList<Commento> commenti = new ArrayList<>();

    public Post(String id, String title, String description, String author, Date data){

    }

    public Post(String id, String description, String author, Date date, ArrayList<Commento> commenti) {
        this.id = id;
        this.description = description;
        this.author = author;
        this.date = date;
        this.commenti = commenti;
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

    public ArrayList<Commento> getCommenti() {
        return commenti;
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

    public void setCommenti(ArrayList<Commento> commenti) {
        this.commenti = commenti;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
