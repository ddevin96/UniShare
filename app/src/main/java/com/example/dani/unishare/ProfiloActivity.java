package com.example.dani.unishare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 */
public class ProfiloActivity extends Activity implements FirebaseInterface {

  TextView textViewNome;
  TextView textViewCognome;
  TextView textViewEmail;
  TextView textViewSesso;
  TextView textViewData;
  Button modificaProfila;
  Button cancellaProfilo;
  DatabaseReference databesaProfilo;
  DatabaseReference databaseUtente;
  FirebaseAuth databaseId;
  FirebaseUser user;
  String nomeEdit;
  String cognomeEdit;
  String emailEdit;
  String sessoEdit;
  String passwordEdit;
  String ruolo;
  String data;
  List<Utente> listaUtente;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profilo);

    textViewNome = (TextView) findViewById(R.id.textViewNomeUser);
    textViewCognome = (TextView) findViewById(R.id.textViewCognomeUtente);
    textViewEmail = (TextView) findViewById(R.id.textViewEmailUtente);
    textViewSesso = (TextView) findViewById(R.id.textViewSessoUtente);
    textViewData = (TextView) findViewById(R.id.textViewDataUtente);
    modificaProfila = (Button) findViewById(R.id.modificaProfiloButton);
    cancellaProfilo = (Button) findViewById(R.id.cancellaProfiloButton);

    //user = databaseId.getInstance().getCurrentUser();
      istance();
      getUser();
    databesaProfilo = FirebaseDatabase.getInstance().getReference("utente").child(getUserId());

    listaUtente = new ArrayList<>();
    databaseUtente = FirebaseDatabase.getInstance().getReference("utente");
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

    modificaProfila.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        modificaProfiloDialog();

      }
    });

    cancellaProfilo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        deleteProfilo();
      }
    });
  }

  @Override
  protected void onStart() {
    super.onStart();
    databesaProfilo.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        nomeEdit = dataSnapshot.child("nome").getValue(String.class);
        cognomeEdit = dataSnapshot.child("cognome").getValue(String.class);
        emailEdit = dataSnapshot.child("email").getValue(String.class);
        sessoEdit = dataSnapshot.child("sesso").getValue(String.class);
        passwordEdit = dataSnapshot.child("password").getValue(String.class);
        data = dataSnapshot.child("dataDiNascita").getValue(String.class);
        ruolo = dataSnapshot.child("ruolo").getValue(String.class);
        textViewNome.setText(nomeEdit);
        textViewCognome.setText(cognomeEdit);
        textViewEmail.setText(emailEdit);
        textViewSesso.setText(sessoEdit);
        textViewData.setText(data);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  /**
   * <p>Activity del Dialog usato per modificare il profilo di un utente.</p>
   * <p>I campi richiesti sono quelli che compongono il costruttore della classe Utente</p>
   * @see Utente
   * <p>I dati vengono reinseriti tramite EditText dedicate.</p>
   * <p>Viene gestita l'evento legato al "Click" del bottone conferma.</p>
   * <p>Vengono implementate tutte le condizioni secondo le quali i parametri
   * inseriti verranno dichiarati idonei o non idonei.</p>
   */
  private void modificaProfiloDialog() {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
    LayoutInflater inflater = getLayoutInflater();
    final View dialogView = inflater.inflate(R.layout.modifica_profilo_dialog, null);
    dialogBuilder.setView(dialogView);

    final EditText editTextNome;
    final EditText editTextCognome;
    final EditText editTextEmail;
    final EditText editTextPassword;
    final DatePicker dataPicker;
    final Button conferma;
    final RadioButton radioButtonUomo;

    editTextNome = (EditText) dialogView.findViewById(R.id.editTextModificaNome);
    editTextCognome = (EditText) dialogView.findViewById(R.id.editTextModificaCognome);
    editTextEmail = (EditText) dialogView.findViewById(R.id.editTextModificaEmail);
    editTextPassword = (EditText) dialogView.findViewById(R.id.editTextModificaPassword);
    dataPicker = (DatePicker) dialogView.findViewById(R.id.editDatePicker);
    radioButtonUomo = (RadioButton) dialogView.findViewById(R.id.radioModificaUomo1);
    conferma = (Button) dialogView.findViewById(R.id.ButtonModifica);
    editTextNome.setText(nomeEdit);
    editTextCognome.setText(cognomeEdit);
    editTextEmail.setText(emailEdit);
    editTextPassword.setText(passwordEdit);

    aggiornaData(dataPicker);
    dialogBuilder.setTitle("Modifica profilo");
    final AlertDialog alertDialog = dialogBuilder.create();
    alertDialog.show();

    conferma.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
          //changed
        String id = getUserId();
        String nome = editTextNome.getText().toString();
        String sesso;
        if (radioButtonUomo.isSelected()) {
          sesso = "U";
        } else {
          sesso = "D";
        }

        if (controllaParametri(nome)) {
          editTextNome.setError("Il campo Nome non può essere vuoto.\n "
                  + "Deve avere al massimo 20 caratteri");
          editTextNome.requestFocus();
          return;
        }

        String cognome = editTextCognome.getText().toString();

        if (controllaParametri(cognome)) {
          editTextCognome.setError("Il campo Cognome non può essere vuoto.\n"
                  + " Deve avere al massimo 20 caratteri");
          editTextCognome.requestFocus();
          return;
        }

        String email = editTextEmail.getText().toString();

        if (confrontaMail(email, id)) {
          editTextEmail.setError("L'email è già presente nel sistema");
          editTextEmail.requestFocus();
          return;
        }

        if (controllaMail(email)) {
          editTextEmail.setError("il campo E-mail non può essere vuoto.\n "
                  + "min:3 max:63 caratteri.\nL'E-mail deve rispettare il formato.");
          editTextEmail.requestFocus();
          return;
        }

        String password = editTextPassword.getText().toString();

        if (controllaPassword(password)) {
          editTextPassword.setError("Il campo password non può essere vuoto."
                  + " \n Deve essere compposto dal almeno 8 caratteri e massimo 20. "
                  + "\n La password deve rispettare il formato.");
          editTextPassword.requestFocus();
          return;
        }

        int year = dataPicker.getYear();
        int month = dataPicker.getMonth() + 1;
        int day = dataPicker.getDayOfMonth();
        String date = day + "/" + month + "/" + year;

        user.updateEmail(email);
        user.updatePassword(password);
        Utente utente = new Utente(id, nome, cognome, sesso, date, email, password, ruolo);
        databesaProfilo.setValue(utente);
        Toast.makeText(getApplicationContext(), "La modifica ha avuto successo",
                Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();

        finish();
        startActivity(new Intent(getApplicationContext(), ProfiloActivity.class));

      }
    });
  }

  /**
   * <p>Metodo privato usato per eliminare ogni riferimento dal database
   * del profilo che si desidera eliminare.</p>
   */
  private void deleteProfilo() {
    logout();
    user.delete();
    databesaProfilo.removeValue();
    Toast.makeText(this, "Il profilo è stato cancellato", Toast.LENGTH_SHORT).show();
    startActivity(new Intent(getApplicationContext(), MainActivity.class));
  }

  /**
   * Metodo private usato per verificare che l'e-mail
   * inserita dall'utente non sia già presente nel database.
   * @param mail  Stringa contenente l'e-mail inserita dall'utente.
   * @return  Valore boolean.
   * <p>Se il valore restituito è true, l'e-mail non è presente nel database.
   * Sarà dunque valida per l'utente che l'ha inserita.</p>
   */
  protected boolean confrontaMail(String mail, String id) {
    boolean value = true;
    for (Utente utente : listaUtente) {
      if (utente.getEmail().equals(mail) && !utente.getId().equals(id)) {
        value = true;
        break;
      } else {
        value = false;
      }
    }

    return value;
  }

  protected boolean controllaParametri(String parametro){
    if (parametro.isEmpty() || parametro.length() > 20){
      return true;
    }
    else {
      return false;
    }
  }

  protected boolean controllaMail(String mail){
    if (mail.isEmpty()
            || mail.length() < 3
            || mail.length() > 63 || !isValidEmail(mail)){
      return true;
    }
    else {
      return false;
    }
  }

  protected boolean controllaPassword(String password){
    if (password.isEmpty()
            || password.length() < 8
            || password.length() > 20
            || !isValidPassword(password)){
      return true;
    }
    else {
      return false;
    }
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
   * Metodo privato usato per modificare la data di nascita selezionata in precedenza
   * alla modifica del profilo.
   * @param data1 parametro di tipo DatePicker in cui viene selezionata la data
   *             desiderata dall'utente.
   */
  protected void aggiornaData(DatePicker data1) {
    int year = 0;
    int month = 0;
    int day = 0;
    int count = 0;
    String temp = "";
    for (int i = 0; i < data.length(); i++) {
      if (!(data.charAt(i) == '/')) {
        temp += data.charAt(i);
      } else if (count == 0) {
        day = Integer.parseInt(temp);
        count++;
        temp = "";
      } else if (count == 1) {
        month = Integer.parseInt(temp);
        count++;
        temp = "";
      } else if (count == 2) {
        year = Integer.parseInt(temp);
        break;
      }
    }
    data1.updateDate(year, month + 1, day);
  }

  public void istance(){
      databaseId = FirebaseAuth.getInstance();
  }

  public void getUser(){
      user = databaseId.getCurrentUser();
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
}
