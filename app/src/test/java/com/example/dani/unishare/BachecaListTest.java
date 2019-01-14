package com.example.dani.unishare;

import org.junit.Before;

import java.util.ArrayList;
import java.util.Date;

public class BachecaListTest {
  private ArrayList<Bacheca> listaB;
  private BachecaList lista;
  @Before
  public void setUp() throws Exception {
    listaB = new ArrayList<>();
    listaB.add(new Bacheca("FRC","Francia","Descrizione","Luigi","LDP",new Date()));
    listaB.add(new Bacheca("SPA","Spagna","Descrizione","Luigi","LDP",new Date()));
    listaB.add(new Bacheca("SVI","Svizzera","Descrizione","Federica","FUN",new Date()));
    MainActivity act = new MainActivity();
    act.listaBacheca = listaB;
    lista = new BachecaList(act,listaB);
  }




}