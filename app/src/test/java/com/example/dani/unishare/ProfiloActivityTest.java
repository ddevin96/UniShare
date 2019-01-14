package com.example.dani.unishare;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProfiloActivityTest {
  private ProfiloActivity act;
  private String emailCorretta, emailCorta, emailLunga, passwordCorretta, passwordLunga,
          passwordCorta,passwordErrata, emailErrata;
  @Before
  public void setUp(){
    act = new ProfiloActivity();
    //E-mail
    emailCorretta = "ciao@mail.com";
    emailErrata = "abc12.";
    //Password
    passwordCorretta = "Ciao!123A!";
    passwordErrata = "ciao123.";
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

}