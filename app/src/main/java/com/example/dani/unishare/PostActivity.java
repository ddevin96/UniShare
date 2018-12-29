package com.example.dani.unishare;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostActivity extends Activity {

    public static final String BACHECA_ID="bachecaid";
    public static final String POST_ID="postid";
    public static final String POST_TITLE="posttitle";

    TextView textViewTitolo;
    TextView textViewDescrizione;
    TextView textViewAutore;
    Button addPost;
    DatabaseReference databasePost;
    ListView listViewPost;
    List<Post> listaPost;
    EditText editTextTitle;
    EditText editTextDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        textViewTitolo = (TextView) this.findViewById(R.id.textViewTitolo);
        textViewDescrizione = (TextView) this.findViewById(R.id.textViewDescrizione);
        textViewAutore= (TextView) this.findViewById(R.id.textViewAuthor);
        listViewPost = (ListView) this.findViewById(R.id.listViewPost);
        addPost = (Button) this.findViewById(R.id.addPost);
        listaPost = new ArrayList<>();

        final Intent intent = getIntent();
        String title=intent.getStringExtra(MainActivity.BACHECA_TITLE);
        final String id=intent.getStringExtra(MainActivity.BACHECA_ID);
        textViewTitolo.setText(title);

        databasePost = FirebaseDatabase.getInstance().getReference("post").child(id);

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
                startActivity(intent);

            }
        });

    }

    protected void onStart() {
        super.onStart();
        databasePost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaPost.clear();
                for (DataSnapshot personSnapshot: dataSnapshot.getChildren()) {
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

    @SuppressLint("WrongViewCast")
    private void showCreateDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView= inflater.inflate(R.layout.activity_add_post_form, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextTitle;
        final EditText editTextDescription;
        final Button pubblica;

        editTextTitle = (EditText) dialogView.findViewById(R.id.titlePost);
        editTextDescription = (EditText) dialogView.findViewById(R.id.descrizionePost);
        pubblica = (Button) dialogView.findViewById(R.id.addPostButton);

        dialogBuilder.setTitle("post");
        final AlertDialog alertDialog= dialogBuilder.create();
        alertDialog.show();



        pubblica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();
                String author = "";
                Date data = new Date();
                String id = databasePost.push().getKey();
                Post post= new Post(id, title, description, author, data);
                addPost(post);
                alertDialog.dismiss();
            }
        });

    }
    //ciao
    public void addPost(Post post) {
        if (!TextUtils.isEmpty(post.getTitle())&&!TextUtils.isEmpty(post.getDescription())) {
            databasePost.child(post.getId()).setValue(post);
            Toast.makeText(this, "Post aggiunto", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Inserisci titolo e descrizione", Toast.LENGTH_SHORT).show();



    }
}

