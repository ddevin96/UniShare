package com.example.dani.unishare;

import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.robolectric.Shadows.shadowOf;


public class MainActivityTest {

  private String ruolo1, ruolo2;
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
}