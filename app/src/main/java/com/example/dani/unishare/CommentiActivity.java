package com.example.dani.unishare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Date;
import java.util.List;

/**
 * <p>Activity usata per la gestione delle attività legate ai commenti.</p>
 * <p>Inserimento del commento: </p>
 * <p>Spiegata nella documentazione legata al metodo addCommento().</p>
 *
 * <p>Modifica commento:</p>
 * <p>Si userà una finestra di dialogo spiegata in seguito.</p>
 *
 * <p>Verranno gestiti tutti gli eventi conseguenti al "Click" dei bottoni e dei longPress.</p>
 * <p>Verranno gestiti i permessi per gli utenti non loggati(ospiti),
 * gli utenti loggati e i manager</p>
 * <p>Verrà gestita la ricerca di un commento all'interno di un post (riga 140)</p>
 */
public class CommentiActivity extends Activity implements FirebaseInterface {

  EditText searchbar;
  Button searcButton;
  TextView textViewPostName;
  TextView textViewPostDescription;
  TextView textViewPostAuthor;
  EditText editTextCommentDescription;
  Button addCommentButton;
  ListView listViewCommenti;
  DatabaseReference databaseCommenti;
  FirebaseAuth databaseId;
  FirebaseUser cUser;
  DatabaseReference databaseAuthor;
  List<Commento> lista;
  String author;
  String ruolo;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_commenti);

    searchbar = (EditText) findViewById(R.id.searchbarCommento);
    searcButton = (Button) findViewById(R.id.searchButtonCommento);
    textViewPostName = (TextView) findViewById(R.id.textViewPostName);
    textViewPostDescription = (TextView) findViewById(R.id.textViewPostDescription);
    textViewPostAuthor = (TextView) findViewById(R.id.textViewPostAuthor);
    editTextCommentDescription = (EditText) findViewById(R.id.editTextCommentDescription);
    editTextCommentDescription.setVisibility(View.GONE);
    addCommentButton = (Button) findViewById(R.id.addCommentButton);
    addCommentButton.setVisibility(View.GONE);
    listViewCommenti = (ListView) findViewById(R.id.listViewCommenti);

    istance();
    getUser();
    lista = new ArrayList<>();

    Intent intent = getIntent();
    String title = intent.getStringExtra(PostActivity.POST_TITLE);
    String description = intent.getStringExtra(PostActivity.POST_DESCRIZIONE);
    String autore = intent.getStringExtra(PostActivity.POST_AUTORE);
    textViewPostName.setText(title);
    textViewPostDescription.setText(description);
    textViewPostAuthor.setText(autore);

    String idPost = intent.getStringExtra(PostActivity.POST_ID);

    databaseCommenti = getChild("commento", idPost);
    if (cUser != null) {
      databaseAuthor = getChild("utente", getUserId());
      databaseAuthor.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          author = dataSnapshot.child("nome").getValue().toString();
          ruolo = dataSnapshot.child("ruolo").getValue(String.class);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
      });


      addCommentButton.setVisibility(View.VISIBLE);
      editTextCommentDescription.setVisibility(View.VISIBLE);
    }
    addCommentButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        addCommento();
      }
    });

    listViewCommenti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Commento commento = lista.get(position);

        if (cUser == null) {
          Toast.makeText(getApplicationContext(), "Non sei autorizzato a modificare",
                  Toast.LENGTH_SHORT).show();
        } else if (isCreator(commento.getAuthorId()) || isManager()) {
          modificaCommentoDialog(commento);
        } else {
          Toast.makeText(getApplicationContext(), "Non sei autorizzato a modificare",
                  Toast.LENGTH_SHORT).show();
        }
      }
    });

    searcButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        List<Commento> listaCommentiTrovati = new ArrayList<>();
        List<String> listaParole = trovaParole(searchbar.getText().toString());

        for (int k = 0; k < lista.size(); k++) {
          Commento commento = lista.get(k);
          for (int j = 0; j < listaParole.size(); j++) {
            String elem = listaParole.get(j);
            if (commento.getDescription().contains(elem)) {
              listaCommentiTrovati.add(commento);
              break;
            }
          }

        }
        lista = listaCommentiTrovati;
        CommentiList adapter1 = new CommentiList(CommentiActivity.this, listaCommentiTrovati);
        listViewCommenti.setAdapter(adapter1);

      }
    });
  }

  @Override
  protected void onStart() {
    super.onStart();
    databaseCommenti.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        lista.clear();
        for (DataSnapshot personSnapshot : dataSnapshot.getChildren()) {
          Commento commento = personSnapshot.getValue(Commento.class);
          lista.add(commento);
        }

        CommentiList adapter = new CommentiList(CommentiActivity.this, lista);
        listViewCommenti.setAdapter(adapter);
      }


      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });


  }

  /**
   * <p>Activity della finestra di dialog usato per modificare il commento di un utente.</p>
   * <p>I campi richiesti sono quelli che compongono il costruttore della classe Commento</p>
   *
   * @param commento Oggetto contenente il commento da modificare
   *                 <p>I dati vengono reinseriti tramite EditText dedicate.</p>
   *                 <p>Vengono gestiti gli eventi legati al "Click" dei bottoni
   *                 modificaCommentoButton e cancellaCommentoButton.</p>
   *                 <p>Vengono chiamati tutti i metodi di controllo dei parametri inseriti.</p>
   * @see Commento
   */
  private synchronized void modificaCommentoDialog(Commento commento) {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
    LayoutInflater inflater = getLayoutInflater();
    final View dialogView = inflater.inflate(R.layout.modifica_commento_dialog, null);
    dialogBuilder.setView(dialogView);

    final EditText editTextDescriptionCommento;
    final Button modificaCommentoButton;
    final Button cancellaCommentoButton;

    editTextDescriptionCommento = (EditText)
            dialogView.findViewById(R.id.editTextDescriptionCommento);
    modificaCommentoButton = (Button) dialogView.findViewById(R.id.modificaCommentoButton);
    cancellaCommentoButton = (Button) dialogView.findViewById(R.id.cancellaCommentoButton);
    editTextDescriptionCommento.setText(commento.getDescription());
    final String id = commento.getId();
    final String idAuthor = commento.getAuthorId();
    final String author = commento.getAuthor();

    dialogBuilder.setTitle("Modifica Commento");
    final AlertDialog alertDialog = dialogBuilder.create();
    alertDialog.show();

    modificaCommentoButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String description = editTextDescriptionCommento.getText().toString();

        if (controlloDescrizione(description)) {
          editTextDescriptionCommento
                  .setError("Il commento non può essere vuoto\nMax 65535 caratteri");
          editTextDescriptionCommento.requestFocus();
          return;
        }

        Date date = new Date();
        Commento commento = new Commento(id, description,
                author, idAuthor, date);
        //databaseCommenti.child(commento.getId()).setValue(commento);
        addValue(databaseCommenti, commento.getId(), commento);
        Toast.makeText(getApplicationContext(), "Commento modificato", Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
      }
    });

    cancellaCommentoButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        cancellaCommento(id);
        alertDialog.dismiss();
      }
    });
  }

  /**
   * <p>Metodo usato per aggiungere un commento ad un post.</p>
   * <p>Verrà inserita tramite EditText la descrizione del Commento.</p>
   * <p>Gli altri dati richiesti nella classe Commento verranno prelevati da database e,
   * alcuni di essi, verranno visualizzati tramite TextView</p>
   *
   * @see Commento
   */
  private void addCommento() {
    String description = editTextCommentDescription.getText().toString();
    String idAuthor = getUserId();
    Date date = new Date();

    if (controlloDescrizione(description)) {
      editTextCommentDescription.setError("Il commento non può essere vuoto\nMax 65535 caratteri");
      editTextCommentDescription.requestFocus();
      return;
    }

    String id = getIdObject(databaseCommenti);
    Commento comment = new Commento(id, description, author, idAuthor, date);
    addValue(databaseCommenti, id, comment);
    editTextCommentDescription.setText("");
    Toast.makeText(this, "Commento Inserito", Toast.LENGTH_SHORT).show();
  }

  /**
   * metodo private usato per cancellare un commento dal database.
   *
   * @param id codice univoco che identifica il commento
   */
  private void cancellaCommento(String id) {
    deleteValue(databaseCommenti, id);
    Toast.makeText(getApplicationContext(), "Commento Eliminato", Toast.LENGTH_SHORT).show();
  }

  /**
   * Metodo protected usato per verificare se un utente loggato è manager.
   *
   * @return valore boolean.
   * <p>Se il valore di ritorno è true, l'utente loggato è  Manager.</p>
   */
  protected boolean isManager() {
    if (ruolo.equals("manager")) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Metodo protected usato per verificare se l'utente corrente è l'autore di un certo commento.
   *
   * @param id codice univoco che identifica l'utente.
   * @return valore boolean.
   * <p>Se il valore di ritorno è true, l'utente associato all'id (parametro)
   * è l'autore del commento.</p>
   */
  protected boolean isCreator(String id) {
    if (getUserId().equals(id)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Metodo protected utilizzato per verificare che la descrizione
   * di un commento rispetti le precondizioni stabilite.
   *
   * @param descrizione Stringa che contiene la descriione da controllare.
   * @return valore boolean.
   * <p>Se il valore di ritorno è true, la descrizione NON rispetta le caratteristice.</p>
   */
  protected boolean controlloDescrizione(String descrizione) {
    if (descrizione.isEmpty() || descrizione.length() >= 65535) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Metodo protected utilizzato per prelevare tutte le parole
   * inserite in una stringa di ricerca (frase).
   *
   * @param stringa Stringa di ricerca.
   * @return listaParole Lista di Stringhe che contiene le parole
   *         contenute nella frase digitata nella barra di ricerca.
   */
  protected List<String> trovaParole(String stringa) {
    String parola = "";
    List<String> listaParole = new ArrayList<>();
    for (int i = 0; i < stringa.length(); i++) {
      if ((stringa.charAt(i) > 'a' && stringa.charAt(i) < 'z')
              || (stringa.charAt(i) > 'A' && stringa.charAt(i) < 'Z')
              || (stringa.charAt(i) > '0' && stringa.charAt(i) < '9') || stringa.charAt(i) != ' ') {
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
   * Metodo public utilizzato per la creazione di un option menu.
   *
   * @param menu Oggetto contenente il menu
   * @return valore boolean.
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.menu_generale, menu);
    //return super.onCreateOptionsMenu(menu);

    return true;
  }

  /**
   * Metodo public usato per la visualizzazione delle voci del menu in base al ruolo dell'utente.
   *
   * @param menu Oggetto contenente il menu
   * @return valore boolean.
   */
  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    MenuItem itemLogin = menu.getItem(0);
    if (cUser != null) {
      itemLogin.setVisible(false);
    }
    MenuItem itemLogout = menu.getItem(1);
    if (cUser == null) {
      itemLogout.setVisible(false);
    }
    MenuItem itemRegistrazione = menu.getItem(2);
    if (cUser != null) {
      itemRegistrazione.setVisible(false);
    }
    MenuItem itemProfilo = menu.getItem(4);
    if (cUser == null) {
      itemProfilo.setVisible(false);
    }
    MenuItem itemManager = menu.getItem(5);
    itemManager.setVisible(false);
    if (cUser != null) {
      if (isManager()) {
        itemManager.setVisible(true);
      }
    }

    return true;
  }

  /**
   * Metodo public usato per il reindirizzamento alle pagine.
   * desiderato al momento del click di ogni voce del menu.
   *
   * @param item Oggetto contenente l'oggetto che rappresenta la voce singola dell'optionMenu
   * @return valore boolean.
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    //return super.onOptionsItemSelected(item);
    switch (item.getItemId()) {
      case R.id.loginMenu:
        finish();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        break;
      case R.id.logoutMenu:
        logout();
        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent1);
        finish();
        break;
      case R.id.registrazioneMenu:
        Intent intent2 = new Intent(getApplicationContext(), RegistrazioneActivity.class);
        startActivity(intent2);
        break;
      case R.id.cercaMenu:
        Intent intent3 = new Intent(getApplicationContext(), RicercaProfiloActivity.class);
        startActivity(intent3);
        break;
      case R.id.profiloMenu:
        Intent intent4 = new Intent(getApplicationContext(), ProfiloActivity.class);
        startActivity(intent4);
        break;
      case R.id.managerMenu:
        Intent intent5 = new Intent(getApplicationContext(), ManagerActivity.class);
        startActivity(intent5);
        break;
      default:
        break;

    }

    return true;
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
  @Override
  public void getUser() {
    cUser = databaseId.getCurrentUser();
  }

  /**
   * Metodo public utilizzato per prelevare l'id dell'utente corrente.
   *
   * @return Stringa contenente l'id.
   */
  @Override
  public String getUserId() {
    return cUser.getUid();
  }

  /**
   * Metodo public utilizzato per prelevare il nome dell'utente corrente.
   *
   * @return Stringa contenente il nome.
   */
  @Override
  public String getUserName() {
    return cUser.getDisplayName();
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
  public void addValue(DatabaseReference data, Object object) {
    data.setValue((Commento) object);
  }

  /**
   * Metodo usato per eliminare un oggetto dal database.
   *
   * @param data    Oggetto contenente il riferimento al database.
   * @param idChild Stringa contenente il campo a cui si vuole
   *                accedere per effettuare l'eliminazione.
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
    data.removeValue();
  }
}
