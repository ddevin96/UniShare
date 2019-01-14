package com.example.dani.unishare;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Activity usata per la gestione della ricerca del profilo di un altro utente.</p>
 */
public class RicercaProfiloActivity extends Activity implements FirebaseInterface {

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

    istance();
    getUser();

    databaseUtente = istanceReference("utente");

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

  /**
   * Metodo protected utilizzato per prelevare tutte le parole inserite
   * in una stringa di ricerca (frase).
   *
   * @param stringa Stringa di ricerca.
   * @return listaParole Lista di Stringhe che contiene le parole
   *         contenute nella frase digitata nella barra di ricerca.
   */
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

  /**
   * <p>Implementazione delle firme dei metodi dell'interfaccia</p>
   * @see FirebaseInterface
   */
  /**
   * <p>Metodi per FirebaseAuth.</p>
   */
  /**
   * <p>Metodo public utilizzato per creareun istanza di FirbaseAuth (autentication).</p>
   */
  public void istance() {
    databaseId = FirebaseAuth.getInstance();
  }

  /**
   * <p>Metdo public usato per creare un istanza dell'Utente che.
   * ha effettuato un accesso al database</p>
   */
  public void getUser() {
    mUser = databaseId.getCurrentUser();
  }

  /**
   * Metodo public utilizzato per prelevare l'id dell'utente corrente.
   *
   * @return Stringa contenente l'id.
   */
  public String getUserId() {
    return mUser.getUid();
  }

  /**
   * Metodo public utilizzato per prelevare il nome dell'utente corrente.
   *
   * @return Stringa contenente il nome.
   */
  public String getUserName() {
    return mUser.getDisplayName();
  }

  /**
   * <p>Metodo utilizzato per effettuare il logout dal database.</p>
   */
  public void logout() {
    FirebaseAuth.getInstance().signOut();
  }

  /**
   * <p>Metodi per DatabaseReference.</p>
   */
  /**
   * Metodo public usato per avere un riferimento ad una certa tabella del database.
   *
   * @param reference Stringa contenente il nome della tabella a cui si vuole accedere.
   * @return DatabaseReference riferimento alla tabella desiderata del database.
   */
  public DatabaseReference istanceReference(String reference) {
    DatabaseReference temp = FirebaseDatabase.getInstance().getReference(reference);
    return temp;
  }

  /**
   * Metodo public usato per accedere ad un certo campo di una tabella specifica del database.
   *
   * @param reference Stringa contenenente il nome dall tabella a cui si vuole accedere.
   * @param childId   Stringa contenente il nome del campo della tabella a cui si vuole accedere.
   * @return DatabaseReference riferimento al campo della tabella del database desiderato.
   */
  public DatabaseReference getChild(String reference, String childId) {
    DatabaseReference temp = FirebaseDatabase.getInstance().getReference(reference).child(childId);
    return temp;
  }

  /**
   * Metodo usato per generare un nuovo id all'interno di un certo riferimento al database.
   *
   * @param data Oggeto contenente il riferimento al database desiderato.
   * @return Stringa contenente il nuovo id.
   */
  public String getIdObject(DatabaseReference data) {
    return data.push().getKey();
  }

  /**
   * Metodo usato per inserire un oggetto all'interno del database.
   *
   * @param data    Oggetto contenente il riferimento al database.
   * @param idChild Stringa contenente il campo a cui si vuole accedere per
   *                effettuare l'inserimento.
   * @param object  Oggetto che si vuole inserire nel database.
   */
  @Override
  public void addValue(DatabaseReference data, String idChild, Object object) {
    data.child(idChild).setValue((Commento) object);
  }

  /**
   * Metodo usato per inserire un oggetto all'interno del database.
   * (Seconda versione del metodo precedente)
   *
   * @param data   Oggetto contenente il riferimento al database.
   * @param object Oggetto che si vuole inserire nel database.
   */
  @Override
  public void addValue(DatabaseReference data, Object object) {
    data.setValue(object);
  }

  /**
   * Metodo usato per eliminare un oggetto dal database.
   *
   * @param data    Oggetto contenente il riferimento al database.
   * @param idChild Stringa contenente il campo a cui si vuole accedere per
   *                effettuare l'eliminazione.
   */
  @Override
  public void deleteValue(DatabaseReference data, String idChild) {
    data.child(idChild).removeValue();
  }

  /**
   * Metodo usato per eliminare un oggetto dal database.
   * (Seconda versione del metodo precedente)
   *
   * @param data Oggetto contenente il riferimento al database.
   */
  public void deleteValue(DatabaseReference data) {

  }
}
