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
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class LoginActivityInstrumentedTest {
  @Rule
  public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

  private String mailCorretta, mailAssente, passCorretta;
  @Before
  public void setUp() throws Exception {
    mailCorretta = "a@a.com";
    mailAssente = "ciao@mail.com";
    passCorretta = "Asd123!@!";
  }

  @Test
  public void loginTest(){
    //Caso 1: lunghezza email non valida, si considera il caso specifico email vuota.
    onView(withId(R.id.InserisciEmail)).perform(typeText(""),closeSoftKeyboard());
    onView(withId(R.id.InserisciPassword)).perform(typeText(passCorretta),closeSoftKeyboard());
    onView(withId(R.id.Accedi)).perform(click());
    String errorExpected = "L'email non può essere vuota.\nDeve rispettare il formato.\n può avere al massimo 63 caratteri.";
    assertEquals(errorExpected,mActivityRule.getActivity().email.getError());

    //Caso 2 : email inesistente nel db
    onView(withId(R.id.InserisciEmail)).perform(typeText(mailAssente),closeSoftKeyboard());
    onView(withId(R.id.Accedi)).perform(click());
    errorExpected = "L' e-mail è errata.";
    assertEquals(errorExpected,mActivityRule.getActivity().email.getError());

    //Caso 3: lunghezza password non valida, si considera il caso di password vuota
    onView(withId(R.id.InserisciEmail)).perform(clearText());
    onView(withId(R.id.InserisciEmail)).perform(typeText(mailCorretta),closeSoftKeyboard());
    onView(withId(R.id.InserisciPassword)).perform(clearText());
    onView(withId(R.id.Accedi)).perform(click());
    errorExpected = "La password non può essere vuota\nMin 8 caratteri\nMax 20 caratteri";
    assertEquals(errorExpected,mActivityRule.getActivity().password.getError());

  }
}