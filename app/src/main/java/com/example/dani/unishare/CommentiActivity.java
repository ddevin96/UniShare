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

public class CommentiActivity extends Activity {

    TextView textViewPostName;
    EditText editTextCommentDescription;
    Button addCommentButton;
    ListView listViewCommenti;
    DatabaseReference databaseCommenti;
    FirebaseAuth databaseId;
    FirebaseUser cUser;
    DatabaseReference databaseAuthor;
    List<Commento> lista;
    String author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commenti);

        textViewPostName = (TextView) findViewById(R.id.textViewPostName);
        editTextCommentDescription = (EditText) findViewById(R.id.editTextCommentDescription);
        addCommentButton = (Button) findViewById(R.id.addCommentButton);
        listViewCommenti = (ListView) findViewById(R.id.listViewCommenti);

        cUser = databaseId.getInstance().getCurrentUser();
        databaseAuthor= FirebaseDatabase.getInstance().getReference("utente").child(cUser.getUid());

        Intent intent = getIntent();

        lista = new ArrayList<>();

        String id = intent.getStringExtra(PostActivity.POST_ID);
        String title = intent.getStringExtra(PostActivity.POST_TITLE);
        textViewPostName.setText(title);

        databaseCommenti = FirebaseDatabase.getInstance().getReference("commento").child(id);
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommento();
            }
        });

        listViewCommenti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Commento commento= lista.get(position);
                modificaCommentoDialog(commento);

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
                for (DataSnapshot personSnapshot: dataSnapshot.getChildren()) {
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

        databaseAuthor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                author= dataSnapshot.child("nome").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addCommento() {
        String description = editTextCommentDescription.getText().toString();
        String idAuthor = cUser.getUid();
        Date date = new Date();

        if (!TextUtils.isEmpty(description)) {
            String id = databaseCommenti.push().getKey();
            Commento comment = new Commento(id, description, author, idAuthor, date);
            databaseCommenti.child(id).setValue(comment);
            Toast.makeText(this, "Commento Inserito", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Inserisci descrizione", Toast.LENGTH_SHORT).show();
    }

    private  void modificaCommentoDialog(Commento commento){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView= inflater.inflate(R.layout.modifica_commento_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextDescriptionCommento;
        final Button modificaCommentoButton;
        final Button cancellaCommentoButton;

        editTextDescriptionCommento = (EditText) dialogView.findViewById(R.id.editTextDescriptionCommento);
        modificaCommentoButton = (Button) dialogView.findViewById(R.id.modificaCommentoButton);
        cancellaCommentoButton = (Button) dialogView.findViewById(R.id.cancellaCommentoButton);
        editTextDescriptionCommento.setText(commento.getDescription());
        final String id = commento.getId();

        dialogBuilder.setTitle("Modifica Commento");
        final AlertDialog alertDialog= dialogBuilder.create();
        alertDialog.show();

        modificaCommentoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = editTextDescriptionCommento.getText().toString();
                Date date = new Date();
                Commento commentoUpdate = new Commento(id,description, cUser.getDisplayName(), cUser.getUid(), date);
                modificaCommento(commentoUpdate);
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

    private void modificaCommento(Commento commento) {
        if (!TextUtils.isEmpty(commento.getDescription())) {
            databaseCommenti.child(commento.getId()).setValue(commento);
            Toast.makeText(this, "Commento Modificato", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Inserisci descrizione", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancellaCommento(String id){
        databaseCommenti.child(id).removeValue();
        Toast.makeText(getApplicationContext(), "Commento Eliminato", Toast.LENGTH_SHORT).show();
    }
}
