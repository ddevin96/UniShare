package com.example.dani.unishare;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {

    EditText editTextTitle;
    EditText editTextDescription;
    EditText editTextAuthor;
    Button addButton;
    DatabaseReference databaseBacheca;
    ListView listViewBacheca;
    List<Bacheca> listaBacheca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseBacheca = FirebaseDatabase.getInstance().getReference("bacheca");

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextAuthor = (EditText) findViewById(R.id.editTextAuthor);
        listViewBacheca = (ListView) findViewById(R.id.listViewBacheca);
        listaBacheca = new ArrayList<>();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBacheca();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseBacheca.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaBacheca.clear();
                for (DataSnapshot personSnapshot: dataSnapshot.getChildren()) {
                    Bacheca bacheca = personSnapshot.getValue(Bacheca.class);
                    listaBacheca.add(bacheca);
                }

                BachecaList adapter = new BachecaList(MainActivity.this, listaBacheca);
                listViewBacheca.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addBacheca() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String author = editTextAuthor.getText().toString();
        Date data = new Date();

        if (!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(description)) {
            String id = databaseBacheca.push().getKey();
            Bacheca bacheca = new Bacheca(id, title, description, author, data);
            databaseBacheca.child(id).setValue(bacheca);
            Toast.makeText(this, "Bacheca aggiunta", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Inserisci titolo e descrizione", Toast.LENGTH_SHORT).show();



    }
}
