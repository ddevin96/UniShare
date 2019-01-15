package com.example.dani.unishare;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;

public class PostActivityTest {
  private List<String> lista;
  private ArrayList<Post> listaPost;
  private String ruolo1, ruolo2, stringaLunga;
  @Before
  public void setUp(){
    //Inizializza dati testing trovaParole
    lista = new ArrayList<>();
    lista.add("Ciao");
    lista.add("mondo");

    //Inizializza dati testing isManagerTest
    ruolo1 = "manager";
    ruolo2 = "utente";

    //Inizializzazioni per testing di confrontaBacheche
    listaPost = new ArrayList<>();
    listaPost.add(new Post("FRC","Info","Descrizione","Luigi","LDP",new Date()));
    listaPost.add(new Post("SPA","Appartamenti","Descrizione","Luigi","LDP",new Date()));
    listaPost.add(new Post("SVI","Residenze","Descrizione","Federica","FUN",new Date()));

    stringaLunga = "";
    for(int i=0;i<65535;i++){
      stringaLunga = stringaLunga.concat("a");
    }
  }

  @Test
  public void trovaParole() {
    String stringa = "Ciao mondo";
    PostActivity act = new PostActivity();
    assertEquals(lista,act.trovaParole(stringa));
  }

  @Test
  public void isManagerTest(){
    PostActivity act = new PostActivity();
    act.ruoloUser = ruolo1;
    assertTrue(act.isManager());
    act.ruoloUser = ruolo2;
    assertFalse(act.isManager());
  }

  @Test
  public void controllaParametroTest(){
    PostActivity act = new PostActivity();
    //Caso 1 : Stringa vuota
    assertTrue(act.controllaParametro(""));
    //Caso 2 : stringa che supera il limite massimo di caratteri
    assertTrue(act.controllaParametro(stringaLunga));
    //Caso 3 : stringa ok
    assertFalse(act.controllaParametro("abc"));
  }



}