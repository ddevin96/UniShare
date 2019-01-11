package com.example.dani.unishare;
import static org.junit.Assert.*;
import org.junit.Test;

public class LoginActivityTest {
  @Test
  public void isValidEmailTest() {
    assertTrue(LoginActivity.isValidEmail("a@a.com"));

  }
}