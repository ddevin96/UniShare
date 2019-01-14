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

  /**
   * Classe Bacheca.
   *
   * @see Bacheca
   * @param id  Codice univoco che identifica la bacheca all'interno del database.
   * @param title  Stringa di presentazione della bacheca all'interno della home.
   * @param description  Stringa in cui si descrive l'oggetto.
   * @param author  Stringa in cui si specifica l'utente che ha pubblicato la bacheca.
   * <p>Solo i manager sono autorizzati a pubblicare nuove bacheche.</p>
   * @param authorId  Codice univoco he identifica l'autore della baceca.
   * @param data  Parametro contenente la data in  cui viene pubblicato il post.
   */

  public Bacheca(String id, String title, String description,
                 String author, String authorId, Date data) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.author = author;
    this.authorId = authorId;
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
