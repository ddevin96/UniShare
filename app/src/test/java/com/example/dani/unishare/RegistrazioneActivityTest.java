package com.example.dani.unishare;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import static org.junit.Assert.*;

public class RegistrazioneActivityTest {
    private String emailCorretta, emailCorta, emailLunga, passwordCorretta, passwordLunga,
            passwordCorta,passwordErrata, emailErrata, nome, cognome;
    ArrayList<Utente> lista;
    RegistrazioneActivity act;


  @Before
  public void setUp() throws Exception {
    act = new RegistrazioneActivity();
    //Lista utenti
    lista = new ArrayList<>();
    lista.add(new Utente("MVD","Maria","Verdi","D",
            "15/02/1995","maria@libero.it","mariAA000!!","utente"));
    lista.add(new Utente("LDP","Luigi","Di Palma","M","18/04/1997",
            "dypalma@gmail.com","@Dip123456","utente"));
    lista.add(new Utente("DCF","Donatella","Cioffi","F",
            "27/06/1997","donaciof@live.com","passW@456789","utente"));


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
  public void controlloParametroTest(){
    //Caso 1: parametro vuoto
    assertTrue(act.controlloParametro(""));
    //Caso 2: parametro con più di 20 caratteri
    assertTrue(act.controlloParametro(passwordLunga));
    //Caso 3: parametro corretto
    assertFalse(act.controlloParametro("ciao"));
  }


  @Test
  public void controlloMailTest(){
   //Caso 1: e-mail vuota
    assertTrue(act.controlloMail(""));
    //Caso 2: e-mail < 3 caratteri
    assertTrue(act.controlloMail(emailCorta));
    //Caso 3: e-mail > 63 caratteri
    assertTrue(act.controlloMail(emailLunga));
    //Caso 4: e-mail in formato scorretto
    assertTrue(act.controlloMail(emailErrata));
    //Caso 6 : e-mail corretta
    assertFalse(act.controlloMail(lista.get(0).getEmail()));
  }

  @Test
  public void controlloPasswordTest(){

    //Caso 1: password vuota
    assertTrue(act.controlloPassword(""));
    //Caso 2: password < 8 caratteri
    assertTrue(act.controlloPassword(passwordCorta));
    //Caso 3: formato scorretto
    assertTrue(act.controlloPassword((passwordErrata)));
    //Caso 4: password corretta
    assertFalse(act.controlloPassword(lista.get(0).getPassword()));
  }

  @Test
  public void controlloConfermaPasswordTest(){
    String confermaPassword = "mariAA000!!";
    assertFalse(act.controlloConfermaPassword(lista.get(0).getPassword(),confermaPassword));
    assertTrue(act.controlloConfermaPassword(lista.get(0).getPassword(),"abc"));
  }

  @Test
  public void confrontaMailTest(){
    act.listaUtente = lista;
    assertTrue(act.confrontaMail(lista.get(1).getEmail()));
    assertFalse(act.confrontaMail("ciao@mail.com"));
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

  @Mock
  DatabaseReference databaseUser;

  @Mock
  FirebaseAuth mFirebaseAuth;

/*
  @Test
  public void createUserTest(){
    act.email = emailCorretta;
    act.password = passwordCorretta;
    act.nome = "Maria";
    act.cognome = "Verdi";
    act.sesso="D";
    act.date = "12/12/1996";
    act.databaseUtente = act.istanceReference("utente");
    act.firebaseAuth = FirebaseAuth.getInstance();
    act.createUser();
    assertTrue(act.user.getDisplayName().equals("Maria"));


  }



/*
  @Test
  public void registraUtente() {
    RegistrazioneActivity act = new RegistrazioneActivity();
    act.editTextRegNome.setText(nome);
    act.editTextRegCognome.setText(cognome);
    act.editTextRegPassword.setText(passwordCorretta);
    act.editTextRegRipetiPassword.setText(passwordCorretta);
    act.radioDonna.setSelected(true);
    act.checkboxPrivacy.setSelected(true);
    act.editTextRegEmail.setText(emailCorretta);
    act.editDatePicker.init(1994,10,10,null);
    act.istance();

    act.registraUtente();
  }
*/
}