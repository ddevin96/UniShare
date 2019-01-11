package com.example.dani.unishare;

import android.widget.DatePicker;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ProfiloActivityTest {

  @Before
  public void setUp(){
    //
  }
  @Test
  public void isValidPassword() {
    assertTrue(LoginActivity.isValidPassword("abcDEF!!123"));
    assertFalse(LoginActivity.isValidPassword(("")));
    assertFalse(LoginActivity.isValidPassword("1234"));
    assertFalse(LoginActivity.isValidPassword("abcdefghilmn"));
  }

  @Test
  public void isValidEmail() {
    assertTrue(LoginActivity.isValidEmail("manager@mail.com"));
    assertFalse(LoginActivity.isValidEmail(("")));
    assertFalse(LoginActivity.isValidEmail("abcdef"));
  }



}