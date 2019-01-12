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
  TextView textViewNomeUtenteCercato;
  TextView textViewCognomeUtenteCercato;
  TextView textViewEmailUtenteCercato;
  TextView textViewSessoUtenteCercato;
  TextView textViewDataUtenteCercato;

  TextView labelNomeCercato;
  TextView labelCognomeCercato;
  TextView labelEmailCercato;
  TextView labelSessoCercato;
  TextView labelDataCercato;

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

    labelNomeCercato = (TextView) findViewById(R.id.labelNomeCercato);
    labelCognomeCercato = (TextView) findViewById(R.id.labelCognomeCercato);
    labelEmailCercato = (TextView) findViewById(R.id.labelEmailCercato);
    labelSessoCercato = (TextView) findViewById(R.id.labelSessoCercato);
    labelDataCercato = (TextView) findViewById(R.id.labelDataCercato);

    textViewNomeUtenteCercato.setVisibility(View.GONE);
    textViewCognomeUtenteCercato.setVisibility(View.GONE);
    textViewEmailUtenteCercato.setVisibility(View.GONE);
    textViewSessoUtenteCercato.setVisibility(View.GONE);
    textViewDataUtenteCercato.setVisibility(View.GONE);

    labelNomeCercato.setVisibility(View.GONE);
    labelCognomeCercato.setVisibility(View.GONE);
    labelEmailCercato.setVisibility(View.GONE);
    labelSessoCercato.setVisibility(View.GONE);
    labelDataCercato.setVisibility(View.GONE);


    listaUtenti = new ArrayList<>();

    mUser = databaseId.getInstance().getCurrentUser();

    databaseUtente = FirebaseDatabase.getInstance().getReference("utente");

    listViewUtenti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listViewUtenti.setVisibility(View.GONE);
        textViewNomeUtenteCercato.setVisibility(View.VISIBLE);
        textViewCognomeUtenteCercato.setVisibility(View.VISIBLE);
        textViewEmailUtenteCercato.setVisibility(View.VISIBLE);
        textViewSessoUtenteCercato.setVisibility(View.VISIBLE);
        textViewDataUtenteCercato.setVisibility(View.VISIBLE);

        labelNomeCercato.setVisibility(View.VISIBLE);
        labelCognomeCercato.setVisibility(View.VISIBLE);
        labelEmailCercato.setVisibility(View.VISIBLE);
        labelSessoCercato.setVisibility(View.VISIBLE);
        labelDataCercato.setVisibility(View.VISIBLE);

        Utente utente = listaUtenti.get(position);
        textViewNomeUtenteCercato.setText(utente.getNome());
        textViewCognomeUtenteCercato.setText(utente.getCognome());
        textViewEmailUtenteCercato.setText(utente.getEmail());
        textViewSessoUtenteCercato.setText(utente.getSesso());
        textViewDataUtenteCercato.setText(utente.getDataDiNascita());
      }
    });

    searchUtenteButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        List<Utente> listaUtentiTrovati = new ArrayList<>();
        List<String> listaParole = trovaParole(searchbarUtente.getText().toString());

        for (int k = 0; k < listaUtenti.size(); k++) {
          Utente utente = listaUtenti.get(k);
          for (int j = 0; j < listaParole.size(); j++) {
            String elem = listaParole.get(j);
            if (utente.getNome().contains(elem) || utente.getCognome().contains(elem)) {
              listaUtentiTrovati.add(utente);
              break;
            }
          }
        }

        listaUtenti = listaUtentiTrovati;
        UtenteList adapter2 = new UtenteList(RicercaProfiloActivity.this, listaUtentiTrovati);
        listViewUtenti.setAdapter(adapter2);
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
        for (DataSnapshot personSnapshot : dataSnapshot.getChildren()) {
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

  protected List<String> trovaParole(String stringa) {
    String parola = "";
    List<String> listaParole = new ArrayList<>();
    for (int i = 0; i < stringa.length(); i++) {
      if (stringa.charAt(i) > 'a' && stringa.charAt(i) < 'z'
              || stringa.charAt(i) > 'A' && stringa.charAt(i) < 'Z'
              || stringa.charAt(i) > '0' && stringa.charAt(i) < '9'
              || stringa.charAt(i) != ' ') {
        parola += stringa.charAt(i);
      } else {
        listaParole.add(parola);
        parola = "";
      }
    }
    listaParole.add(parola);
    return listaParole;
  }
}
