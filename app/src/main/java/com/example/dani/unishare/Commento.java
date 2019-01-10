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

  /**
   *Classe Commento.
   *
   * @see Commento
   * @param id  Codice univoco che identifica il commento nel database.
   * @param description  Stringa che rappresenta il corpo del commento.
   * @param author  Stringa contenete il nome dell'utente che ha pubblicato il commento.
   * <p>L'autore e la descrizione sono visualizzati a video.</p>
   * @param authorId  Codice univoco che identifica l'autore del commento nel database.
   * @param data  Parametro contenente la data di pubblicazione del commento.
   */

  public Commento(String id, String description, String author, String authorId, Date data) {
    this.id = id;
    this.description = description;
    this.author = author;
    this.authorId = authorId;
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
