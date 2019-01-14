package com.example.dani.unishare;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.lang.reflect.Method;
import java.security.DomainCombiner;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

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


  @Test
  public void getView() {
    MainActivity act = new MainActivity();
    act.listaBacheca = listaB;
    lista = new BachecaList(act,listaB);
    ListView listView = new ListView(act);
    View v = null;
    v = lista.getView(0,null,listView);
    assertNotNull(v);

  }


}