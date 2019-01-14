package com.example.dani.unishare;

import org.junit.Before;

import java.util.ArrayList;

public class UtenteListTest {
  RicercaProfiloActivity act;
  ArrayList<Utente> listaUtenti;
  UtenteList lista;
  @Before
  public void setUp() throws Exception {
    act = new RicercaProfiloActivity();
    listaUtenti = new ArrayList<>();
    listaUtenti.add(new Utente("MVD","Maria","Verdi","D",
            "15/02/1995","maria@libero.it","mariAA000!!","utente"));
    listaUtenti.add(new Utente("LDP","Luigi","Di Palma","M","18/04/1997",
            "dypalma@gmail.com","@Dip123456","utente"));
    lista = new UtenteList(act,listaUtenti);
  }


}