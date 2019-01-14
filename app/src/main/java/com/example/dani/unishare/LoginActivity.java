package com.example.dani.unishare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dani.unishare.FirebaseInterface;
import com.example.dani.unishare.MainActivity;
import com.example.dani.unishare.R;
import com.example.dani.unishare.Utente;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Activity usata per effettuare l'accesso di un utente all'interno del sistema.</p>
 * <p>Verranno inseriti tramite EditText tutti i dati richiesti per l'accesso.
 * (Credenziali d'accesso: E-mail, password)</p>
 *
 * <p>Verranno gestiti tutti gli eventi conseguenti al "Click" dei bottoni.</p>
 * <p>Verranno effettuati tutti i controlli
 * per l'accettazione dei parametri inseriti dall'Utente (riga 139).</p>
 */
public class LoginActivity extends Activity implements FirebaseInterface {

  public EditText email;
  public EditText password;
  Button accedi;
  FirebaseUser user;
  FirebaseAuth databaseLogin;
  DatabaseReference databaseUtente;
  List<Utente> listaUtente;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    email = (EditText) findViewById(R.id.InserisciEmail);
    password = (EditText) findViewById(R.id.InserisciPassword);
    accedi = (Button) findViewById(R.id.Accedi);

    listaUtente = new ArrayList<>();

    istance();
    databaseUtente = istanceReference("utente");

    databaseUtente.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        listaUtente.clear();
        for (DataSnapshot utenteSnapshot : dataSnapshot.getChildren()) {
          Utente utente = utenteSnapshot.getValue(Utente.class);
          listaUtente.add(utente);
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });

    accedi.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String eMail = email.getText().toString();
        String password1 = password.getText().toString();

        if (!controlloMail(eMail)) {
          email.setError("L'email non può essere vuota.\nDeve rispettare il formato."
                  + "\n può avere al massimo 63 caratteri.");
          email.requestFocus();
          return;
        }

        if (!confrontaMail(eMail)) {
          email.setError("L' e-mail è errata.");
          email.requestFocus();
          return;
        }

        if (!controlloPassword(password1)) {
          password.setError("La password non può essere vuota\nMin 8 caratteri\n"
                  + "Max 20 caratteri");
          password.requestFocus();
          return;
        }

        databaseLogin.signInWithEmailAndPassword(eMail,
                password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                      finish();
                      Toast.makeText(getApplicationContext(), "Login effettuato con successo.",
                              Toast.LENGTH_SHORT).show();
                      startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                      Toast.makeText(getApplicationContext(), "L'E-mail o la password sono errate.",
                              Toast.LENGTH_SHORT).show();
                    }
                  }
                });
      }
    });
    getUser();
    if (user != null) {
      startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
  }

  /**
   * Metodo private usato per confrontare la password inserita dall'utente con il formato richiesto.
   *
   * @param password Stringa contenente la password inserita dall'utente.
   * @return Valore boolean.
   * <p>Se il valore restituito è ture, il formato richiesto è stato rispettato.</p>
   */
  protected static boolean isValidPassword(String password) {

    Pattern pattern;
    Matcher matcher;
    final String Password_Pattern =
            "^(?=.*[0-9])(?=.*[A-Z])(?=.[a-z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    pattern = Pattern.compile(Password_Pattern);
    matcher = pattern.matcher(password);

    return matcher.matches();

  }

  /**
   * Metodo private usato per confrontare l'e-mail inserita dall'utente con il formato richiesto.
   *
   * @param email Stringa contenente l'e-mail inserita dall'utente.
   * @return Valore boolean.
   * <p>Se il valore restituito è true, il formato richiesto è stato rispettato.</p>
   */
  protected static boolean isValidEmail(String email) {
    Pattern pattern;
    Matcher matcher;
    final String Email_Pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    pattern = Pattern.compile(Email_Pattern);
    matcher = pattern.matcher(email);

    return matcher.matches();
  }

  /**
   * Metodo private usato per verificare che l'e-mail
   * inserita dall'utente non sia già presente nel database.
   *
   * @param mail Stringa contenente l'e-mail inserita dall'utente.
   * @return Valore boolean.
   * <p>Se il valore restituito è true, l'e-mail non è presente nel database.
   * Sarà dunque valida per l'utente che l'ha inserita.</p>
   */
  protected boolean confrontaMail(String mail) {
    boolean value = true;
    for (Utente utente : listaUtente) {
      if (utente.getEmail().equals(mail)) {
        value = true;
        break;
      } else {
        value = false;
      }
    }

    return value;
  }

  /**
   * Metodo usato per controllare la validità dell'e-mail inserita dall'utente.
   *
   * @param mail Stringa contenente l'e-mail inserita da editText
   * @return valore boolean.
   * <p>Se il valore di  ristorno è true, il parametro inserito non rispetta le precodizioni.</p>
   */
  protected boolean controlloMail(String mail) {
    if (mail.isEmpty() || mail.length() < 3
            || mail.length() > 63 || !isValidEmail(mail)) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * Metodo usato per controllare la validità della password inserita dall'Utente.
   *
   * @param password Stringa contenente la password inserita da editText
   * @return valore boolean.
   * <p>Se il valore di  ristorno è true, il parametro inserito non rispetta le precodizioni.</p>
   */
  protected boolean controlloPassword(String password) {
    if (password.isEmpty() || password.length() < 8
            || password.length() > 23 || !isValidPassword(password)) {
      return false;
    } else {
      return true;
    }
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
    databaseLogin = FirebaseAuth.getInstance();
  }

  /**
   * <p>Metdo public usato per creare un istanza dell'Utente che.
   * ha effettuato un accesso al database</p>
   */
  public void getUser() {
    user = databaseLogin.getCurrentUser();
  }

  /**
   * Metodo public utilizzato per prelevare l'id dell'utente corrente.
   *
   * @return Stringa contenente l'id.
   */
  public String getUserId() {
    return user.getUid();
  }

  /**
   * Metodo public utilizzato per prelevare il nome dell'utente corrente.
   *
   * @return Stringa contenente il nome.
   */
  public String getUserName() {
    return user.getDisplayName();
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
   * @param idChild Stringa contenente il campo a cui si vuole accedere per effettuare
   *                l'inserimento.
   * @param object  Oggetto che si vuole inserire nel database.
   */
  @Override
  public void addValue(DatabaseReference data, String idChild, Object object) {
    data.child(idChild).setValue(object);
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
