package com.example.dani.unishare;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfiloActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        TextView textViewNome;
        TextView textViewCognome;
        TextView textViewEmail;
        TextView textViewSesso;
        TextView textViewData;
        Button modificaProfila;
        Button cancellaProfilo;
        DatabaseReference databesaProfilo;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        textViewNome= (TextView) findViewById(R.id.textViewNomeUser);
        textViewCognome= (TextView) findViewById(R.id.textViewCognomeUtente);
        textViewEmail= (TextView) findViewById(R.id.textViewEmailUtente);
        textViewSesso= (TextView) findViewById(R.id.textViewSessoUtente);
        textViewData= (TextView) findViewById(R.id.textViewDataUtente);
        modificaProfila= (Button) findViewById(R.id.modificaProfiloButton);
        cancellaProfilo= (Button) findViewById(R.id.cancellaProfiloButton);

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        databesaProfilo= FirebaseDatabase.getInstance().getReference("utente").child(user.getUid());
        final Utente utente = new Utente();

        databesaProfilo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot data= (DataSnapshot) dataSnapshot.getChildren();
                utente =data.getValue(Utente.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
