package com.example.dani.unishare;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class CommentiActivityTest {
  private List<String> lista;
  CommentiActivity act;
  String stringa;
  @Before
  public void setUp(){
    act = new CommentiActivity();
    //Inizializza dati per il testing di trovaParole
    lista = new ArrayList<>();
    lista.add("Ciao");
    lista.add("mondo");
    stringa = "";
    for(int i=0;i<65536;i++){
      stringa = stringa.concat("a");
    }

  }

  @Test
  public void trovaParole() {
    String stringa = "Ciao mondo";
    assertEquals(lista,act.trovaParole(stringa));
  }

  @Test
  public void isManager() {
    act.ruolo = "manager";
    assertTrue(act.isManager());
    act.ruolo = "utente";
    assertFalse(act.isManager());
  }

  @Test
  public void controlloDescrizioneTest(){
    //Caso 1: descrizione vuota
    assertTrue(act.controlloDescrizione(""));
    //Caso 2: descrizione supera il limite massimo di caratteri
    assertTrue(act.controlloDescrizione(stringa));
    //Caso 3: descrizione corretta
    assertFalse(act.controlloDescrizione("Descrizione"));
  }

}