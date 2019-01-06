package com.example.dani.unishare;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class BachecaTest {

    private Bacheca b;
    @Before
    public void setUp(){

        try{
            b = new Bacheca("abc","","Descrizione","Daniele","ddv",new Date());
        }catch(Exception e){
            System.out.println("Titolo non presente.");
        }
        try{
            b = new Bacheca("abc","Germania","","Daniele","ddv",new Date());
        }catch(Exception e){
            System.out.println("Descrizione non presente.");
        }
        try{
            b = new Bacheca("abc","Germania","Descrizione","","ddv",new Date());
        }catch(Exception e){
            System.out.println("Autore non presente.");
        }
        try{
            b = new Bacheca("abc","Germania","Descrizione","Daniele","ddv",new Date());
        }catch(Exception e){
            System.out.println("Non dovrebbe lanciare eccezioni");
        }
    }

    @Test
    public void getId() {
        assertEquals("abc",b.getId());
    }

    @Test
    public void setId() {
        String id = "dadada";
        b.setId(id);
        assertEquals(id,b.getId());
    }

    @Test
    public void getTitle() {
        assertEquals("Germania",b.getTitle());
    }

    @Test
    public void setTitle() {
        String title = "Francia";
        b.setId(title);
        assertEquals(title,b.getTitle());
    }

    @Test
    public void getDescription() {
        assertEquals("Descrizione",b.getDescription());
    }

    @Test
    public void setDescription() {
        String description = "Descr";
        b.setDescription(description);
        assertEquals(description,b.getDescription());
    }

    @Test
    public void getAuthor() {
        assertEquals("Daniele",b.getAuthor());
    }

    @Test
    public void setAuthor() {
        String author = "Luigi";
        b.setAuthor(author);
        assertEquals(author,b.getAuthor());
    }

    @Test
    public void setAuthorId() {
        String id = "ldp";
        b.setAuthorId(id);
        assertEquals(id,b.getAuthorId());
    }

    @Test
    public void getAuthorId() {
        assertEquals("ddv",b.getAuthorId());
    }
/*
    @Test
    public void getDate() {

    }

    @Test
    public void setDate() {
    }
    */
}