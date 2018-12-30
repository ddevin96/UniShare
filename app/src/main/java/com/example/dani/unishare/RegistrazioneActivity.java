package com.example.dani.unishare;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

public class RegistrazioneActivity extends Activity {

    EditText editTextRegNome, editTextRegCognome, editTextRegEmail, editTextRegPassword;
    EditText editTextRegRipetiPassword, editTextRegData;
    CheckBox checkboxPrivacy;
    RadioButton radioUomo, radioDonna;
    RadioGroup radioGroupSesso;
    Button buttonRegistrazione;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        editTextRegNome = (EditText) findViewById(R.id.editTextRegNome);
        editTextRegCognome = (EditText) findViewById(R.id.editTextRegCognome);
        editTextRegEmail = (EditText) findViewById(R.id.editTextRegEmail);
        editTextRegPassword = (EditText) findViewById(R.id.editTextRegPassword);
        editTextRegRipetiPassword = (EditText) findViewById(R.id.editTextRegRipetiPassword);
        editTextRegData = (EditText) findViewById(R.id.editTextRegData);
        checkboxPrivacy = (CheckBox) findViewById(R.id.checkboxPrivacy);
        radioGroupSesso = (RadioGroup) findViewById(R.id.radioGroupSesso);
        radioDonna = (RadioButton) findViewById(R.id.radioDonna);
        radioUomo = (RadioButton) findViewById(R.id.radioUomo);
        buttonRegistrazione = (Button) findViewById(R.id.buttonRegistrazione);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonRegistrazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registraUtente();
            }
        });
    }


    private void registraUtente() {
        String nome = editTextRegNome.getText().toString().trim();
        String cognome = editTextRegCognome.getText().toString().trim();
        String email = editTextRegEmail.getText().toString().trim();
        String password = editTextRegPassword.getText().toString().trim();
        String ripPassword = editTextRegRipetiPassword.getText().toString().trim();
        String data = editTextRegData.getText().toString().trim();

    }
}
