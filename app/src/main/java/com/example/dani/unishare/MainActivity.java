package com.example.dani.unishare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import java.util.Date;
import java.util.List;

/**
 * <p>Activity usata per la gestione delle attività legate alle bacheche.</p>
 * <p>Inserimento bacheca:</p>
 * <p>Si userà una finestra di dialogo spiegata in seguito.</p>
 *
 * <p>Modifica bacheca:</p>
 * <p>Si userà una finestra di dialogo spiegata in seguito.</p>
 *
 * <p>Verranno gestiti tutti gli eventi conseguenti al "Click" dei bottoni e dei longPress.</p>
 * <p>Verranno gestiti i permessi per gli utenti non loggati(ospiti),
 * gli utenti loggati e i manager</p>
 */
public class MainActivity extends Activity implements FirebaseInterface {

  public static final String BACHECA_ID = "bachecaid";
  public static final String BACHECA_TITLE = "bachecatitle";
  public static final String BACHECA_DESCRIPTION = "bachecadescription";


  EditText editTextTitle;
  EditText editTextDescription;
  Button addButton;
  DatabaseReference databaseBacheca;
  DatabaseReference databaseUtente;
  DatabaseReference databasePost;
  FirebaseUser bUser;
  FirebaseAuth DatabaseId;
  ListView listViewBacheca;
  List<Bacheca> listaBacheca;
  List<Post> listaPost;
  String ruoloManager;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    FirebaseApp.initializeApp(this);

    SharedPreferences sp = getSharedPreferences(
            String.valueOf(getApplicationContext()), Context.MODE_PRIVATE);
    if (!sp.getBoolean("first", false)) {
      SharedPreferences.Editor editor = sp.edit();
      editor.putBoolean("first", true);
      editor.apply();
      Intent intent = new Intent(this, IntoActivity.class); // Call the AppIntro java class
      startActivity(intent);
    }



    databaseBacheca = istanceReference("bacheca");

    editTextTitle = (EditText) this.findViewById(R.id.editTextTitle);
    editTextDescription = (EditText) this.findViewById(R.id.editTextDescription);
    listViewBacheca = (ListView) this.findViewById(R.id.listViewBacheca);
    addButton = (Button) this.findViewById(R.id.addBachecaButton);
    addButton.setVisibility(View.GONE);

    listaBacheca = new ArrayList<>();
    listaPost = new ArrayList<>();

    istance();
    getUser();

    if (bUser != null) {
      addButton.setVisibility(View.VISIBLE);
      databaseUtente = getChild("utente", getUserId());
      databaseUtente.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          ruoloManager = dataSnapshot.child("ruolo").getValue(String.class);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
      });
    }


    addButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        if (bUser != null && isManager()) {
          showCreaBachecaDialog();
        } else {
          Toast.makeText(getApplicationContext(), "Solo il manager può creare una bacheca",
                  Toast.LENGTH_SHORT).show();
        }
      }
    });

    listViewBacheca.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bacheca bacheca = listaBacheca.get(position);
        Intent intent = new Intent(getApplicationContext(), PostActivity.class);
        intent.putExtra(BACHECA_ID, bacheca.getId());
        intent.putExtra(BACHECA_TITLE, bacheca.getTitle());
        intent.putExtra(BACHECA_DESCRIPTION, bacheca.getDescription());
        startActivity(intent);
      }
    });

    listViewBacheca.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Bacheca bacheca = listaBacheca.get(position);
        if (bUser == null || !isManager()) {
          Toast.makeText(getApplicationContext(), "Solo un manager può modificare le bacheche.",
                  Toast.LENGTH_SHORT).show();
        } else {
          showModificaBachecaDialog(bacheca);
        }
        return true;
      }
    });

  }

  @Override
  protected void onStart() {
    super.onStart();
    databaseBacheca.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        listaBacheca.clear();
        for (DataSnapshot personSnapshot : dataSnapshot.getChildren()) {
          Bacheca bacheca = personSnapshot.getValue(Bacheca.class);
          listaBacheca.add(bacheca);
        }

        BachecaList adapter = new BachecaList(MainActivity.this, listaBacheca);
        listViewBacheca.setAdapter(adapter);
      }


      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });


  }

  /**
   * <p>Activity della finestra di dialog usato per creare una bacheca.</p>
   * <p>I campi richiesti sono quelli che compongono il costruttore della classe Bacheca.</p>
   *
   * @see Bacheca
   * <p>I dati vengono reinseriti tramite EditText dedicate.</p>
   * <p>Viene gestito l'evento legato al "Click" del bottone
   * addBachecaButton.</p>
   * <p>Vengono chiamati tutti i metodi di controllo dei parametri inseriti.</p>
   */
  private void showCreaBachecaDialog() {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
    LayoutInflater inflater = getLayoutInflater();
    final View dialogView = inflater.inflate(R.layout.add_bacheca_dialog, null);
    dialogBuilder.setView(dialogView);

    final EditText editTextTitle = (EditText) dialogView.findViewById(R.id.editTextTitle);
    final EditText editTextDescription = (EditText)
            dialogView.findViewById(R.id.editTextDescription);
    final Button addBachecaButton = (Button) dialogView.findViewById(R.id.addButton);

    dialogBuilder.setTitle("Crea bacheca");
    final AlertDialog alertDialog = dialogBuilder.create();
    alertDialog.show();


    addBachecaButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String id = getIdObject(databaseBacheca);

        if (controlloTitolo(editTextTitle.getText().toString(), id)) {
          editTextTitle.setError("Il titolo non può essere vuoto.\n Deve avere un "
                  + "massimo di 20 caratteri.\n Non possono esistere "
                  + "due Bacheche di uno stesso paese.");
          editTextTitle.requestFocus();
          return;
        }
        if (controlloDescrizione(editTextDescription.getText().toString())) {
          editTextDescription.setError("La descrizione non può essere vuota.\n Deve "
                  + "avere un massimo di 200 caratteri.");
          editTextDescription.requestFocus();
          return;
        }

        Date data = new Date();

        Bacheca bacheca = new Bacheca(id, title, description,
                getUserName(), getUserId(), data);
        addValue(databaseBacheca, bacheca.getId(), bacheca);
        Toast.makeText(getApplicationContext(), "Bacheca aggiunta", Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
      }
    });

  }

  /**
   * <p>Activity della finestra di dialog usato per modificare una bacheca creata in precedenza.</p>
   * <p>I campi richiesti sono quelli che compongono il costruttore della classe Bacheca.</p>
   *
   * @param bacheca oggetto contenente la bacheca da modificare
   *                <p>I dati vengono reinseriti tramite EditText dedicate.</p>
   *                <p>Viengono gestiti gli eventi legati al "Click" dei bottoni
   *                modificaBachecaButton e cancellaBachecaButton.</p>
   *                <p>Vengono chiamati tutti i metodi di controllo dei parametri inseriti.</p>
   * @see Bacheca
   */
  private synchronized void showModificaBachecaDialog(final Bacheca bacheca) {

    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
    LayoutInflater inflater = getLayoutInflater();
    final View dialogView = inflater.inflate(R.layout.modifica_bacheca_dialog, null);
    dialogBuilder.setView(dialogView);

    final EditText editTextTitleModifica = (EditText)
            dialogView.findViewById(R.id.editTextTitoloBacheca);
    final EditText editTextDescriptionModifica = (EditText)
            dialogView.findViewById(R.id.editTextDescriptionBacheca);
    final Button modificaBachecaButton = (Button)
            dialogView.findViewById(R.id.modificaBachecaButton);
    final Button cancellaBachecaButton = (Button)
            dialogView.findViewById(R.id.cancellaBachecaButton);

    editTextTitleModifica.setText(bacheca.getTitle());
    editTextDescriptionModifica.setText(bacheca.getDescription());
    final String id = bacheca.getId();

    dialogBuilder.setTitle("Modifica bacheca");
    final AlertDialog alertDialog = dialogBuilder.create();
    alertDialog.show();


    modificaBachecaButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String title = editTextTitleModifica.getText().toString();
        String description = editTextDescriptionModifica.getText().toString();


        if (controlloTitolo(editTextTitleModifica.getText().toString(), bacheca.getId())) {
          editTextTitleModifica.setError("Il titolo non può essere vuoto."
                  + "\n Deve avere un massimo di 20 caratteri.\n Non possono "
                  + "esistere due Bacheche di uno stesso paese.");
          editTextTitleModifica.requestFocus();
          return;
        }
        if (controlloDescrizione(editTextDescriptionModifica.getText().toString())) {
          editTextDescriptionModifica.setError("La descrizione non può essere "
                  + "vuota.\n Deve avere un massimo di 200 caratteri.");
          editTextDescriptionModifica.requestFocus();
          return;
        }

        Date data = new Date();
        String id = bacheca.getId();
        Bacheca bacheca = new Bacheca(id, title, description,
                getUserName(), getUserId(), data);
        addValue(databaseBacheca, bacheca.getId(), bacheca);
        Toast.makeText(getApplicationContext(), "Bacheca Modificata",
                Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
      }
    });

    databasePost = getChild("post", id);
    databasePost.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
          Post post = postSnapshot.getValue(Post.class);
          listaPost.add(post);
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });

    cancellaBachecaButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        cancellaBacheca(id);
        alertDialog.dismiss();
      }
    });

  }

  /**
   * metodo private usato per cancellare una bacheca dal database.
   *
   * @param id codice univoco che identifica la bacheca
   */
  private void cancellaBacheca(String id) {
    deleteValue(databaseBacheca, id);
    for (Post elemento : listaPost) {
      String idPost = elemento.getId();
      DatabaseReference commenti = istanceReference("commento");
      DatabaseReference postDaEliminare = getChild("post", id);
      deleteValue(postDaEliminare, idPost);
      deleteValue(commenti, idPost);
    }

    Toast.makeText(this, "Bacheca eliminata", Toast.LENGTH_SHORT).show();
  }

  /**
   * Metodo protected usato per verificare se un utente loggato è manager.
   *
   * @return valore boolean.
   * <p>Se il valore di ritorno è true, l'utente loggato è  Manager.</p>
   */
  protected boolean isManager() {
    if (ruoloManager.equals("manager")) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Metodo protected usato per confrontare una bacheca
   * con le altre già presenti all'interno del databaase.
   * Il controllo viene adoperato allo scopo di inserire
   * bacheche che abbiano tutti titolii diversi.
   *
   * @param titolo Stringa contenente il titolo della bacheca da confrontare
   * @param id     Stringa contenente il codice univoco della bacheca da confrontare
   * @return valore boolean.
   * <p>Se il valore di ritorno è true, non esiste nessuna bacheca
   * con lo stesso titolo di quella che è stata confrontata.</p>
   */
  protected boolean confrontaBacheche(String titolo, String id) {
    boolean value = true;
    if (!listaBacheca.isEmpty()) {
      for (Bacheca bacheca : listaBacheca) {
        if (bacheca.getTitle().equals(titolo) && !bacheca.getId().equals(id)) {
          value = true;
          break;
        } else {
          value = false;
        }
      }
    } else {
      return false;
    }
    return value;
  }

  /**
   * Metodo protected usato per controllare se il titolo rispetti le precondizioni stabilite.
   *
   * @param titolo Stringa contenente il titolo inserito dall'utente
   * @param id     Stringa contenente il codice univoco della bacheca presa in considerazione.
   * @return valore boolean.
   * <p>Se il valore di ritorno è true, il titolo non rispatta le precondizioni.</p>
   */
  protected boolean controlloTitolo(String titolo, String id) {
    if (titolo.isEmpty() || titolo.length() > 20
            || confrontaBacheche(titolo, id)) {
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
    if (descrizione.isEmpty() || descrizione.length() > 200) {
      return true;
    } else {
      return false;
    }
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
    if (bUser != null) {
      itemLogin.setVisible(false);
    }
    MenuItem itemLogout = menu.getItem(1);
    if (bUser == null) {
      itemLogout.setVisible(false);
    }
    MenuItem itemRegistrazione = menu.getItem(2);
    if (bUser != null) {
      itemRegistrazione.setVisible(false);
    }
    MenuItem itemProfilo = menu.getItem(4);
    if (bUser == null) {
      itemProfilo.setVisible(false);
    }
    MenuItem itemManager = menu.getItem(5);
    itemManager.setVisible(false);
    if (bUser != null) {
      if (isManager()) {
        itemManager.setVisible(true);
      }
    }

    return true;
  }

  /**
   * Metodo public usato per il reindirizzamento alle pagine.
   * desiderato al momento del click di ogni voce del menu
   *
   * @param item Oggetto contenente l'oggetto che rappresenta la voce singola dell'optionMenu
   * @return valore boolean.
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

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
    DatabaseId = FirebaseAuth.getInstance();
  }

  /**
   * <p>Metdo public usato per creare un istanza dell'Utente che.
   * ha effettuato un accesso al database</p>
   */
  @Override
  public void getUser() {
    bUser = DatabaseId.getCurrentUser();
  }

  /**
   * Metodo public utilizzato per prelevare l'id dell'utente corrente.
   *
   * @return Stringa contenente l'id.
   */
  @Override
  public String getUserId() {
    return bUser.getUid();
  }

  /**
   * Metodo public utilizzato per prelevare il nome dell'utente corrente.
   *
   * @return Stringa contenente il nome.
   */
  @Override
  public String getUserName() {
    return bUser.getDisplayName();
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
    data.child(idChild).setValue((Bacheca) object);
  }

  /**
   * Metodo usato per inserire un oggetto all'interno del database.
   * (Seconda versione del metodo precedente)
   *
   * @param data   Oggetto contenente il riferimento al database.
   * @param object Oggetto che si vuole inserire nel database.
   */
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
    data.removeValue();
  }
}
