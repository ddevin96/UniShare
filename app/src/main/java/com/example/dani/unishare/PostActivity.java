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
    public static final String POST_DESCRIZIONE="postdescrizione";
    public static final String POST_AUTORE="postautore";

    EditText searchbarPost;
    Button searchButton;
    TextView textViewTitolo;
    TextView textViewDescrizione;
    Button addPost;
    DatabaseReference databasePost;
    DatabaseReference databaseUtente;
    ListView listViewPost;
    List<Post> listaPost;
    EditText editTextTitle;
    EditText editTextDescription;
    FirebaseAuth databaseId;
    FirebaseUser mUser;
    String ruoloUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mUser = databaseId.getInstance().getCurrentUser();
        searchbarPost= (EditText) this.findViewById(R.id.searchbarPost);
        searchButton= (Button) this.findViewById(R.id.searchButton);
        textViewTitolo = (TextView) this.findViewById(R.id.textViewTitolo);
        textViewDescrizione = (TextView) this.findViewById(R.id.textViewDescrizione);
        listViewPost = (ListView) this.findViewById(R.id.listViewPost);
        addPost = (Button) this.findViewById(R.id.addPost);
        addPost.setVisibility(View.GONE);
        listaPost = new ArrayList<>();

        Intent intent = getIntent();
        String id=intent.getStringExtra(MainActivity.BACHECA_ID);
        String title=intent.getStringExtra(MainActivity.BACHECA_TITLE);
        String description=intent.getStringExtra(MainActivity.BACHECA_DESCRIPTION);
        textViewTitolo.setText(title);
        textViewDescrizione.setText(description);

        databasePost = FirebaseDatabase.getInstance().getReference("post").child(id);

        if (mUser != null) {
            addPost.setVisibility(View.VISIBLE);
            databaseUtente = FirebaseDatabase.getInstance().getReference("utente").child(mUser.getUid());
            databaseUtente.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ruoloUser = dataSnapshot.child("ruolo").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Post> listaPostTrovati = new ArrayList<>();
                List<String> listaParole = trovaParole(searchbarPost.getText().toString());

                for(int k=0; k<listaPost.size();k++){
                    Post post = listaPost.get(k);
                    for(int j=0; j<listaParole.size(); j++) {
                        String elem = listaParole.get(j);
                        if(post.getTitle().contains(elem)) {
                            listaPostTrovati.add(post);
                            break;
                        }
                    }

                }
                /*for(int j=0; j<listaParole.size(); j++){
                    String elem = listaParole.get(j);
                    for(int k=0; k<listaPost.size();k++){
                        Post post = listaPost.get(k);
                        if(post.getTitle().contains(elem)){
                            if(!listaPostTrovati.isEmpty()){
                                for (int i=0; i<listaPostTrovati.size();i++) {
                                    Post post1 = listaPostTrovati.get(i);
                                    if(!post.getId().equals(post1.getId())) {
                                        listaPostTrovati.add(post);
                                        break;
                                    }
                                }
                            }
                            else
                                listaPostTrovati.add(post);
                        }
                    }
                }*/
                PostList adapter1 = new PostList(PostActivity.this, listaPostTrovati);
                listViewPost.setAdapter(adapter1);
            }
        });

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
                intent.putExtra(POST_DESCRIZIONE, post.getDescription());
                intent.putExtra(POST_AUTORE, post.getAuthor());
                startActivity(intent);

            }
        });

        listViewPost.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = listaPost.get(position);

                if(mUser == null || !isCreator(post.getAuthorId()))
                    Toast.makeText(getApplicationContext(), "Non sei autorizzato a modificare", Toast.LENGTH_SHORT).show();
                else if (isCreator(post.getAuthorId()) || isManager())
                    modificaPostDialog(post);
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
        if (ruoloUser.equals("manager"))
            return true;
        else
            return false;
    }

    private boolean isCreator(String id) {
        if (mUser.getUid().equals(id))
            return true;
        else
            return false;
    }

    private List<String> trovaParole(String stringa){
        String parola= "";
        List<String> listaParole= new ArrayList<>();
        for(int i=0; i<stringa.length(); i++){
            if(stringa.charAt(i) > 'a' && stringa.charAt(i) < 'z' || stringa.charAt(i) > 'A' && stringa.charAt(i) < 'Z' || stringa.charAt(i) > '0' && stringa.charAt(i) < '9' || stringa.charAt(i) != ' ')
                parola += stringa.charAt(i);
            else{
                listaParole.add(parola);
                parola ="";
            }
        }
        listaParole.add(parola);
        return listaParole;
    }

}

