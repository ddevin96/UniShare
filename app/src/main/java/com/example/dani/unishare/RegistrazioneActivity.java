package com.example.dani.unishare;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class RegistrazioneActivity extends Activity {

    EditText editTextRegNome, editTextRegCognome, editTextRegEmail, editTextRegPassword;
    EditText editTextRegRipetiPassword, editTextRegData;
    CheckBox checkboxPrivacy;
    RadioButton radioUomo, radioDonna;
    RadioGroup radioGroupSesso;
    Button buttonRegistrazione;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);
    }
}
