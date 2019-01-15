package com.example.dani.unishare;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;
import static org.robolectric.Shadows.shadowOf;


public class MainActivityTest {

  private String ruolo1, ruolo2,stringaLunga;
  private ArrayList<Bacheca> lista;
  MainActivity act;
  @Before
  public void setUp() throws Exception {
    act = new MainActivity();
    //Inizializzazioni per testing di isManager
    ruolo1 = "manager";
    ruolo2 = "utente";
    //Inizializzazioni per testing di confrontaBacheche
    lista = new ArrayList<>();
    lista.add(new Bacheca("FRC","Francia","Descrizione","Luigi","LDP",new Date()));
    lista.add(new Bacheca("SPA","Spagna","Descrizione","Luigi","LDP",new Date()));
    lista.add(new Bacheca("SVI","Svizzera","Descrizione","Federica","FUN",new Date()));
    //Inizializzazione per controlloDescrizione
    stringaLunga = "";
    for(int i = 0;i<201;i++){
      stringaLunga = stringaLunga.concat("a");
    }
  }

  @Test
  public void confrontaBacheche() {
    act.listaBacheca = lista;
    assertTrue(act.confrontaBacheche(lista.get(0).getTitle(),"FRA"));
    assertFalse(act.confrontaBacheche("Italia","ita"));
  }

  @Test
  public void isManager() {
    act.ruoloManager = ruolo1;
    assertTrue(act.isManager());
    act.ruoloManager = ruolo2;
    assertFalse(act.isManager());
  }

  @Test
  public void controlloTitolo(){
    act.listaBacheca = lista;
    //Caso 1 : titolo vuoto
    assertTrue(act.controlloTitolo("","abc"));
    //Caso 2 : titolo supera i 20 caratteri
    assertTrue(act.controlloTitolo(stringaLunga,"abc"));
    //Caso 3 : bacheca già esiste
    assertTrue(act.controlloTitolo(lista.get(0).getTitle(),"FRA"));
    //Caso 4 : bacheca inserita
    assertFalse(act.controlloTitolo("Svezia","SVE"));

  }

  @Test
  public void controlloDescrizione(){
    //Caso 1 : descrizione vuota
    assertTrue(act.controlloDescrizione(""));
    //Caso 2 : descrizione supera 200 caratteri
    assertTrue(act.controlloDescrizione(stringaLunga));
    //Caso 3 : descrizione accettata
    assertFalse(act.controlloDescrizione("Descrizione"));
  }
}