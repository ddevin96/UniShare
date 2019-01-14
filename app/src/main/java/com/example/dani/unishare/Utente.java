package com.example.dani.unishare;

public class Utente {

  private String id;
  private String nome;
  private String cognome;
  private String sesso;
  private String dataDiNascita;
  private String email;
  private String password;
  private String ruolo;

  public Utente() {

  }


  /**
     * Classe Utente.
     *
     * @see Utente
     * @param id   Codice univoco associato ad ogni utente inserito nel database.
     * @param nome Striga contenente il nome dell'utente.
     * @param cognome Stringa contenente il cognome dell'utente.
     * @param sesso Parametro contenente il genere dell'utente.
     * @param dataDiNascita Parametro contente la data di nascita dell'utente.
     * <p> Nome, cognome, sesso e data di nascita si riferiscono ai dati anagrafici dell'utente.</p>
     * @param email Stringa contenente l' e-mail dell'utente.
     * @param password Stringa contenente la password dell'utente.
     * <p>E-mail e password sono le credenziali che l'utente userà per accedere al sistema.</p>
     * @param ruolo Parametro che identifica il ruolo dell'utente
   *                che ha effettuato il login (utente/maager).
     */

  public Utente(String id, String nome, String cognome, String sesso, String dataDiNascita,
                String email, String password, String ruolo) {
    this.id = id;
    this.nome = nome;
    this.cognome = cognome;
    this.sesso = sesso;
    this.dataDiNascita = dataDiNascita;
    this.email = email;
    this.password = password;
    this.ruolo = ruolo;
  }

  public String getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public String getCognome() {
    return cognome;
  }

  public String getSesso() {
    return sesso;
  }

  public String getDataDiNascita() {
    return dataDiNascita;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setCognome(String cognome) {
    this.cognome = cognome;
  }

  public void setSesso(String sesso) {
    this.sesso = sesso;
  }

  public void setDataDiNascita(String dataDiNascita) {
    this.dataDiNascita = dataDiNascita;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRuolo() {
    return ruolo;
  }

  public void setRuolo(String ruolo) {
    this.ruolo = ruolo;
  }
}
