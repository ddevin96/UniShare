package com.example.dani.unishare;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UtenteListTest {
  RicercaProfiloActivity act;
  ArrayList<Utente> listaUtenti;
  @Before
  public void setUp() throws Exception {
    act = new RicercaProfiloActivity();
    listaUtenti = new ArrayList<>();
    listaUtenti.add(new Utente("MVD","Maria","Verdi","D",
            "15/02/1995","maria@libero.it","mariAA000!!","utente"));
    listaUtenti.add(new Utente("LDP","Luigi","Di Palma","M","18/04/1997",
            "dypalma@gmail.com","@Dip123456","utente"));
    UtenteList lista = new UtenteList(act,listaUtenti);
  }

  @Test
  public void getView() {

  }
}