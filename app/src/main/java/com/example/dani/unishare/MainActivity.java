package com.example.dani.unishare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class MainActivity extends Activity {

    public static final String BACHECA_ID = "bachecaid";
    public static final String BACHECA_TITLE = "bachecatitle";


    EditText editTextTitle;
    EditText editTextDescription;
    EditText editTextAuthor;
    Button addButton;
    Button signOutButton;
    DatabaseReference databaseBacheca;
    ListView listViewBacheca;
    List<Bacheca> listaBacheca;

    Button signUpButton;
    Button loginButton;
    Button profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseBacheca = FirebaseDatabase.getInstance().getReference("bacheca");

        editTextTitle = (EditText) this.findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) this.findViewById(R.id.editTextDescription);
        editTextAuthor = (EditText) this.findViewById(R.id.editTextAuthor);
        listViewBacheca = (ListView) this.findViewById(R.id.listViewBacheca);
        addButton = (Button) this.findViewById(R.id.addBachecaButton);

        signUpButton = (Button) this.findViewById(R.id.signUpButton);
        loginButton = (Button) this.findViewById(R.id.loginButton);
        profileButton = (Button) this.findViewById(R.id.profileButton);
        signOutButton = (Button) this.findViewById(R.id.signOutButton);

        listaBacheca = new ArrayList<>();

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showCreaBachecaDialog();
            }
        });

        listViewBacheca.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bacheca bacheca = listaBacheca.get(position);
                Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                intent.putExtra(BACHECA_ID, bacheca.getId());
                intent.putExtra(BACHECA_TITLE, bacheca.getTitle());
                startActivity(intent);

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrazioneActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfiloActivity.class);
                startActivity(intent);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
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
                for (DataSnapshot personSnapshot: dataSnapshot.getChildren()) {
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

    private void showCreaBachecaDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_bacheca_dialog,null);
        dialogBuilder.setView(dialogView);

        final EditText editTextTitle = (EditText) dialogView.findViewById(R.id.editTextTitle);
        final EditText editTextDescription = (EditText) dialogView.findViewById(R.id.editTextDescription);
        final EditText editTextAuthor = (EditText) dialogView.findViewById(R.id.editTextAuthor);
        final Button addBachecaButton = (Button) dialogView.findViewById(R.id.addButton);

        dialogBuilder.setTitle("Crea bacheca");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        if(TextUtils.isEmpty(editTextTitle.getText())||editTextTitle.getText().length()>20 || confrontaBacheche(editTextTitle.getText().toString())){
            editTextTitle.setError("Il titolo non può essere vuoto.\n Deve avere un massimo di 20 caratteri.\n Non possono esistere due Bacheche di uno stesso paese.");
            editTextTitle.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(editTextDescription.getText()) || editTextDescription.getText().length()>200){
            editTextDescription.setError("La descrizione non può essere vuota.\n Deve avere un massimo di 200 caratteri.");
            editTextDescription.requestFocus();
            return;
        }

        addBachecaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();
                String author = editTextAuthor.getText().toString();
                Date data = new Date();
                String id = databaseBacheca.push().getKey();
                Bacheca bacheca = new Bacheca(id, title, description, author, data);
                addBacheca(bacheca);
                alertDialog.dismiss();
            }
        });

    }

    public void addBacheca(Bacheca bacheca) {

        if (!TextUtils.isEmpty(bacheca.getTitle())&&!TextUtils.isEmpty(bacheca.getDescription())) {
            databaseBacheca.child(bacheca.getId()).setValue(bacheca);
            Toast.makeText(this, "Bacheca aggiunta", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Inserisci titolo e descrizione", Toast.LENGTH_SHORT).show();



    }

    private boolean confrontaBacheche(String titolo){
        for (Bacheca bacheca : listaBacheca){
            if(bacheca.getTitle().equals(titolo)){
                return true;
            }
            else{
                return false;
            }
        }
    }
}
