package com.example.dani.unishare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
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

        String eMail= email.getText().toString();
        String password1=password.getText().toString();

        databaseLogin.signInWithEmailAndPassword(eMail, password1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent (getApplicationContext(),MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "L'E-mail o la password sono errate.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        accedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        if(databaseLogin.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}
