package com.example.dani.unishare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

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

        databaseUtente = FirebaseDatabase.getInstance().getReference("utente");

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

        boolean valida = true;

        if (TextUtils.isEmpty(nome)&&nome.length()>20) {
            editTextRegNome.setError("Il nome non può essere vuoto\nMax 20 caratteri");
            valida = false;
        } else if (TextUtils.isEmpty(cognome)&&cognome.length()>20) {
            editTextRegCognome.setError("Il cognome non può essere vuoto\nMax 20 caratteri");
            valida = false;
        } else if (TextUtils.isEmpty(email)&&email.length()>63) {
            editTextRegEmail.setError("L'email non può essere vuota\nMax 63 caratteri");
            valida = false;
        } else if (TextUtils.isEmpty(password)&&password.length()<8&&password.length()>23) {
            editTextRegPassword.setError("La password non può essere vuota\nMin 8 caratteri\nMax 20 caratteri");
            valida = false;
        } else if (TextUtils.isEmpty(ripPassword)&&ripPassword.equals(password)) {
            editTextRegRipetiPassword.setError("Le password non coincidono");
            valida = false;
        }

        if (valida){
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Utente Aggiunto", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();
                        user.updateProfile(profileUpdate);
                        Utente utente = new Utente(user.getUid(), nome, cognome, sesso, date, email, password);
                        databaseUtente.child(firebaseAuth.getCurrentUser().getUid()).setValue(utente);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Problema con registrazione", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Inserisci tutti i campi", Toast.LENGTH_SHORT).show();
        }
    }
}

