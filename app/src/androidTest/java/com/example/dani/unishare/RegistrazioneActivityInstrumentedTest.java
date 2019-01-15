package com.example.dani.unishare;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class RegistrazioneActivityInstrumentedTest {
  @Rule
  public ActivityTestRule<RegistrazioneActivity> mActivityRule = new ActivityTestRule<>(RegistrazioneActivity.class);
  private String emailCorretta, passwordCorretta,nomeLungo,nomeCorretto,cognomeLungo,cognomeCorretto;

  @Before
  public void setUp() throws Exception {
    emailCorretta = "mario@gmail.com";
    passwordCorretta = "Abc!123!45";
    cognomeCorretto = "Razzii";
    cognomeLungo="Raaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaazzi";
    nomeCorretto = "Mario";
    nomeLungo = "Maaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaario";
  }

  @Test
  public void registrazioneTest(){
    /*
    I seguenti casi sono stati testati in RegistrazioneActivityTest.java
    Caso 1 : e-mail < 3 caratteri
    Caso 2 : e-mail > 63 caratteri
    Caso 3 : e-mail in formato scorretto
    Caso 4 : password < 8 caratteri
    Caso 5 : password > 20 caratteri
    */
    onView(withId(R.id.editTextRegEmail)).perform(typeText(emailCorretta),closeSoftKeyboard());
    onView(withId(R.id.editTextRegPassword)).perform(typeText(passwordCorretta),closeSoftKeyboard());
    //Caso 6 : nome vuoto
    onView(withId(R.id.editTextRegNome)).perform(typeText(""),closeSoftKeyboard());

    onView(withId(R.id.buttonRegistrazione)).perform(scrollTo()).perform(click());
    String errorExpected = "Il nome non può essere vuoto\nMax20Caratteri";
    assertEquals(errorExpected,mActivityRule.getActivity().editTextRegNome.getError());

    //Caso 7 : nome > 20 caratteri
    onView(withId(R.id.editTextRegNome)).perform(typeText(nomeLungo),closeSoftKeyboard());
    onView(withId(R.id.buttonRegistrazione)).perform(scrollTo()).perform(click());
    assertEquals(errorExpected,mActivityRule.getActivity().editTextRegNome.getError());

    onView(withId(R.id.editTextRegNome)).perform(clearText());
    onView(withId(R.id.editTextRegNome)).perform(typeText(nomeCorretto),closeSoftKeyboard());

    //Caso 8 : cognome vuoto
    onView(withId(R.id.editTextRegCognome)).perform(typeText(""),closeSoftKeyboard());
    onView(withId(R.id.buttonRegistrazione)).perform(scrollTo()).perform(click());
    errorExpected = "Il cognome non può essere vuoto\nMax 20 caratteri";
    assertEquals(errorExpected,mActivityRule.getActivity().editTextRegCognome.getError());

    //Caso 9 cognome > 20 caratteri
    onView(withId(R.id.editTextRegCognome)).perform(typeText(cognomeLungo),closeSoftKeyboard());
    onView(withId(R.id.buttonRegistrazione)).perform(scrollTo()).perform(click());
    assertEquals(errorExpected,mActivityRule.getActivity().editTextRegCognome.getError());

    onView(withId(R.id.editTextRegCognome)).perform(clearText());
    onView(withId(R.id.editTextRegCognome)).perform(typeText(cognomeCorretto),closeSoftKeyboard());

    //Caso 10 : conferma password diverso da password
    onView(withId(R.id.editTextRegRipetiPassword)).perform(typeText("abc"),closeSoftKeyboard());
    onView(withId(R.id.buttonRegistrazione)).perform(scrollTo()).perform(click());
    errorExpected = "Le password non coincidono";
    assertEquals(errorExpected,mActivityRule.getActivity().editTextRegRipetiPassword.getError());

    //Caso 11 : checkbox della privacy non accettata
    onView(withId(R.id.checkboxPrivacy)).perform(click());
    onView(withId(R.id.buttonRegistrazione)).perform(scrollTo()).perform(click());
    errorExpected = "Le condizioni sulla privacy devono essere accettate";
    assertEquals(errorExpected,mActivityRule.getActivity().checkboxPrivacy.getError());

    //Caso 12 : registrazione corretta
    onView(withId(R.id.checkboxPrivacy)).perform(click());
    onView(withId(R.id.buttonRegistrazione)).perform(scrollTo()).perform(click());
  }
}