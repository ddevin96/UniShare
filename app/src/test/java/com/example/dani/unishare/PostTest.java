package com.example.dani.unishare;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class PostTest {
    private Post p;
    private Date d = new Date();
    @Before
    public void setUp() throws Exception {
        try{
            p = new Post();
        }catch(Exception e){
            System.out.println("Post assente");
        }
        try{
            p = new Post("abc","","Descrizione","Daniele","ddv",d);
        }catch(Exception e){
            System.out.println("Titolo non presente.");
        }
        try{
            p = new Post("abc","Residenze","","Daniele","ddv",d);
        }catch(Exception e){
            System.out.println("Descrizione non presente.");
        }
        try{
            p = new Post("abc","Residenze","Descrizione","","ddv",d);
        }catch(Exception e){
            System.out.println("Autore non presente.");
        }
        try{
            p = new Post("abc","Residenze","Descrizione","Daniele","ddv",d);
        }catch(Exception e){
            System.out.println("Non dovrebbe lanciare eccezioni");
        }
    }

    @Test
    public void getId() {
        assertEquals("abc",p.getId());
    }

    @Test
    public void getDescription() {
        assertEquals("Descrizione",p.getDescription());
    }

    @Test
    public void getAuthor() {
        assertEquals("Daniele",p.getAuthor());
    }

    @Test
    public void getDate() {
        assertEquals(d,p.getDate());
    }

    @Test
    public void setId() {
        String id = "dadada";
        p.setId(id);
        assertEquals(id,p.getId());
    }

    @Test
    public void setDescription() {
        String description = "Descr";
        p.setDescription(description);
        assertEquals(description,p.getDescription());
    }

    @Test
    public void setAuthor() {
        String author = "Luigi";
        p.setAuthor(author);
        assertEquals(author,p.getAuthor());
    }

    @Test
    public void setDate() {
        p.setDate(d);
        assertEquals(d,p.getDate());
    }

    @Test
    public void getTitle() {
        assertEquals("Residenze",p.getTitle());
    }

    @Test
    public void setTitle() {
        String title = "Appartamenti";
        p.setTitle(title);
        assertEquals(title,p.getTitle());
    }

    @Test
    public void getAuthorId() {
        assertEquals("ddv",p.getAuthorId());
    }

    @Test
    public void setAuthorId() {
        String id = "ldp";
        p.setAuthorId(id);
        assertEquals(id,p.getAuthorId());
    }
}