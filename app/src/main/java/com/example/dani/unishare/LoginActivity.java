package com.example.dani.unishare;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends Activity {

    EditText email;
    EditText password;
    Button accedi;
    FirebaseAuth databaseLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email= (EditText) findViewById(R.id.InserisciEmail);
        password= (EditText) findViewById(R.id.InserisciPassword);
        accedi= (Button) findViewById(R.id.Accedi);

        databaseLogin=


    }
}
