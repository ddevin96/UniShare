package com.example.dani.unishare;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
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
 * <p>Activity usata per la gestione dell'attività di nomina dei manager.</p>
 * <p>Questa classe è attiva solo per gli utenti che sono già manager.</p>
 */
public class ManagerActivity extends Activity implements FirebaseInterface {

  Button creaManager;
  DatabaseReference databaseUtente;
  FirebaseAuth databaseId;
  FirebaseUser user;
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
    databaseUtente = istanceReference("utente");

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
        if (!utente.getRuolo().equals("manager")) {
          utente.setRuolo("manager");
          databaseUtente.child(utente.getId()).setValue(utente);
          Toast.makeText(getApplicationContext(), "Manager aggiunto", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(getApplicationContext(), "E' già manager!", Toast.LENGTH_SHORT).show();
        }
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

        UtenteList adapter = new UtenteList(ManagerActivity.this, listaUtenti);
        listViewUtenti.setAdapter(adapter);
      }


      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });

  }

  /**
   * <p>Implementazione delle firme dei metodi dell'interfaccia</p>
   * @see FirebaseInterface
   */
  /**
   * <p>Metodi per FirebaseAuth.</p>
   */
  /**
   * <p>Metodo public utilizzato per creareun istanza di.
   * FirbaseAuth (autentication)</p>
   */
  public void istance() {
    FirebaseApp.initializeApp(this);
    databaseId = FirebaseAuth.getInstance();
  }

  /**
   * <p>Metdo public usato per creare un istanza dell'Utente che.
   * ha effettuato un accesso al database</p>
   */
  @Override
  public void getUser() {
    user = databaseId.getCurrentUser();
  }

  /**
   * Metodo public utilizzato per prelevare l'id dell'utente corrente.
   *
   * @return Stringa contenente l'id.
   */
  @Override
  public String getUserId() {
    return user.getUid();
  }

  /**
   * Metodo public utilizzato per prelevare il nome dell'utente corrente.
   *
   * @return Stringa contenente il nome.
   */
  @Override
  public String getUserName() {
    return user.getDisplayName();
  }

  /**
   * <p>Metodo utilizzato per effettuare il logout dal database.</p>
   */
  @Override
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
   * @param idChild Stringa contenente il campo a cui si vuole accedere
   *                per effettuare l'inserimento.
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
   * (Seconda versione del metodo precedente)
   *
   * @param data Oggetto contenente il riferimento al database.
   */
  @Override
  public void deleteValue(DatabaseReference data) {
    data.removeValue();
  }


  /**
   * Metodo usato per eliminare un oggetto dal database.
   *
   * @param data    Oggetto contenente il riferimento al database.
   * @param idChild Stringa contenente il campo a cui si vuole accedere
   *                per effettuare l'eliminazione.
   */
  @Override
  public void deleteValue(DatabaseReference data, String idChild) {
    data.child(idChild).removeValue();
  }
}
