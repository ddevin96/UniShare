package com.example.dani.unishare;

import android.annotation.SuppressLint;
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
 * <p>Activity usata per la gestione delle attività legate ai post.</p>
 * <p>Inserimento post:</p>
 * <p>Si userà una finestra di dialogo spiegata in seguito.</p>
 *
 * <p>Modifica post:</p>
 * <p>Si userà una finestra di dialogo spiegata in seguito.</p>
 *
 * <p>Verranno gestiti tutti gli eventi conseguenti al "Click" dei bottoni e dei longPress.</p>
 * <p>Verranno gestiti i permessi per gli utenti non loggati(ospiti),
 * gli utenti loggati e i manager</p>
 * <p>Verrà gestita la ricerca di un post all'interno di una bacheca (riga 112)</p>
 */
public class PostActivity extends Activity implements FirebaseInterface {

  public static final String POST_ID = "postid";
  public static final String POST_TITLE = "posttitle";
  public static final String POST_DESCRIZIONE = "postdescrizione";
  public static final String POST_AUTORE = "postautore";

  EditText searchbarPost;
  Button searchButton;
  TextView textViewTitolo;
  TextView textViewDescrizione;
  Button addPost;
  DatabaseReference databasePost;
  DatabaseReference databaseUtente;
  ListView listViewPost;
  List<Post> listaPost;
  EditText editTextTitle;
  EditText editTextDescription;
  FirebaseAuth databaseId;
  FirebaseUser pUser;
  String ruoloUser;
  String nomeUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_post);

    istance();
    getUser();
    searchbarPost = (EditText) this.findViewById(R.id.searchbarPost);
    searchButton = (Button) this.findViewById(R.id.searchButton);
    textViewTitolo = (TextView) this.findViewById(R.id.textViewTitolo);
    textViewDescrizione = (TextView) this.findViewById(R.id.textViewDescrizione);
    listViewPost = (ListView) this.findViewById(R.id.listViewPost);
    addPost = (Button) this.findViewById(R.id.addPost);
    addPost.setVisibility(View.GONE);
    listaPost = new ArrayList<>();

    Intent intent = getIntent();
    String idBacheca = intent.getStringExtra(MainActivity.BACHECA_ID);
    String title = intent.getStringExtra(MainActivity.BACHECA_TITLE);
    String description = intent.getStringExtra(MainActivity.BACHECA_DESCRIPTION);
    textViewTitolo.setText(title);
    textViewDescrizione.setText(description);

    databasePost = getChild("post", idBacheca);
    if (pUser != null) {
      addPost.setVisibility(View.VISIBLE);
      databaseUtente = getChild("utente", getUserId());
      databaseUtente.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          ruoloUser = dataSnapshot.child("ruolo").getValue(String.class);
          nomeUser = dataSnapshot.child("nome").getValue(String.class);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
      });
    }


    searchButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        List<Post> listaPostTrovati = new ArrayList<>();
        List<String> listaParole = trovaParole(searchbarPost.getText().toString());

        for (int k = 0; k < listaPost.size(); k++) {
          Post post = listaPost.get(k);
          for (int j = 0; j < listaParole.size(); j++) {
            String elem = listaParole.get(j);
            if (post.getTitle().contains(elem)) {
              listaPostTrovati.add(post);
              break;
            }
          }

        }
        listaPost = listaPostTrovati;
        PostList adapter1 = new PostList(PostActivity.this, listaPostTrovati);
        listViewPost.setAdapter(adapter1);
      }
    });

    addPost.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        showCreateDialog();
      }
    });

    listViewPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Post post = listaPost.get(position);
        Intent intent = new Intent(PostActivity.this, CommentiActivity.class);
        intent.putExtra(POST_ID, post.getId());
        intent.putExtra(POST_TITLE, post.getTitle());
        intent.putExtra(POST_DESCRIZIONE, post.getDescription());
        intent.putExtra(POST_AUTORE, post.getAuthor());
        startActivity(intent);

      }
    });

    listViewPost.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Post post = listaPost.get(position);
        if (pUser != null && (isCreator(post.getAuthorId()) || isManager())) {
          modificaPostDialog(post);
        } else {
          Toast.makeText(getApplicationContext(), "Non sei autorizzato a modificare",
                  Toast.LENGTH_SHORT).show();
        }
        return true;
      }
    });

  }

  protected void onStart() {
    super.onStart();
    databasePost.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        listaPost.clear();
        for (DataSnapshot personSnapshot : dataSnapshot.getChildren()) {
          Post post = personSnapshot.getValue(Post.class);
          listaPost.add(post);
        }

        PostList adapter = new PostList(PostActivity.this, listaPost);
        listViewPost.setAdapter(adapter);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  /**
   * <p>Activity della finestra di dialog usato per creare una post.</p>
   * <p>I campi richiesti sono quelli che compongono il costruttore della classe Post.</p>
   *
   * @see Post
   * <p>I dati vengono reinseriti tramite EditText dedicate.</p>
   * <p>Viene gestito l'evento legato al "Click" del bottone
   * pubblica.</p>
   * <p>Vengono chiamati tutti i metodi di controllo dei parametri inseriti.</p>
   */
  @SuppressLint("WrongViewCast")
  private void showCreateDialog() {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
    LayoutInflater inflater = getLayoutInflater();
    final View dialogView = inflater.inflate(R.layout.activity_add_post_form, null);
    dialogBuilder.setView(dialogView);

    final EditText editTextTitle;
    final EditText editTextDescription;
    final Button pubblica;

    editTextTitle = (EditText) dialogView.findViewById(R.id.titlePost);
    editTextDescription = (EditText) dialogView.findViewById(R.id.descrizionePost);
    pubblica = (Button) dialogView.findViewById(R.id.addPostButton);
    dialogBuilder.setTitle("post");
    final AlertDialog alertDialog = dialogBuilder.create();
    alertDialog.show();

    pubblica.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        if (controllaParametro(editTextTitle.getText().toString())) {
          editTextTitle.setError("Il titolo non può essere vuoto.\n "
                  + "Deve avere un massimo di 65534 caratteri.");
          editTextTitle.requestFocus();
          return;
        }
        if (controllaParametro(editTextDescription.getText().toString())) {
          editTextDescription.setError("La descrizione non può essere vuota.\n "
                  + "Deve avere un massimo di 65534 caratteri.");
          editTextDescription.requestFocus();
          return;
        }
        Date data = new Date();
        String idAuthor = getUserId();
        String id = getIdObject(databasePost);
        Post post = new Post(id, title, description, nomeUser, idAuthor, data);
        //databasePost.child(post.getId()).setValue(post);
        addValue(databasePost, post.getId(), post);
        Toast.makeText(getApplicationContext(), "Post aggiunto", Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();

      }
    });
  }

  /**
   * <p>Activity della finestra di dialog usato per modificare il post di un utente.</p>
   * <p>I campi richiesti sono quelli che compongono il costruttore della classe Post</p>
   *
   * @param post Oggetto contenente il post da modificare
   *             <p>I dati vengono reinseriti tramite EditText dedicate.</p>
   *             <p>Vengono gestiti gli eventi legati al "Click" dei bottoni
   *             modificaPostButton e cancellaPostButton.</p>
   *             <p>Vengono chiamati tutti i metodi di controllo dei parametri inseriti.</p>
   * @see Post
   */
  private synchronized void modificaPostDialog(Post post) {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
    LayoutInflater inflater = getLayoutInflater();
    final View dialogView = inflater.inflate(R.layout.modifica_post_dialog, null);
    dialogBuilder.setView(dialogView);

    final EditText editTextTitlePost;
    final EditText editTextDescriptionPost;
    final Button modificaPostButton;
    final Button cancellaPostButton;

    editTextTitlePost = (EditText) dialogView.findViewById(R.id.editTextTitlePost);
    editTextDescriptionPost = (EditText) dialogView.findViewById(R.id.editTextDescriptionPost);
    modificaPostButton = (Button) dialogView.findViewById(R.id.modificaPostButton);
    cancellaPostButton = (Button) dialogView.findViewById(R.id.cancellaPostButton);
    editTextTitlePost.setText(post.getTitle());
    editTextDescriptionPost.setText(post.getDescription());
    final String id = post.getId();
    final String idAuthor = post.getAuthorId();
    final String Author = post.getAuthor();


    dialogBuilder.setTitle("Modifica Post");
    final AlertDialog alertDialog = dialogBuilder.create();
    alertDialog.show();

    modificaPostButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String title = editTextTitlePost.getText().toString();
        String description = editTextDescriptionPost.getText().toString();
        if (controllaParametro(editTextTitlePost.getText().toString())) {
          editTextTitlePost.setError("Il titolo non può essere vuoto.\n "
                  + "Deve avere un massimo di 65534 caratteri.");
          editTextTitlePost.requestFocus();
          return;
        }
        if (controllaParametro(editTextDescriptionPost.getText().toString())) {
          editTextDescriptionPost.setError("La descrizione non può essere vuota."
                  + "\n Deve avere un massimo di 65534 caratteri.");
          editTextDescriptionPost.requestFocus();
          return;
        }
        Date date = new Date();
        Post postUpdate = new Post(id, title, description,
                Author, idAuthor, date);
        addValue(databasePost, postUpdate.getId(), postUpdate);
        Toast.makeText(getApplicationContext(), "Post Modificato",
                Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
      }
    });

    cancellaPostButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        deletePost(id);
        alertDialog.dismiss();
      }
    });
  }

  /**
   * Metodo usato per eliminare un post dal database.
   *
   * @param id Stringa contenente il codice univoco del post da eliminare.
   */
  private void deletePost(String id) {
    deleteValue(databasePost, id);
    DatabaseReference postCommenti = istanceReference("commento");
    deleteValue(postCommenti, id);
    Toast.makeText(getApplicationContext(), "Post Eliminato", Toast.LENGTH_SHORT).show();
  }

  /**
   * Metodo protected utilizzato per verificare che il parametro
   * di un post (Titolo o Descrizione) rispetti le precondizioni stabilite.
   *
   * @param parametro Stringa che contiene il parametro da controllare.
   * @return valore boolean.
   * <p>Se il valore di ritorno è true, il parametro NON rispetta le caratteristice.</p>
   */
  protected boolean controllaParametro(String parametro) {
    if (parametro.isEmpty() || parametro.length() > 65534) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Metodo protected usato per verificare se un utente loggato è manager.
   *
   * @return valore boolean.
   * <p>Se il valore di ritorno è true, l'utente loggato è  Manager.</p>
   */
  protected boolean isManager() {
    if (ruoloUser.equals("manager")) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Metodo protected usato per verificare se l'utente corrente è l'autore di un certo post.
   *
   * @param id codice univoco che identifica l'utente.
   * @return valore boolean.
   * <p>Se il valore di ritorno è true, l'utente associato all'id (parametro)
   * è l'autore del post.</p>
   */
  private boolean isCreator(String id) {
    if (getUserId().equals(id)) {
      return true;
    } else {
      return false;
    }
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
    if (pUser != null) {
      itemLogin.setVisible(false);
    }
    MenuItem itemLogout = menu.getItem(1);
    if (pUser == null) {
      itemLogout.setVisible(false);
    }
    MenuItem itemRegistrazione = menu.getItem(2);
    if (pUser != null) {
      itemRegistrazione.setVisible(false);
    }
    MenuItem itemProfilo = menu.getItem(4);
    if (pUser == null) {
      itemProfilo.setVisible(false);
    }
    MenuItem itemManager = menu.getItem(5);
    itemManager.setVisible(false);
    if (pUser != null) {
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
    databaseId = FirebaseAuth.getInstance();
  }

  /**
   * <p>Metdo public usato per creare un istanza dell'Utente che.
   * ha effettuato un accesso al database</p>
   */
  @Override
  public void getUser() {
    pUser = databaseId.getCurrentUser();
  }

  /**
   * Metodo public utilizzato per prelevare l'id dell'utente corrente.
   *
   * @return Stringa contenente l'id.
   */
  @Override
  public String getUserId() {
    return pUser.getUid();
  }

  /**
   * Metodo public utilizzato per prelevare il nome dell'utente corrente.
   *
   * @return Stringa contenente il nome.
   */
  @Override
  public String getUserName() {
    return pUser.getDisplayName();
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
    data.child(idChild).setValue((Post) object);
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
   * @param idChild Stringa contenente il campo a cui si vuole accedere
   *                per effettuare l'eliminazione.
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
  @Override
  public void deleteValue(DatabaseReference data) {
    data.removeValue();
  }
}

