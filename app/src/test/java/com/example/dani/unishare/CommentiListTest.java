package com.example.dani.unishare;

import android.view.View;
import android.widget.ListView;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class CommentiListTest {
  private List<Commento> lista;
  CommentiList mCommentiList;
  CommentiActivity act;
  @Before
  public void setUp() throws Exception {
    lista = new ArrayList<>();
    lista.add(new Commento("AS","Descri","Mario","MAR",new Date()));
    lista.add(new Commento("AB","Descr","Maria","MRI",new Date()));
    act = new CommentiActivity();
    mCommentiList = new CommentiList(act, lista);
  }

  @Test
  public void getViewTest(){
    act.lista = lista;
    ListView listView = new ListView(act);
    mCommentiList.getView(0,null,listView);
  }
}