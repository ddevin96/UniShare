package com.example.dani.unishare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
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
    List<Commento> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commenti);

        textViewPostName = (TextView) findViewById(R.id.textViewPostName);
        editTextCommentDescription = (EditText) findViewById(R.id.editTextCommentDescription);
        addCommentButton = (Button) findViewById(R.id.addCommentButton);
        listViewCommenti = (ListView) findViewById(R.id.listViewCommenti);

        cUser = databaseId.getInstance().getCurrentUser();

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
    }

    private void addCommento() {
        String description = editTextCommentDescription.getText().toString();
        String author = cUser.getDisplayName();
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
}
