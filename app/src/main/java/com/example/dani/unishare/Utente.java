package com.example.dani.unishare;

import java.util.Date;

public class Utente {

    private String id;
    private String nome;
    private String cognome;
    private String sesso;
    private Date dataDiNascita;
    private String email;
    private String password;
    private String ruolo;

    public Utente(){

    }

    public Utente(String id, String nome, String cognome, String sesso, Date dataDiNascita, String email, String password, String ruolo) {
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

    public Date getDataDiNascita() {
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

    public void setDataDiNascita(Date dataDiNascita) {
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
