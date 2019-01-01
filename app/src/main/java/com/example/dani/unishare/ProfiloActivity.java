package com.example.dani.unishare;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        FirebaseUser user= databaseId.getInstance().getCurrentUser();
        databesaProfilo= FirebaseDatabase.getInstance().getReference("utente").child(user.getUid());
        textViewNome.setText(user.getUid());

        databesaProfilo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nome = dataSnapshot.child("nome").getValue(String.class);
                String cognome = dataSnapshot.child("cognome").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String sesso = dataSnapshot.child("sesso").getValue(String.class);
                Long year = dataSnapshot.child("dataDiNascita").child("year").getValue(Long.class);
                Long month = dataSnapshot.child("dataDiNascita").child("month").getValue(Long.class);
                Long day = dataSnapshot.child("dataDiNascita").child("day").getValue(Long.class);
                String data= day + "/" + month + "/" + year;
                textViewNome.setText(nome);
                textViewCognome.setText(cognome);
                textViewEmail.setText(email);
                textViewSesso.setText(sesso);
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

            editTextNome= (EditText) dialogView.findViewById(R.id.editTextModificaNome);
            editTextCognome =(EditText) dialogView.findViewById(R.id.editTextModificaCognome);
            editTextEmail = (EditText) dialogView.findViewById(R.id.editTextModificaEmail);
            editTextPassword = (EditText) dialogView.findViewById(R.id.editTextModificaPassword);
            data = (DatePicker) dialogView.findViewById(R.id.modificaData);
            conferma = (Button) dialogView.findViewById(R.id.ButtonModifica);

            dialogBuilder.setTitle("Modifica profilo");
            final AlertDialog alertDialog= dialogBuilder.create();
            alertDialog.show();
        }
    }
}
