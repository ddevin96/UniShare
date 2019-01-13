package com.example.dani.unishare;
import android.widget.DatePicker;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class ProfiloActivityTest {
  private ProfiloActivity act;
  private DatePicker data;
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
/*
  @Test
  public void aggiornaDataTest(){
    ProfiloActivity act = Robolectric.setupActivity(ProfiloActivity.class);
    data.init(2018,1,12,null);
    int month = 2;
    act.aggiornaData(data);
    assertTrue(month==data.getMonth());
  }
*/
}