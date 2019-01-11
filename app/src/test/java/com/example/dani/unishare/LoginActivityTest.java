package com.example.dani.unishare;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class LoginActivityTest {


  @Test
  public void isValidEmailTest() {
    assertTrue(LoginActivity.isValidEmail("manager@mail.com"));
    assertFalse(LoginActivity.isValidEmail(("")));
    assertFalse(LoginActivity.isValidEmail("abcdef"));
  }

  @Test
  public void isValidPasswordTest(){
    assertTrue(LoginActivity.isValidPassword("abcDEF!!123"));
    assertFalse(LoginActivity.isValidPassword(("")));
    assertFalse(LoginActivity.isValidPassword("1234"));
    assertFalse(LoginActivity.isValidPassword("abcdefghilmn"));
  }
/*
  @Test
  public void confrontaMailTest(){

  }
*/

}