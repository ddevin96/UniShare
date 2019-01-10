package com.example.dani.unishare;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class LoginActivityTest {

    //Inizializzazione della lista di utenti
    private ArrayList<Utente> lista;
    @Before
    public void setUp() throws Exception {
       lista = new ArrayList<Utente>();
       lista.add(new Utente("DDV","Daniele","De Vinco","U","22/03/1996","danieledivi@live.com","abc.123.DE","utente"));
       lista.add(new Utente("LDP","Luigi","Di Palma","U","18/04/1997","dypalma@gmail.com","@DIP123456","utente"));
       lista.add(new Utente("DCF","Donatella","Cioffi","D","27/06/1997","donaciof@live.com","passW@456789","utente"));
       lista.add(new Utente("FUN","Federica","Ungherese","D","04/11/1997","fedeu@gmail.com",".0512104717.","utente"));
    }

    //Testing sui metodi di LoginActivity
    @Test
    public void confrontaMailTest(){
        String emailPresente = lista.get(0).getEmail();
        String emailAssente ="mariorossi@gmail.com";
        assertTrue(confrontaMail(emailPresente));
        assertFalse(confrontaMail(emailAssente));
    }

    @Test
    public void isValidEmailTest(){
        String emailCorretta = lista.get(1).getEmail();
        String emailErrata = "abc";
        assertTrue(isValidEmail(emailCorretta));
        assertFalse(isValidEmail(emailErrata));
    }

    @Test
    public void isValidPasswordTest(){
        String passwordCorretta = lista.get(2).getPassword();
        String passwordErrata = "123abc";
        assertTrue(isValidPassword(passwordCorretta));
        assertFalse(isValidPassword(passwordErrata));
    }


    //METODI DELLA CLASSE DA TESTARE
    private boolean isValidPassword(String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.[a-z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean isValidEmail(String email){
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean confrontaMail(String email){
        boolean value= true;
        for (Utente utente : lista) {
            if (utente.getEmail().equals(email)) {
                value=true;
                break;
            }
            else {
                value=false;
            }
        }
        return value;
    }
}