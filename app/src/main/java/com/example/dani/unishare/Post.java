package com.example.dani.unishare;

import java.util.Date;

public class Post {

  private String id;
  private String title;
  private String description;
  private String author;
  private String authorId;
  private Date date;


  public Post() {

  }

  /**
   * Classe Post.
   *
   * @see Post
   * @param id  Codice univoco che identifica il post nel database.
   * @param title  Stringa di presentazione del post all'interno della bacheca.
   * @param description Stringa in cui vi è il corpo del post.
   * @param author  Utente loggato che ha scritto il post.
   * <p>L'autore, il titolo e la descrizione verrano visualizzati a video.</p>
   * @param authorId  Codice univoco che identifica l'autore del post nel database.
   * @param date  Data in cui il post è stato pubblicato.
   */

  public Post(String id, String title, String description,
              String author, String authorId, Date date) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.author = author;
    this.authorId = authorId;
    this.date = date;

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

}
