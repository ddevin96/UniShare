package com.example.dani.unishare;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class UtenteTest {
    private Utente u;
    @Before
    public void setUp() throws Exception {
        try{
            u = new Utente("abc","","Cioffi","F",new Date(1997,06,27),"donacio@gmail.com","abCD.123");
        }catch(Exception e){
            System.out.println("Nome assente");
        }
        try{
            u = new Utente("abc","Donatella","","F",new Date(1997,06,27),"donacio@gmail.com","abCD.123");
        }catch(Exception e){
            System.out.println("Cognome assente");
        }
        try{
            u = new Utente("abc","Donatella","Cioffi","",new Date(1997,06,27),"donacio@gmail.com","abCD.123");
        }catch(Exception e){
            System.out.println("Il sesso è assente");
        }
        try{
            u = new Utente("abc","Donatella","Cioffi","F",new Date(1997,06,27),"","abCD.123");
        }catch(Exception e){
            System.out.println("L'email è assente");
        }
        try{
            u = new Utente("abc","Donatella","Cioffi","F",new Date(1997,06,27),"donacio@gmail.com","");
        }catch(Exception e){
            System.out.println("La password è assente");
        }
        try{
            u = new Utente("abc","Donatella","Cioffi","F",new Date(1997,06,27),"donacio@gmail.com","abCD.123");
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
        assertEquals(new Date(1997,06,27),u.getDataDiNascita());
    }

    @Test
    public void getEmail() {
        assertEquals("donacio@gmail.com",u.getEmail());
    }

    @Test
    public void getPassword() {
        assertEquals("1234",u.getPassword());
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
        Date data = new Date(1997,01,01);
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

}