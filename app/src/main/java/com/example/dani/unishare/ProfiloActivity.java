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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

        databesaProfilo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nomeEdit = dataSnapshot.child("nome").getValue(String.class);
                cognomeEdit = dataSnapshot.child("cognome").getValue(String.class);
                emailEdit = dataSnapshot.child("email").getValue(String.class);
                sessoEdit = dataSnapshot.child("sesso").getValue(String.class);
                passwordEdit = dataSnapshot.child("password").getValue(String.class);
                Long year = dataSnapshot.child("dataDiNascita").child("year").getValue(Long.class);
                Long month = dataSnapshot.child("dataDiNascita").child("month").getValue(Long.class);
                Long day = dataSnapshot.child("dataDiNascita").child("day").getValue(Long.class);
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
    private void modificaProfiloDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView= inflater.inflate(R.layout.modifica_profilo_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextNome;
        final EditText editTextCognome;
        final EditText editTextSesso;
        final EditText editTextEmail;
        final EditText editTextPassword;
        final DatePicker data;
        final Button conferma;

        editTextNome= (EditText) dialogView.findViewById(R.id.editTextModificaNome);
        editTextCognome =(EditText) dialogView.findViewById(R.id.editTextModificaCognome);
        editTextEmail = (EditText) dialogView.findViewById(R.id.editTextModificaEmail);
        editTextSesso=  (EditText) dialogView.findViewById(R.id.editTextModificaSesso);
        editTextPassword = (EditText) dialogView.findViewById(R.id.editTextModificaPassword);
        data = (DatePicker) dialogView.findViewById(R.id.modificaData);
        conferma = (Button) dialogView.findViewById(R.id.ButtonModifica);
        editTextNome.setText(nomeEdit);
        editTextCognome.setText(cognomeEdit);
        editTextEmail.setText(emailEdit);
        editTextSesso.setText(sessoEdit);
        editTextPassword.setText(passwordEdit);

        dialogBuilder.setTitle("Modifica profilo");
        final AlertDialog alertDialog= dialogBuilder.create();
        alertDialog.show();

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = editTextNome.getText().toString();
                String cognome = editTextCognome.getText().toString();
                String sesso = editTextSesso.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                int year = data.getYear();
                int month = data.getMonth();
                int day = data.getDayOfMonth();


                Date date = new Date(year,month,day);
                String id = user.getUid();
                user.updateEmail(email);
                user.updatePassword(password);
                Utente utente = new Utente(id, nome, cognome, sesso, date, email, password);
                addProfilo(utente);
                alertDialog.dismiss();
            }
        });
    }

    public void addProfilo(Utente utente) {

        if (!TextUtils.isEmpty(utente.getNome())&&!TextUtils.isEmpty(utente.getCognome())&&!TextUtils.isEmpty(utente.getSesso())&&!TextUtils.isEmpty(utente.getEmail())&&!TextUtils.isEmpty(utente.getPassword())&&!TextUtils.isEmpty(utente.getDataDiNascita().toString())) {
            databesaProfilo.setValue(utente);
            Toast.makeText(this, "Il profilo è stato modificato", Toast.LENGTH_SHORT).show();
        } else
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
