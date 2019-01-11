package com.example.dani.unishare;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class CommentoTest {
    private Commento c;
    private Date d = new Date();
    @Before
    public void setUp() throws Exception {
        try{
            c = new Commento();
        }catch(Exception e){
            System.out.println("Commento assente");
        }
        try{
            c = new Commento("abc","","Donatella","dcf",d);
        }catch(Exception e){
            System.out.println("Descrizione assente");
        }
        try{
            c = new Commento("abc","Descrizione","","dcf",d);
        }catch(Exception e){
            System.out.println("Autore assente");
        }
        try{
            c = new Commento("abc","Descrizione","Donatella","dcf",d);
        }catch(Exception e){
            System.out.println("L'inserimento dovrebbe essere andato a buon fine");
        }
    }

    @Test
    public void getId() {
        assertEquals("abc",c.getId());
    }

    @Test
    public void setId() {
        String id = "def";
        c.setId(id);
        assertEquals(id,c.getId());
    }

    @Test
    public void getDescription() {
        assertEquals("Descrizione",c.getDescription());
    }



    @Test
    public void setDescription() {
        String descrizione = "Descr";
        c.setDescription(descrizione);
        assertEquals(descrizione,c.getDescription());
    }

    @Test
    public void getAuthor() {
        assertEquals("Donatella",c.getAuthor());
    }

    @Test
    public void setAuthor() {
        String author = "Federica";
        c.setAuthor(author);
        assertEquals(author,c.getAuthor());
    }


    @Test
    public void getAuthorId() {
        assertEquals("dcf",c.getAuthorId());
    }

    @Test
    public void setAuthorId() {
        String id = "feu";
        c.setAuthorId(id);
        assertEquals(id,c.getAuthorId());
    }

    @Test
    public void setDate() {

        c.setData(d);
        assertEquals(d,c.getData());
    }
    @Test
    public void getDate() {
        assertEquals(d,c.getData());
    }
}