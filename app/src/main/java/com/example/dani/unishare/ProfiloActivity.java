package com.example.dani.unishare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class ProfiloActivity extends Activity {

    TextView textViewNome;
    TextView textViewCognome;
    TextView textViewEmail;
    TextView textViewSesso;
    TextView textViewData;
    Button modificaProfila;
    Button cancellaProfilo;
    DatabaseReference databesaProfilo;
    FirebaseAuth databaseId;
    FirebaseUser user;
    String nomeEdit, cognomeEdit, emailEdit, sessoEdit, passwordEdit;
    Long year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        textViewNome= (TextView) findViewById(R.id.textViewNomeUser);
        textViewCognome= (TextView) findViewById(R.id.textViewCognomeUtente);
        textViewEmail= (TextView) findViewById(R.id.textViewEmailUtente);
        textViewSesso= (TextView) findViewById(R.id.textViewSessoUtente);
        textViewData= (TextView) findViewById(R.id.textViewDataUtente);
        modificaProfila= (Button) findViewById(R.id.modificaProfiloButton);
        cancellaProfilo= (Button) findViewById(R.id.cancellaProfiloButton);

        user = databaseId.getInstance().getCurrentUser();
        databesaProfilo= FirebaseDatabase.getInstance().getReference("utente").child(user.getUid());


        modificaProfila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificaProfiloDialog();

            }
        });

        cancellaProfilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProfilo();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databesaProfilo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nomeEdit = dataSnapshot.child("nome").getValue(String.class);
                cognomeEdit = dataSnapshot.child("cognome").getValue(String.class);
                emailEdit = dataSnapshot.child("email").getValue(String.class);
                sessoEdit = dataSnapshot.child("sesso").getValue(String.class);
                passwordEdit = dataSnapshot.child("password").getValue(String.class);
                year = dataSnapshot.child("dataDiNascita").child("year").getValue(Long.class);
                month = dataSnapshot.child("dataDiNascita").child("month").getValue(Long.class);
                day = dataSnapshot.child("dataDiNascita").child("day").getValue(Long.class);
                String data= day + "/" + month + "/" + year;
                textViewNome.setText(nomeEdit);
                textViewCognome.setText(cognomeEdit);
                textViewEmail.setText(emailEdit);
                textViewSesso.setText(sessoEdit);
                textViewData.setText(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void modificaProfiloDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView= inflater.inflate(R.layout.modifica_profilo_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextNome;
        final EditText editTextCognome;
        final EditText editTextEmail;
        final EditText editTextPassword;
        final DatePicker data;
        final Button conferma;
        final RadioButton radioButtonUomo;

        editTextNome= (EditText) dialogView.findViewById(R.id.editTextModificaNome);
        editTextCognome =(EditText) dialogView.findViewById(R.id.editTextModificaCognome);
        editTextEmail = (EditText) dialogView.findViewById(R.id.editTextModificaEmail);
        editTextPassword = (EditText) dialogView.findViewById(R.id.editTextModificaPassword);
        data = (DatePicker) dialogView.findViewById(R.id.modificaData);
        radioButtonUomo = (RadioButton) dialogView.findViewById(R.id.radioModificaUomo);
        conferma = (Button) dialogView.findViewById(R.id.ButtonModifica);
        editTextNome.setText(nomeEdit);
        editTextCognome.setText(cognomeEdit);
        editTextEmail.setText(emailEdit);
        editTextPassword.setText(passwordEdit);

        int yearUpdate = year.intValue();
        int monthUpdate = month.intValue();
        int dayUpdate = day.intValue();
        data.updateDate(yearUpdate,monthUpdate,dayUpdate);

        dialogBuilder.setTitle("Modifica profilo");
        final AlertDialog alertDialog= dialogBuilder.create();
        alertDialog.show();

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = editTextNome.getText().toString();
                String cognome = editTextCognome.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                int year = data.getYear();
                int month = data.getMonth();
                int day = data.getDayOfMonth();
                String sesso;
                //radiobutton
                if (radioButtonUomo.isSelected())
                    sesso = "M";
                else
                    sesso = "F";


                if(TextUtils.isEmpty(nome) && nome.length()>20){
                    editTextNome.setError("Il campo Nome non può essere vuoto./n Deve avere al massimo 20 caratteri");
                }
                else if(TextUtils.isEmpty(cognome)&& cognome.length()>20){
                    editTextCognome.setError("il campo Cognome non può essere vuoto./n Deve avere al massimo 20 caratteri.");
                }
                else if(TextUtils.isEmpty(password)&& password.length()<8 && password.length()>20){
                    editTextPassword.setError("il campo password non può essere vuoto./n Deve essere composto di almeno 8 caratteri e massimo 20./n");
                }
                else if (TextUtils.isEmpty(email)&& email.length()<3 && email.length()>63){
                    editTextEmail.setError("il campo E-mail non può essere vuoto./n min:3 max:63 caratteri.");
                }
                else {
                    Date date = new Date(year, month, day);
                    String id = user.getUid();
                    user.updateEmail(email);
                    user.updatePassword(password);
                    Utente utente = new Utente(id, nome, cognome, sesso, date, email, password);
                    addProfilo(utente);
                    alertDialog.dismiss();

                    finish();
                    startActivity(new Intent(getApplicationContext(), ProfiloActivity.class));
                }
            }
        });
    }

    public void addProfilo(Utente utente) {
        Toast.makeText(this, "La modifica non ha avuto successo", Toast.LENGTH_SHORT).show();

    }

    private void deleteProfilo() {
        FirebaseAuth.getInstance().signOut();
        user.delete();
        databesaProfilo.removeValue();
        Toast.makeText(this,"Il profilo è stato cancellato" , Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
