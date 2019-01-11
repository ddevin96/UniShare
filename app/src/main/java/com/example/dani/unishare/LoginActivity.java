package com.example.dani.unishare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends Activity {

  EditText email;
  EditText password;
  Button accedi;
  FirebaseAuth databaseLogin;
  DatabaseReference databaseUtente;
  List<Utente> listaUtente;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    email = (EditText) findViewById(R.id.InserisciEmail);
    password = (EditText) findViewById(R.id.InserisciPassword);
    accedi = (Button) findViewById(R.id.Accedi);

    listaUtente = new ArrayList<>();

    databaseLogin = FirebaseAuth.getInstance();
    databaseUtente = FirebaseDatabase.getInstance().getReference("utente");

    databaseUtente.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        listaUtente.clear();
        for (DataSnapshot utenteSnapshot : dataSnapshot.getChildren()) {
          Utente utente = utenteSnapshot.getValue(Utente.class);
          listaUtente.add(utente);
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });

    accedi.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String eMail = email.getText().toString();
        String password1 = password.getText().toString();

        if (TextUtils.isEmpty(eMail) || eMail.length() < 3
                || eMail.length() > 63 || !isValidEmail(eMail)) {
          email.setError("L'email non può essere vuota.");
          email.requestFocus();
          return;
        }

        if (!confrontaMail(eMail)) {
          email.setError("L' e-mail è errata.");
          email.requestFocus();
          return;
        }

        if (TextUtils.isEmpty(password1) || password1.length() < 8
                || password1.length() > 23 || !isValidPassword(password1)) {
          password.setError("La password non può essere vuota\nMin 8 caratteri\nMax 20 caratteri");
          password.requestFocus();
          return;
        }

        databaseLogin.signInWithEmailAndPassword(eMail,
                password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                      finish();
                      Toast.makeText(getApplicationContext(), "Login effettuato con successo.",
                              Toast.LENGTH_SHORT).show();
                      startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                      Toast.makeText(getApplicationContext(), "L'E-mail o la password sono errate.",
                              Toast.LENGTH_SHORT).show();
                    }
                  }
                });
      }
    });

    if (databaseLogin.getCurrentUser() != null) {
      startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
  }

  private static boolean isValidPassword(String password) {

    Pattern pattern;
    Matcher matcher;
    final String Password_Pattern =
            "^(?=.*[0-9])(?=.*[A-Z])(?=.[a-z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    pattern = Pattern.compile(Password_Pattern);
    matcher = pattern.matcher(password);

    return matcher.matches();

  }

  @VisibleForTesting
  protected static boolean isValidEmail(String email) {
    Pattern pattern;
    Matcher matcher;
    final String Email_Pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    pattern = Pattern.compile(Email_Pattern);
    matcher = pattern.matcher(email);

    return matcher.matches();
  }

  private boolean confrontaMail(String mail) {
    boolean value = true;
    for (Utente utente : listaUtente) {
      if (utente.getEmail().equals(mail)) {
        value = true;
        break;
      } else {
        value = false;
      }
    }

    return value;
  }

}
