package com.example.dani.unishare;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class UtenteTest {
    private Utente u;
    @Before
    public void setUp() throws Exception {
        try{
            u = new Utente();
        }catch(Exception e){
            System.out.println("Utente assente");
        }
        try{
            u = new Utente("abc","","Cioffi","F","10/12/97","donacio@gmail.com","abCD.123","utente");
        }catch(Exception e){
            System.out.println("Nome assente");
        }
        try{
            u = new Utente("abc","Donatella","","F","10/12/97","donacio@gmail.com","abCD.123","utente");
        }catch(Exception e){
            System.out.println("Cognome assente");
        }
        try{
            u = new Utente("abc","Donatella","Cioffi","","10/12/97","donacio@gmail.com","abCD.123","utente");
        }catch(Exception e){
            System.out.println("Il sesso è assente");
        }
        try{
            u = new Utente("abc","Donatella","Cioffi","F","10/12/97","","abCD.123","utente");
        }catch(Exception e){
            System.out.println("L'email è assente");
        }
        try{
            u = new Utente("abc","Donatella","Cioffi","F","10/12/97","donacio@gmail.com","","utente");
        }catch(Exception e){
            System.out.println("La password è assente");
        }
        try{
            u = new Utente("abc","Donatella","Cioffi","F","10/12/97","donacio@gmail.com","abCD.123","utente");
        }catch(Exception e){
            System.out.println("L'inserimento dovrebbe andare a buon fine");
        }
    }

    @Test
    public void getId() {
        assertEquals("abc",u.getId());
    }

    @Test
    public void getNome() {
        assertEquals("Donatella",u.getNome());
    }

    @Test
    public void getCognome() {
        assertEquals("Cioffi",u.getCognome());
    }

    @Test
    public void getSesso() {
        assertEquals("F",u.getSesso());
    }

    @Test
    public void getDataDiNascita() {
        assertEquals("10/12/97",u.getDataDiNascita());
    }

    @Test
    public void getEmail() {
        assertEquals("donacio@gmail.com",u.getEmail());
    }

    @Test
    public void getPassword() {
        assertEquals("abCD.123",u.getPassword());
    }

    @Test
    public void setId() {
        String id = "def";
        u.setId(id);
        assertEquals(id,u.getId());
    }

    @Test
    public void setNome() {
        String nome = "Luigi";
        u.setNome(nome);
        assertEquals(nome,u.getNome());
    }

    @Test
    public void setCognome() {
        String cognome = "Di Palma";
        u.setCognome(cognome);
        assertEquals(cognome,u.getCognome());
    }

    @Test
    public void setSesso() {
        String sesso = "M";
        u.setSesso(sesso);
        assertEquals(sesso,u.getSesso());
    }

    @Test
    public void setDataDiNascita() {
        String data = "10/12/97";
        u.setDataDiNascita(data);
        assertEquals(data,u.getDataDiNascita());
    }

    @Test
    public void setEmail() {
        String email = "ldpp@gmail.com";
        u.setEmail(email);
        assertEquals(email,u.getEmail());
    }

    @Test
    public void setPassword() {
        String password = "abcdef";
        u.setPassword(password);
        assertEquals(password,u.getPassword());
    }

    @Test
    public void getRuolo(){
        assertEquals("utente",u.getRuolo());
    }

    @Test
    public void setRuolo(){
        String ruolo = "manager";
        u.setRuolo(ruolo);
        assertEquals(ruolo,u.getRuolo());
    }

}