package com.example.dani.unishare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
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

public class AddPostFormActivity extends Activity {

    final static String BACHECA_ID="bachecaid";


    DatabaseReference databasePost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_form);

        final Intent intent = getIntent();
        final String id = intent.getStringExtra(PostActivity.BACHECA_ID);

        databasePost = FirebaseDatabase.getInstance().getReference("post").child(id);




        pubblica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addPost();
                Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                intent.putExtra(BACHECA_ID, id);
                startActivity(intent);
            }
        });
    }


    public void addPost() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String author = "";
        Date data = new Date();

        if (!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(description)) {
            String id = databasePost.push().getKey();
            Post post= new Post(id, title, description, author, data);
            databasePost.child(id).setValue(post);
            Toast.makeText(this, "Post aggiunto", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Inserisci titolo e descrizione", Toast.LENGTH_SHORT).show();



    }
}
