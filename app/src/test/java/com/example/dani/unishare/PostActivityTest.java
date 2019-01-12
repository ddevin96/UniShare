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
  private String ruolo1, ruolo2;
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
  }

  @Test
  public void trovaParole() {
    String stringa = "Ciao mondo";
    RicercaProfiloActivity act = new RicercaProfiloActivity();
    assertEquals(lista,act.trovaParole(stringa));
  }

  @Test
  public void isManagerTest(){
    MainActivity act = new MainActivity();
    act.ruoloManager = ruolo1;
    assertTrue(act.isManager());
    act.ruoloManager = ruolo2;
    assertFalse(act.isManager());
  }


}