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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends Activity implements FirebaseInterface{

  EditText email;
  EditText password;
  Button accedi;
  FirebaseUser user;
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

    istance();
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

        if (!controlloMail(eMail)) {
          email.setError("L'email non può essere vuota.\nDeve rispettare il formato.\n può avere al massimo 63 caratteri.");
          email.requestFocus();
          return;
        }

        if (!confrontaMail(eMail)) {
          email.setError("L' e-mail è errata.");
          email.requestFocus();
          return;
        }

        if (!controlloPassword(password1)) {
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
    getUser();
    if (user!= null) {
      startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
  }

  protected static boolean isValidPassword(String password) {

    Pattern pattern;
    Matcher matcher;
    final String Password_Pattern =
            "^(?=.*[0-9])(?=.*[A-Z])(?=.[a-z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    pattern = Pattern.compile(Password_Pattern);
    matcher = pattern.matcher(password);

    return matcher.matches();

  }

  protected static boolean isValidEmail(String email) {
    Pattern pattern;
    Matcher matcher;
    final String Email_Pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    pattern = Pattern.compile(Email_Pattern);
    matcher = pattern.matcher(email);

    return matcher.matches();
  }

  protected boolean confrontaMail(String mail) {
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

  protected boolean controlloMail(String mail){
    if (mail.isEmpty() || mail.length() < 3
            || mail.length() > 63 || !isValidEmail(mail)){
      return false;
    }
    else {
      return true;
    }
  }

  protected boolean controlloPassword(String password){
    if (password.isEmpty() || password.length() < 8
            || password.length() > 23 || !isValidPassword(password)){
      return false;
    }
    else {
      return true;
    }
  }

  public void istance(){
    databaseLogin = FirebaseAuth.getInstance();
  }

  public void getUser(){
    user = databaseLogin.getCurrentUser();
  }

  public String getUserId(){
    return user.getUid();
  }

  public String getUserName(){
    return user.getDisplayName();
  }

  public void logout(){
    FirebaseAuth.getInstance().signOut();
  }

  public DatabaseReference istanceReference(String reference){
    DatabaseReference temp = FirebaseDatabase.getInstance().getReference("reference");
    return temp;
  }

  public DatabaseReference getChild(String reference, String childId){
    DatabaseReference temp = FirebaseDatabase.getInstance().getReference("reference").child(childId);
    return temp;
  }

  public String getIdObject(DatabaseReference data){
    return data.push().getKey();
  }

  @Override
  public void addValue(DatabaseReference data, String idChild, Object object) {
    data.child(idChild).setValue((Commento)object);
  }

  @Override
  public void deleteVlaue(DatabaseReference data,String idChild) {
    data.child(idChild).removeValue();
  }
}
