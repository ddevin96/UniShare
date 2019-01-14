package com.example.dani.unishare;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RicercaProfiloActivityTest {
  private List<String> lista;
  @Before
  public void setUp(){
    lista = new ArrayList<>();
    lista.add("Ciao");
    lista.add("mondo");
  }

  @Test
  public void trovaParole() {
    String stringa = "Ciao mondo";
    RicercaProfiloActivity act = new RicercaProfiloActivity();
    assertEquals(lista,act.trovaParole(stringa));
  }
}