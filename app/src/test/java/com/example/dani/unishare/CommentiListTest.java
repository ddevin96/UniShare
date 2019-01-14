package com.example.dani.unishare;

import org.junit.Before;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentiListTest {
   List<Commento> listaC;
  CommentiList mCommentiList;
  CommentiActivity act;
  @Before
  public void setUp() throws Exception {
    act = new CommentiActivity();
    listaC = new ArrayList<>();
    listaC.add(new Commento("AS","Descri","Mario","MAR",new Date()));
    listaC.add(new Commento("AB","Descr","Maria","MRI",new Date()));
    act.lista = listaC ;
    mCommentiList = new CommentiList(act, listaC);
  }
}