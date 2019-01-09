package com.example.dani.unishare;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrazioneActivity extends Activity {

    EditText editTextRegNome, editTextRegCognome, editTextRegEmail, editTextRegPassword;
    EditText editTextRegRipetiPassword;
    DatePicker editDatePicker;
    CheckBox checkboxPrivacy;
    RadioButton radioUomo, radioDonna;
    RadioGroup radioGroupSesso;
    Button buttonRegistrazione;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseUtente;
    List<Utente> listaUtente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        editTextRegNome = (EditText) findViewById(R.id.editTextRegNome);
        editTextRegCognome = (EditText) findViewById(R.id.editTextRegCognome);
        editTextRegEmail = (EditText) findViewById(R.id.editTextRegEmail);
        editTextRegPassword = (EditText) findViewById(R.id.editTextRegPassword);
        editTextRegRipetiPassword = (EditText) findViewById(R.id.editTextRegRipetiPassword);
        editDatePicker = (DatePicker) findViewById(R.id.editDatePicker);
        checkboxPrivacy = (CheckBox) findViewById(R.id.checkboxPrivacy);
        radioGroupSesso = (RadioGroup) findViewById(R.id.radioGroupSesso);
        radioDonna = (RadioButton) findViewById(R.id.radioDonna);
        radioUomo = (RadioButton) findViewById(R.id.radioUomo);
        buttonRegistrazione = (Button) findViewById(R.id.buttonRegistrazione);

        listaUtente = new ArrayList<>();
        databaseUtente = FirebaseDatabase.getInstance().getReference("utente");
        databaseUtente.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaUtente.clear();
                for(DataSnapshot utenteSnapshot: dataSnapshot.getChildren()) {
                    Utente utente = utenteSnapshot.getValue(Utente.class);
                    listaUtente.add(utente);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        buttonRegistrazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registraUtente();
            }
        });
    }



    private void registraUtente() {
        final String nome = editTextRegNome.getText().toString().trim();
        final String cognome = editTextRegCognome.getText().toString().trim();
        final String email = editTextRegEmail.getText().toString().trim();
        final String password = editTextRegPassword.getText().toString().trim();
        final String ripPassword = editTextRegRipetiPassword.getText().toString().trim();
        int year = editDatePicker.getYear();
        int month = editDatePicker.getMonth();
        int day = editDatePicker.getDayOfMonth();

        final Date date = new Date(year, month, day);
        final String sesso;
        if(radioDonna.isSelected())
            sesso = "D";
        else
            sesso = "M";

        if(nome.isEmpty()||nome.length()>20) {
            editTextRegNome.setError("Il nome non può essere vuoto\nMax20Caratteri");
            editTextRegNome.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(cognome)||cognome.length()>20) {
            editTextRegCognome.setError("Il cognome non può essere vuoto\nMax 20 caratteri");
            editTextRegCognome.requestFocus();
            return;
        }

        if(confrontaMail(email)) {
            editTextRegEmail.setError("L'email è già presente nel sistema");
            editTextRegEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)||email.length()<3||email.length()>63||!isValidEmail(email)) {
            editTextRegEmail.setError("L'email non può essere vuota\nMax 63 caratteri");
            editTextRegEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)||password.length()<8||password.length()>23||!isValidPassword(password)) {
            editTextRegPassword.setError("La password non può essere vuota\nMin 8 caratteri\nMax 20 caratteri");
            editTextRegPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(ripPassword)||!ripPassword.equals(password)) {
            editTextRegRipetiPassword.setError("Le password non coincidono");
            editTextRegRipetiPassword.requestFocus();
            return;
        }




            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Utente Aggiunto", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();
                        user.updateProfile(profileUpdate);
                        String ruolo = "utente";
                        Utente utente = new Utente(user.getUid(), nome, cognome, sesso, date, email, password, ruolo);
                        databaseUtente.child(firebaseAuth.getCurrentUser().getUid()).setValue(utente);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Problema con registrazione", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private static boolean isValidPassword(String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.[a-z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    private static boolean isValidEmail(String email){
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private boolean confrontaMail(String mail) {
        boolean value= true;
        for (Utente utente : listaUtente) {
            if (utente.getEmail().equals(mail)) {
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

