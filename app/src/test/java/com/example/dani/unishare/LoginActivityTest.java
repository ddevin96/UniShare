package com.example.dani.unishare;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class LoginActivityTest {
  private ArrayList<Utente> lista;
  @Before
  public void setUp(){
    lista = new ArrayList<>();
    lista.add(new Utente("DDV","Daniele","De Vinco","M","22/03/1996","danieledivi@live.com","abc.123.DE","utente"));
    lista.add(new Utente("LDP","Luigi","Di Palma","M","18/04/1997","dypalma@gmail.com","@Dip123456","utente"));
    lista.add(new Utente("DCF","Donatella","Cioffi","F","27/06/1997","donaciof@live.com","passW@456789","utente"));
    lista.add(new Utente("FUN","Federica","Ungherese","F","04/11/1997","fedeu@gmail.com",".051abcDE17.","utente"));
  }

  @Test
  public void isValidEmailTest() {
    assertTrue(LoginActivity.isValidEmail(lista.get(1).getEmail()));
    assertFalse(LoginActivity.isValidEmail(("")));
    assertFalse(LoginActivity.isValidEmail("abcdef"));
  }

  @Test
  public void isValidPasswordTest(){
    assertTrue(LoginActivity.isValidPassword(lista.get(2).getPassword()));
    assertFalse(LoginActivity.isValidPassword(("")));
    assertFalse(LoginActivity.isValidPassword("1234"));
    assertFalse(LoginActivity.isValidPassword("abcdefghilmn"));
  }

  @Test
  public void confrontaMailTest(){
    LoginActivity act = new LoginActivity();
    act.listaUtente = lista;
    assertTrue(act.confrontaMail(lista.get(0).getEmail()));
    assertFalse(act.confrontaMail(""));
  }

}