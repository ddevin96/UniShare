package com.example.dani.unishare;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class RicercaProfiloActivityInstrumentalTest {
  @Rule
  public ActivityTestRule<RicercaProfiloActivity> mActivityRule = new ActivityTestRule<>(RicercaProfiloActivity.class);

  @Test
  public void searchUserTest(){
    onView(withId(R.id.searchbarUtente)).perform(typeText("Donatella"),closeSoftKeyboard());
    onView(withId(R.id.searchUtenteButton)).perform(click());
    assertTrue(mActivityRule.getActivity().listaUtenti.size()!=0);


  }

}