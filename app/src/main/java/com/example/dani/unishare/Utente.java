package com.example.dani.unishare;

import java.util.Date;

public class Utente {

    private String id;
    private String nome;
    private String cognome;
    private char sesso;
    private Date dataDiNascita;
    private String Email;
    private String password;


    public Utente(){

    }

    public Utente(String id, String nome, String cognome, char sesso, Date dataDiNascita, String email, String password) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.sesso = sesso;
        this.dataDiNascita = dataDiNascita;
        Email = email;
        this.password = password;
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

    public char getSesso() {
        return sesso;
    }

    public Date getDataDiNascita() {
        return dataDiNascita;
    }

    public String getEmail() {
        return Email;
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

    public void setSesso(char sesso) {
        this.sesso = sesso;
    }

    public void setDataDiNascita(Date dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
