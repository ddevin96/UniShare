package com.example.dani.unishare;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class LoginActivityTest {
  private ArrayList<Utente> lista;
  LoginActivity act;
  private String emailCorta,emailLunga,emailErrata,passwordErrata,passwordCorretta,emailCorretta,
          passwordCorta,passwordLunga;
  @Before
  public void setUp(){
    act = new LoginActivity();
    lista = new ArrayList<>();
    lista.add(new Utente("DDV","Daniele","De Vinco","M","22/03/1996","danieledivi@live.com","abc!123!DE","utente"));
    lista.add(new Utente("LDP","Luigi","Di Palma","M","18/04/1997","dypalma@gmail.com","@Dip123456","utente"));
    lista.add(new Utente("DCF","Donatella","Cioffi","F","27/06/1997","donaciof@live.com","passW@456789","utente"));
    //E-mail
    emailCorretta = lista.get(0).getEmail();
    emailCorta = "a@b";
    emailLunga = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@mail.com";
    emailErrata = "abc12.";

    //Password
    passwordCorretta = lista.get(0).getPassword();
    passwordCorta = "Ab12!";
    passwordLunga = "Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!Ab1!ababababababababababa";
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

  @Test
  public void confrontaMailTest(){
    act.listaUtente = lista;
    //Caso 1: e-mail esistente nel database
    assertTrue(act.confrontaMail(lista.get(0).getEmail()));
    //Caso 2 : e-mail assente dal database
    assertFalse(act.confrontaMail(""));
  }

  @Test
  public void controlloMailTest(){
    //Caso 1: e-mail vuota
    assertFalse(act.controlloMail(""));
    //Caso 2: e-mail < 3 caratteri
    assertFalse(act.controlloMail(emailCorta));
    //Caso 3: e-mail > 63 caratteri
    assertFalse(act.controlloMail(emailLunga));
    //Caso 4: e-mail in formato scorretto
    assertFalse(act.controlloMail(emailErrata));
    //Caso 6 : e-mail corretta
    assertTrue(act.controlloMail(lista.get(0).getEmail()));
  }

  @Test
  public void controlloPasswordTest(){
    //Caso 1: password vuota
    assertFalse(act.controlloPassword(""));
    //Caso 2: password < 8 caratteri
    assertFalse(act.controlloPassword(passwordCorta));
    //Caso 3: password > 23 caratteri
    assertFalse((act.controlloPassword(passwordLunga)));
    //Caso 4: formato scorretto
    assertFalse(act.controlloPassword((passwordErrata)));
    //Caso 5: password corretta
    assertTrue(act.controlloPassword(lista.get(0).getPassword()));
  }
}