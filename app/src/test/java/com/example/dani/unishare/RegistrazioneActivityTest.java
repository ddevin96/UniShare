package com.example.dani.unishare;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RegistrazioneActivityTest {
    private String nome, cognome, email, password;

  @Before
  public void setUp() throws Exception {
    nome = "Maria";
    cognome = "Verdi";
    email = "maria@libero.it";
    password = "mariAA000!!";
  }

  @Test
  public void registraUtente() {
    RegistrazioneActivity act = new RegistrazioneActivity();
    act.editTextRegNome.setText(nome);
    act.editTextRegCognome.setText(cognome);
    act.editTextRegPassword.setText(password);
    act.editTextRegRipetiPassword.setText(password);
    act.radioDonna.setSelected(true);
    act.editTextRegEmail.setText(email);
    act.editDatePicker.init(1994,10,10,null);
    act.registraUtente();
    //aspettare firebase



  }
}