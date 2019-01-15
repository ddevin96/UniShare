package com.example.dani.unishare;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ProfiloActivityTest {
  private ProfiloActivity act;
  ArrayList<Utente> lista;
  private String emailCorretta, emailCorta, emailLunga, passwordCorretta, passwordLunga,
          passwordCorta,passwordErrata, emailErrata;
  @Before
  public void setUp(){
    act = new ProfiloActivity();
    //Lista utenti
    lista = new ArrayList<>();
    lista.add(new Utente("MVD","Maria","Verdi","D",
            "15/02/1995","maria@libero.it","mariAA000!!","utente"));
    lista.add(new Utente("LDP","Luigi","Di Palma","M","18/04/1997",
            "dypalma@gmail.com","@Dip123456","utente"));
    //E-mail
    emailCorretta = "ciao@mail.com";
    emailErrata = "abc12.";
    emailCorta = "ab";
    emailLunga = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@mail.com";
    //Password
    passwordCorretta = "Ciao!123A!";
    passwordErrata = "ciao123.";
    passwordCorta = "Ab12!";
    passwordLunga = "Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!ababababababababababa";
  }

  @Test
  public void isValidPassword() {
    //Caso 1 : password nel formato scorretto
    assertFalse(act.isValidPassword(passwordErrata));
    //Caso 2 : password corretta
    assertTrue(act.isValidPassword(passwordCorretta));
  }

  @Test
  public void isValidEmail() {
    //Caso 1: e-mail in formato scorretto
    assertFalse(act.isValidEmail(emailErrata));
    //Caso 2 : e-mail corretta
    assertTrue(act.isValidEmail(emailCorretta));
  }

  @Test
  public void controllaPasswordTest(){
    //Caso 1 : password vuota
    assertTrue(act.controllaPassword(""));
    //Caso 2 : password < 8
    assertTrue(act.controllaPassword(passwordCorta));
    //Caso 3 : password > 20
    assertTrue(act.controllaPassword(passwordLunga));
    //Caso 4 : formato scorretto
    assertTrue(act.controllaPassword(passwordErrata));
    //Caso 5 : password accettata
    assertFalse(act.controllaPassword(passwordCorretta));
  }

  @Test
  public void controllaMailTest(){
    //caso 1 : mail vuota
    assertTrue(act.controllaMail(""));
    //caso 2 : mail < 3
    assertTrue(act.controllaMail(emailCorta));
    //caso 3 : mail > 63
    assertTrue(act.controllaMail(emailLunga));
    //caso 4 : mail formato scorretto
    assertTrue(act.controllaMail(emailErrata));
    //caso 5 : mail formato corretto
    assertFalse(act.controllaMail(emailCorretta));
  }

  @Test
  public void controllaParametri(){
    //Caso 1: parametro vuoto
    assertTrue(act.controllaParametri(""));
    //Caso 2: parametro con più di 20 caratteri
    assertTrue(act.controllaParametri(passwordLunga));
    //Caso 3: parametro corretto
    assertFalse(act.controllaParametri("ciao"));
  }

  @Test
  public void confrontaMailTest(){
    act.listaUtente = lista;
    //Caso 1 : e-mail presente nel database
    assertTrue(act.confrontaMail(lista.get(1).getEmail(),"ABC"));
    //Caso 2 : e-mail assente dal database
    assertFalse(act.confrontaMail("ciao@mail.com","CIAO"));
  }
}