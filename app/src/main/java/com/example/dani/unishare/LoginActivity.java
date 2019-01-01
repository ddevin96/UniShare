package com.example.dani.unishare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



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

        databaseLogin = FirebaseAuth.getInstance();

        accedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eMail= email.getText().toString();
                String password1=password.getText().toString();


                databaseLogin.signInWithEmailAndPassword(eMail, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish();
                            Toast.makeText(getApplicationContext(), "Login effettuato con successo.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "L'E-mail o la password sono errate.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        if(databaseLogin.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}
