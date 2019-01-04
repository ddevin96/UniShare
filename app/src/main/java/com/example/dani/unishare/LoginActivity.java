package com.example.dani.unishare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

                if (TextUtils.isEmpty(eMail)||eMail.length()<3||eMail.length()>63||!isValidEmail(eMail)) {
                    email.setError("L'email non può essere vuota\nDeve rispettare il formato");
                    email.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password1)||password1.length()<8||password1.length()>23||!isValidPassword(password1)) {
                    password.setError("La password non può essere vuota\nMin 8 caratteri\nMax 20 caratteri");
                    password.requestFocus();
                    return;
                }


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
}
