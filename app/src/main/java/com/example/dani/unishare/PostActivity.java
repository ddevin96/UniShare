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

public class PostActivity extends Activity {

    public static final String POST_ID="postid";
    public static final String POST_TITLE="posttitle";

    TextView textViewTitolo;
    TextView textViewDescrizione;
    TextView textViewAutore;
    Button addPost;
    DatabaseReference databasePost;
    DatabaseReference databaseUtente;
    ListView listViewPost;
    List<Post> listaPost;
    EditText editTextTitle;
    EditText editTextDescription;
    FirebaseAuth databaseId;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mUser = databaseId.getInstance().getCurrentUser();
        textViewTitolo = (TextView) this.findViewById(R.id.textViewTitolo);
        textViewDescrizione = (TextView) this.findViewById(R.id.textViewDescrizione);
        textViewAutore= (TextView) this.findViewById(R.id.textViewAuthor);
        listViewPost = (ListView) this.findViewById(R.id.listViewPost);
        addPost = (Button) this.findViewById(R.id.addPost);
        addPost.setVisibility(View.GONE);
        listaPost = new ArrayList<>();

        Intent intent = getIntent();
        String title=intent.getStringExtra(MainActivity.BACHECA_TITLE);
        String id=intent.getStringExtra(MainActivity.BACHECA_ID);
        textViewTitolo.setText(title);

        databasePost = FirebaseDatabase.getInstance().getReference("post").child(id);
        databaseUtente = FirebaseDatabase.getInstance().getReference("utente").child(mUser.getUid());

        if (mUser != null)
            addPost.setVisibility(View.VISIBLE);



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

        listViewPost.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = listaPost.get(position);
                if (isCreator(post.getAuthorId()) || isManager())
                    modificaPostDialog(post);
                else
                    Toast.makeText(getApplicationContext(), "Solo l'autore può modificare", Toast.LENGTH_SHORT).show();
                return true;
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

    private void modificaPostDialog(Post post) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView= inflater.inflate(R.layout.modifica_post_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextTitlePost;
        final EditText editTextDescriptionPost;
        final Button modificaPostButton;
        final Button cancellaPostButton;

        editTextTitlePost = (EditText) dialogView.findViewById(R.id.editTextTitlePost);
        editTextDescriptionPost = (EditText) dialogView.findViewById(R.id.editTextDescriptionPost);
        modificaPostButton = (Button) dialogView.findViewById(R.id.modificaPostButton);
        cancellaPostButton = (Button) dialogView.findViewById(R.id.cancellaPostButton);
        editTextTitlePost.setText(post.getTitle());
        editTextDescriptionPost.setText(post.getDescription());
        final String id = post.getId();

        dialogBuilder.setTitle("Modifica Post");
        final AlertDialog alertDialog= dialogBuilder.create();
        alertDialog.show();

        modificaPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitlePost.getText().toString();
                String description = editTextDescriptionPost.getText().toString();
                if(TextUtils.isEmpty(title) || title.length()> 65534 || confrontaPost(title)){
                    editTextTitlePost.setError("Il titolo non può essere vuoto.\n Deve avere un massimo di 65534 caratteri.");
                    editTextTitlePost.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(description) || description.length()>65534){
                    editTextDescriptionPost.setError("La descrizione non può essere vuota.\n Deve avere un massimo di 65534 caratteri.");
                    editTextDescriptionPost.requestFocus();
                    return;
                }
                Date date = new Date();
                Post postUpdate = new Post(id, title, description, mUser.getDisplayName(), mUser.getUid(), date);
                databasePost.child(postUpdate.getId()).setValue(postUpdate);
                Toast.makeText(getApplicationContext(), "Post Modificato", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        cancellaPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost(id);
                alertDialog.dismiss();
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
        //titolo dialog
        dialogBuilder.setTitle("post");
        final AlertDialog alertDialog= dialogBuilder.create();
        alertDialog.show();

        pubblica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();

                if(TextUtils.isEmpty(title) || title.length()> 65534 || confrontaPost(title)){
                    editTextTitle.setError("Il titolo non può essere vuoto.\n Deve avere un massimo di 65534 caratteri.");
                    editTextTitle.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(description) || description.length()>65534){
                    editTextDescription.setError("La descrizione non può essere vuota.\n Deve avere un massimo di 65534 caratteri.");
                    editTextDescription.requestFocus();
                    return;
                }
                Date data = new Date();
                String author = mUser.getDisplayName();
                String idAuthor = mUser.getUid();
                String id = databasePost.push().getKey();
                Post post= new Post(id, title, description, author, idAuthor, data);
                databasePost.child(post.getId()).setValue(post);
                Toast.makeText(getApplicationContext(), "Post aggiunto", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();

            }
        });
    }


    private void deletePost(String id) {
        databasePost.child(id).removeValue();
        DatabaseReference postCommenti = FirebaseDatabase.getInstance().getReference("commento").child(id);
        postCommenti.removeValue();
        Toast.makeText(getApplicationContext(), "Post Eliminato", Toast.LENGTH_SHORT).show();
    }

    private boolean confrontaPost(String titolo){
        boolean value=true;
        if (!listaPost.isEmpty()) {
            for (Post post : listaPost) {
                if (post.getTitle().equals(titolo)) {
                    value = true;
                    break;
                } else {
                    value = false;
                }
            }
        } else
            return false;
        return value;
    }

    private boolean isManager() {
        if (databaseUtente.child("ruolo").equals("manager"))
            return true;
        else
            return false;
    }

    private boolean isCreator(String id) {
        if (databaseUtente.child("authorId").equals(id))
            return true;
        else
            return false;
    }
}

