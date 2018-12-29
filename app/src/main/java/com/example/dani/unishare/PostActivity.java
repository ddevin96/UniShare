package com.example.dani.unishare;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends Activity {

    public static final String POST_ID="postid";
    public static final String POST_TITLE="posttitle";

    TextView textViewTitolo;
    TextView textViewDescrizione;
    TextView textViewAutore;
    Button addPost;
    DatabaseReference databasePost;
    ListView listViewPost;
    List<Post> listaPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        databasePost = FirebaseDatabase.getInstance().getReference("post");

        textViewTitolo = (TextView) this.findViewById(R.id.textViewTitolo);
        textViewDescrizione = (TextView) this.findViewById(R.id.textViewDescrizione);
        textViewAutore= (TextView) this.findViewById(R.id.textViewAuthor);
        listViewPost = (ListView) this.findViewById(R.id.listViewPost);
        addPost = (Button) this.findViewById(R.id.addPost);
        listaPost = new ArrayList<>();

        addPost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPostFormActivity.class);
                startActivity(intent);
            }
        });

        listViewPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = listaPost.get(position);
                Intent intent = new Intent(getApplicationContext(), CommentiActivity.class);
                intent.putExtra(POST_ID, post.getId());
                intent.putExtra(POST_TITLE, post.getId());
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
}

