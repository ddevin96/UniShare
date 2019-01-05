package com.example.dani.unishare;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManagerActivity extends Activity {

    Button creaManager;
    DatabaseReference databaseUtente;
    ListView listViewUtenti;
    List<Utente> listaUtenti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        creaManager = (Button) findViewById(R.id.creaManagerButton);
        listViewUtenti = (ListView) findViewById(R.id.listViewUtenti);
        listaUtenti = new ArrayList<>();
        listViewUtenti.setVisibility(View.GONE);
        databaseUtente = FirebaseDatabase.getInstance().getReference("utente");

        creaManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewUtenti.setVisibility(View.VISIBLE);
            }
        });

        listViewUtenti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utente utente = listaUtenti.get(position);
                utente.setRuolo("manager");
                Toast.makeText(getApplicationContext(),"Manager aggiunto" ,Toast.LENGTH_SHORT ).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseUtente.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaUtenti.clear();
                for (DataSnapshot personSnapshot: dataSnapshot.getChildren()) {
                    Utente utente = personSnapshot.getValue(Utente.class);
                    listaUtenti.add(utente);
                }

                UtenteList adapter = new UtenteList(ManagerActivity.this, listaUtenti);
                listViewUtenti.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
