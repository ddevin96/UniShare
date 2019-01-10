package com.example.dani.unishare;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class PostActivityTest {
  private ArrayList<Post> lista;

  //Inizializza la lista dei post
  @Before
  public void setUp() throws Exception {
    lista = new ArrayList<>();
    lista.add(new Post("ABC","Residenze","Cerco informazioni sulle residenze universitarie","Mario96","MRS",new Date()));
    lista.add(new Post("DEF","Appartamenti","Cerco informazioni sugli appartamenti","Checco","FRS",new Date()));
  }

  @Test
  public void confrontaPostTest(){
    String titoloPresente = lista.get(0).getAuthor();
    String titoloAssente = "Info";
    assertTrue(confrontaPost(titoloPresente));
    assertFalse(confrontaPost(titoloAssente));
  }


  //Metodo da testare  in PostActivity
  private boolean confrontaPost(String titolo) {
    boolean value = true;
    if (!lista.isEmpty()) {
      for (Post post : lista) {
        if (post.getTitle().equals(titolo)) {
          value = true;
          break;
        } else {
          value = false;
        }
      }
    } else {
      return false;
    }
    return value;
  }

}