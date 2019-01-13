package com.example.dani.unishare;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Activity usata per registrare un nuovo utente all'interno del database del sistema.</p>
 * <p>Verranno inseriti tramite EditText tutti i dati richiesti nella classe utente.</p>
 *  @see Utente
 *
 *  <p>Verranno gestiti tutti gli eventi conseguenti al "Click" dei bottoni.</p>
 *  <p>All'interno del metodo privato RegistraUtente vengono effettuati tutti
 *  i controlli per l'accettazione dei parametri inseriti dall'Utente (riga 139).</p>
 */
public class RegistrazioneActivity extends Activity implements FirebaseInterface{

  EditText editTextRegNome;
  EditText editTextRegCognome;
  EditText editTextRegEmail;
  EditText editTextRegPassword;
  EditText editTextRegRipetiPassword;
  DatePicker editDatePicker;
  CheckBox checkboxPrivacy;
  RadioButton radioUomo;
  RadioButton radioDonna;
  RadioGroup radioGroupSesso;
  Button buttonRegistrazione;
  Button buttonGiaRegistrato;
  FirebaseAuth firebaseAuth;
  DatabaseReference databaseUtente;
  FirebaseUser user;
  List<Utente> listaUtente;
  String nome;
  String cognome;
  String email;
  String password;
  String ripPassword;
  String date;
  String sesso;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_registrazione);

    editTextRegNome = (EditText) findViewById(R.id.editTextRegNome);
    editTextRegCognome = (EditText) findViewById(R.id.editTextRegCognome);
    editTextRegEmail = (EditText) findViewById(R.id.editTextRegEmail);
    editTextRegPassword = (EditText) findViewById(R.id.editTextRegPassword);
    editTextRegRipetiPassword = (EditText) findViewById(R.id.editTextRegRipetiPassword);
    editDatePicker = (DatePicker) findViewById(R.id.editDatePicker);
    checkboxPrivacy = (CheckBox) findViewById(R.id.checkboxPrivacy);
    radioGroupSesso = (RadioGroup) findViewById(R.id.radioGroupSesso);
    radioDonna = (RadioButton) findViewById(R.id.radioDonna);
    radioUomo = (RadioButton) findViewById(R.id.radioUomo);
    buttonRegistrazione = (Button) findViewById(R.id.buttonRegistrazione);
    buttonGiaRegistrato = (Button) findViewById(R.id.buttonGiaRegistrato);

    listaUtente = new ArrayList<>();
    //databaseUtente = FirebaseDatabase.getInstance().getReference("utente");
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

    istance();

    buttonRegistrazione.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        registraUtente();
      }
    });

    buttonGiaRegistrato.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
      }
    });
  }


  protected void registraUtente() {
    nome = editTextRegNome.getText().toString().trim();
    cognome = editTextRegCognome.getText().toString().trim();
    email = editTextRegEmail.getText().toString().trim();
    password = editTextRegPassword.getText().toString().trim();
    ripPassword = editTextRegRipetiPassword.getText().toString().trim();
    int year = editDatePicker.getYear();
    int month = editDatePicker.getMonth() + 1;
    int day = editDatePicker.getDayOfMonth();

    date = day + "/" + month + "/" + year;
    if (radioDonna.isSelected()) {
      sesso = "D";
    } else {
      sesso = "U";
    }

    if (!checkboxPrivacy.isChecked()) {
      checkboxPrivacy.setError("Le condizioni sulla privacy devono essere accettate");
      checkboxPrivacy.requestFocus();
      return;
    }

    if (controlloParametro(nome)) {
      editTextRegNome.setError("Il nome non può essere vuoto\nMax20Caratteri");
      editTextRegNome.requestFocus();
      return;
    }

    if (controlloParametro(cognome)) {
      editTextRegCognome.setError("Il cognome non può essere vuoto\nMax 20 caratteri");
      editTextRegCognome.requestFocus();
      return;
    }

    if (confrontaMail(email)) {
      editTextRegEmail.setError("L'email è già presente nel sistema");
      editTextRegEmail.requestFocus();
      return;
    }

    if (controlloMail(email)) {
      editTextRegEmail.setError("L'email non può essere vuota\nMax 63 caratteri\nDeve rispettare il formato.");
      editTextRegEmail.requestFocus();
      return;
    }

    if (controlloPassword(password)) {
      editTextRegPassword.setError("La password non può essere vuota\n"
              + "Min 8 caratteri\nMax 20 caratteri");
      editTextRegPassword.requestFocus();
      return;
    }

    if (controlloConfermaPassword(password, ripPassword)) {
      editTextRegRipetiPassword.setError("Le password non coincidono");
      editTextRegRipetiPassword.requestFocus();
      return;
    }

    createUser();

  }


  /**
   * Metodo private usato per confrontare la password inserita dall'utente con il formato richiesto.
   * @param password  Stringa contenente la password inserita dall'utente.
   * @return  Valore boolean.
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

  public void createUser(){
    firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                  Toast.makeText(getApplicationContext(), "Utente Aggiunto",
                          Toast.LENGTH_SHORT).show();
                  getUser();
                  UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest
                          .Builder().setDisplayName(nome).build();
                  user.updateProfile(profileUpdate);
                  String ruolo = "utente";
                  Utente utente = new Utente(getUserId(), nome, cognome,
                          sesso, date, email, password, ruolo);
                  addValue(databaseUtente, getUserId(), utente);
                  startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                  Toast.makeText(getApplicationContext(), "Problema con registrazione",
                          Toast.LENGTH_SHORT).show();
                }
              }
            });
  }

  /**
   * Metodo private usato per confrontare l'e-mail inserita dall'utente con il formato richiesto.
   * @param email  Stringa contenente l'e-mail inserita dall'utente.
   * @return  Valore boolean.
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
   * @param mail  Stringa contenente l'e-mail inserita dall'utente.
   * @return  Valore boolean.
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

  protected boolean controlloParametro(String parametro){
    if(parametro.isEmpty() || parametro.length() > 20){
      return true;
    }
    else{
      return false;
    }
  }

  protected boolean controlloMail(String mail){
    if (mail.isEmpty() || mail.length() < 3
            || mail.length() > 63 || !isValidEmail(mail)){
      return true;
    }
    else{
      return false;
    }
  }

  protected boolean controlloPassword(String password){
    if (password.isEmpty() || password.length() < 8
            || password.length() > 23 || !isValidPassword(password)){
      return true;
    }
    else{
      return false;
    }
  }

  protected boolean controlloConfermaPassword(String password, String conferma){
    if (conferma.isEmpty() || !conferma.equals(password)){
      return true;
    }
    else{
      return false;
    }
  }

  public void istance(){
    firebaseAuth = FirebaseAuth.getInstance();
  }

  public void getUser(){
    user = firebaseAuth.getCurrentUser();
  }

  public String getUserId(){
    return user.getUid();
  }

  public String getUserName(){
    return user.getDisplayName();
  }

  public void logout(){
    FirebaseAuth.getInstance().signOut();
  }

  public DatabaseReference istanceReference(String reference){
    DatabaseReference temp = FirebaseDatabase.getInstance().getReference(reference);
    return temp;
  }

  public DatabaseReference getChild(String reference, String childId){
    DatabaseReference temp = FirebaseDatabase.getInstance().getReference(reference).child(childId);
    return temp;
  }

  public String getIdObject(DatabaseReference data){
    return data.push().getKey();
  }

  @Override
  public void addValue(DatabaseReference data, String idChild, Object object) {
    data.child(idChild).setValue((Utente)object);
  }

  @Override
  public void addValue(DatabaseReference data, Object object) {
    data.setValue(object);
  }

  @Override
  public void deleteValue(DatabaseReference data,String idChild) {
    data.child(idChild).removeValue();
  }

  @Override
  public void deleteValue(DatabaseReference data) {
    data.removeValue();
  }
}