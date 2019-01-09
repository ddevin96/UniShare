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

public class ProfiloActivity extends Activity {

    TextView textViewNome;
    TextView textViewCognome;
    TextView textViewEmail;
    TextView textViewSesso;
    TextView textViewData;
    Button modificaProfila;
    Button cancellaProfilo;
    DatabaseReference databesaProfilo;
    DatabaseReference databaseBacheche;
    DatabaseReference databasePost;
    DatabaseReference databaseCommento;
    FirebaseAuth databaseId;
    FirebaseUser user;
    String nomeEdit, cognomeEdit, emailEdit, sessoEdit, passwordEdit, ruolo;
    Long year, month, day;
    List<Bacheca> listaBacheche;
    List<Post> listaPost;
    List<Commento> listaCommenti;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        textViewNome= (TextView) findViewById(R.id.textViewNomeUser);
        textViewCognome= (TextView) findViewById(R.id.textViewCognomeUtente);
        textViewEmail= (TextView) findViewById(R.id.textViewEmailUtente);
        textViewSesso= (TextView) findViewById(R.id.textViewSessoUtente);
        textViewData= (TextView) findViewById(R.id.textViewDataUtente);
        modificaProfila= (Button) findViewById(R.id.modificaProfiloButton);
        cancellaProfilo= (Button) findViewById(R.id.cancellaProfiloButton);

        user = databaseId.getInstance().getCurrentUser();
        databesaProfilo= FirebaseDatabase.getInstance().getReference("utente").child(user.getUid());
        databaseBacheche= FirebaseDatabase.getInstance().getReference("bacheca");
        databasePost= FirebaseDatabase.getInstance().getReference("post");
        databaseCommento= FirebaseDatabase.getInstance().getReference("commento");

        listaBacheche = new ArrayList<>();
        listaPost= new ArrayList<>();
        listaCommenti= new ArrayList<>();


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
                //year = dataSnapshot.child("dataDiNascita").child("year").getValue(Long.class);
                //month = dataSnapshot.child("dataDiNascita").child("month").getValue(Long.class);
                //day = dataSnapshot.child("dataDiNascita").child("day").getValue(Long.class);
                String data= dataSnapshot.child("datadiNascita").getValue(String.class);
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
        databaseBacheche.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot bachecaSnapshot : dataSnapshot.getChildren()){
                    Bacheca bacheca = bachecaSnapshot.getValue(Bacheca.class);
                    listaBacheche.add(bacheca);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       databasePost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Post post = postSnapshot.getValue(Post.class);
                    listaPost.add(post);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

            databaseCommento.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot commentoSnapshot : dataSnapshot.getChildren()){
                    Commento commento = commentoSnapshot.getValue(Commento.class);
                    listaCommenti.add(commento);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void modificaProfiloDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView= inflater.inflate(R.layout.modifica_profilo_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextNome;
        final EditText editTextCognome;
        final EditText editTextEmail;
        final EditText editTextPassword;
        final DatePicker data;
        final Button conferma;
        final RadioButton radioButtonUomo;

        editTextNome= (EditText) dialogView.findViewById(R.id.editTextModificaNome);
        editTextCognome =(EditText) dialogView.findViewById(R.id.editTextModificaCognome);
        editTextEmail = (EditText) dialogView.findViewById(R.id.editTextModificaEmail);
        editTextPassword = (EditText) dialogView.findViewById(R.id.editTextModificaPassword);
        data = (DatePicker) dialogView.findViewById(R.id.editDatePicker);
        radioButtonUomo = (RadioButton) dialogView.findViewById(R.id.radioModificaUomo1);
        conferma = (Button) dialogView.findViewById(R.id.ButtonModifica);
        editTextNome.setText(nomeEdit);
        editTextCognome.setText(cognomeEdit);
        editTextEmail.setText(emailEdit);
        editTextPassword.setText(passwordEdit);

        int yearUpdate =(int) year.longValue();
        int monthUpdate = (int) month.longValue()+1;
        int dayUpdate = (int) day.longValue();
        data.updateDate(yearUpdate,monthUpdate,dayUpdate);

        dialogBuilder.setTitle("Modifica profilo");
        final AlertDialog alertDialog= dialogBuilder.create();
        alertDialog.show();


        conferma.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String nome = editTextNome.getText().toString();
                String cognome = editTextCognome.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                int year = data.getYear();
                int month = data.getMonth();
                int day = data.getDayOfMonth();


                String sesso;
                //radiobutton
                if (radioButtonUomo.isSelected())
                    sesso = "M";
                else
                    sesso = "F";

                if(TextUtils.isEmpty(editTextNome.getText()) || editTextNome.getText().length()>20){
                    editTextNome.setError("Il campo Nome non può essere vuoto.\n Deve avere al massimo 20 caratteri");
                    editTextNome.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(editTextCognome.getText()) || editTextCognome.getText().length()>20){
                    editTextCognome.setError("Il campo Cognome non può essere vuoto.\n Deve avere al massimo 20 caratteri");
                    editTextCognome.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(editTextEmail.getText()) || editTextEmail.getText().length()<3 || editTextEmail.getText().length()>63 || !isValidEmail(email)){
                    editTextEmail.setError("il campo E-mail non può essere vuoto.\n min:3 max:63 caratteri.\nL'E-mail deve rispettare il formato.");
                    editTextEmail.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(editTextPassword.getText()) || editTextPassword.getText().length()<8 || editTextPassword.getText().length()>20 || !isValidPassword(editTextPassword.getText().toString())){
                    editTextPassword.setError("Il campo password non può essere vuoto. \n Deve essere compposto dal almeno 8 caratteri e massimo 20. \n La password deve rispettare il formato.");
                    editTextPassword.requestFocus();
                    return;
                }

                    String date = day + "/" + month +"/" + year;
                    String id = user.getUid();
                    user.updateEmail(email);
                    user.updatePassword(password);
                    Utente utente = new Utente(id, nome, cognome, sesso, date, email, password, ruolo);
                    databesaProfilo.setValue(utente);
                    Toast.makeText(getApplicationContext(), "La modifica ha avuto successo", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();

                    finish();
                    startActivity(new Intent(getApplicationContext(), ProfiloActivity.class));

            }
        });
    }



    private void deleteProfilo() {
        FirebaseAuth.getInstance().signOut();
        user.delete();
        databesaProfilo.removeValue();
        Toast.makeText(this,"Il profilo è stato cancellato" , Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    private static boolean isValidPassword(String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.[a-z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    private static boolean isValidEmail(String email){
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        return matcher.matches();
    }

}
