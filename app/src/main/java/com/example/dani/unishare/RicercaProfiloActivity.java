package com.example.dani.unishare;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RicercaProfiloActivity extends Activity {

    Button searchUtenteButton;
    TextView searchbarUtente;
    DatabaseReference databaseUtente;
    ListView listViewUtenti;
    List<Utente> listaUtenti;
    FirebaseAuth databaseId;
    FirebaseUser mUser;
    TextView textViewNomeUtenteCercato, textViewCognomeUtenteCercato, textViewEmailUtenteCercato, textViewSessoUtenteCercato, textViewDataUtenteCercato;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricerca_profilo);

        searchUtenteButton = (Button) findViewById(R.id.searchUtenteButton);
        searchbarUtente = (TextView) findViewById(R.id.searchbarUtente);
        listViewUtenti = (ListView) findViewById(R.id.listViewUtenti);

        textViewNomeUtenteCercato = (TextView) findViewById(R.id.textViewNomeUtenteCercato);
        textViewCognomeUtenteCercato = (TextView) findViewById(R.id.textViewCognomeUtenteCercato);
        textViewEmailUtenteCercato = (TextView) findViewById(R.id.textViewEmailUtenteCercato);
        textViewSessoUtenteCercato = (TextView) findViewById(R.id.textViewSessoUtenteCercato);
        textViewDataUtenteCercato = (TextView) findViewById(R.id.textViewDataUtenteCercato);

        textViewNomeUtenteCercato.setVisibility(View.GONE);
        textViewCognomeUtenteCercato.setVisibility(View.GONE);
        textViewEmailUtenteCercato.setVisibility(View.GONE);
        textViewSessoUtenteCercato.setVisibility(View.GONE);
        textViewDataUtenteCercato.setVisibility(View.GONE);

        listaUtenti = new ArrayList<>();

        mUser = databaseId.getInstance().getCurrentUser();

        databaseUtente = FirebaseDatabase.getInstance().getReference("utente");

        listViewUtenti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utente utente = listaUtenti.get(position);
                listViewUtenti.setVisibility(View.GONE);

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

                UtenteList adapter = new UtenteList(RicercaProfiloActivity.this, listaUtenti);
                listViewUtenti.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
