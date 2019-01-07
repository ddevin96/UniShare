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
    public static final String BACHECA_DESCRIPTION = "bachecadescription";


    EditText editTextTitle;
    EditText editTextDescription;
    EditText editTextAuthor;
    Button addButton;
    Button signOutButton;
    DatabaseReference databaseBacheca;
    DatabaseReference databaseUtente;
    DatabaseReference databasePost;
    FirebaseUser bUser;
    FirebaseAuth DatabaseId;
    ListView listViewBacheca;
    List<Bacheca> listaBacheca;
    List<Post> listaPost;
    String ruoloManager;

    Button signUpButton;
    Button loginButton;
    Button profileButton;
    Button managerButton;
    Button cercaProfiloButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseBacheca = FirebaseDatabase.getInstance().getReference("bacheca");

        editTextTitle = (EditText) this.findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) this.findViewById(R.id.editTextDescription);
        listViewBacheca = (ListView) this.findViewById(R.id.listViewBacheca);
        addButton = (Button) this.findViewById(R.id.addBachecaButton);
        addButton.setVisibility(View.GONE);

        signUpButton = (Button) this.findViewById(R.id.signUpButton);
        loginButton = (Button) this.findViewById(R.id.loginButton);
        profileButton = (Button) this.findViewById(R.id.profileButton);
        signOutButton = (Button) this.findViewById(R.id.signOutButton);
        managerButton = (Button) this.findViewById(R.id.ManagerButton);
        cercaProfiloButton = (Button) this.findViewById(R.id.cercaProfiloButton);

        listaBacheca = new ArrayList<>();
        listaPost = new ArrayList<>();

        bUser= DatabaseId.getInstance().getCurrentUser();

        if(bUser!=null) {
            addButton.setVisibility(View.VISIBLE);
            databaseUtente = FirebaseDatabase.getInstance().getReference("utente").child(bUser.getUid());
            databaseUtente.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ruoloManager = dataSnapshot.child("ruolo").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bUser==null || !isManager())
                    Toast.makeText(getApplicationContext(), "Solo il manager può creare una bacheca", Toast.LENGTH_SHORT).show();
                else
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
                intent.putExtra(BACHECA_DESCRIPTION, bacheca.getDescription());
                startActivity(intent);

            }
        });

        listViewBacheca.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Bacheca bacheca = listaBacheca.get(position);
                if(bUser==null || !isManager()) {
                    Toast.makeText(getApplicationContext(),"Solo un manager può modificare le bacheche.", Toast.LENGTH_SHORT).show();
                }
                else {
                    showModificaBachecaDialog(bacheca);
                }
                return true;
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

        managerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ManagerActivity.class);
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

        cercaProfiloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RicercaProfiloActivity.class);
                startActivity(intent);
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
        final Button addBachecaButton = (Button) dialogView.findViewById(R.id.addButton);

        dialogBuilder.setTitle("Crea bacheca");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        addBachecaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();


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

                Date data = new Date();
                String id = databaseBacheca.push().getKey();
                Bacheca bacheca = new Bacheca(id, title, description, bUser.getDisplayName(),bUser.getUid(), data);
                databaseBacheca.child(bacheca.getId()).setValue(bacheca);
                Toast.makeText(getApplicationContext(), "Bacheca aggiunta", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

    }

    private void showModificaBachecaDialog(Bacheca bacheca){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.modifica_bacheca_dialog,null);
            dialogBuilder.setView(dialogView);

        final EditText editTextTitleModifica = (EditText) dialogView.findViewById(R.id.editTextTitoloBacheca);
        final EditText editTextDescriptionModifica = (EditText) dialogView.findViewById(R.id.editTextDescriptionBacheca);
        final Button modificaBachecaButton = (Button) dialogView.findViewById(R.id.modificaBachecaButton);
        final Button cancellaBachecaButton = (Button) dialogView.findViewById(R.id.cancellaBachecaButton);

        editTextTitleModifica.setText(bacheca.getTitle());
        editTextDescriptionModifica.setText(bacheca.getDescription());
        final String id = bacheca.getId();

        dialogBuilder.setTitle("Modifica bacheca");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


            modificaBachecaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitleModifica.getText().toString();
                String description = editTextDescriptionModifica.getText().toString();


                if(TextUtils.isEmpty(editTextTitleModifica.getText())||editTextTitleModifica.getText().length()>20 || confrontaBacheche(editTextTitleModifica.getText().toString())){
                    editTextTitleModifica.setError("Il titolo non può essere vuoto.\n Deve avere un massimo di 20 caratteri.\n Non possono esistere due Bacheche di uno stesso paese.");
                    editTextTitleModifica.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(editTextDescriptionModifica.getText()) || editTextDescriptionModifica.getText().length()>200){
                    editTextDescriptionModifica.setError("La descrizione non può essere vuota.\n Deve avere un massimo di 200 caratteri.");
                    editTextDescriptionModifica.requestFocus();
                    return;
                }

                Date data = new Date();
                String id = databaseBacheca.push().getKey();
                Bacheca bacheca = new Bacheca(id, title, description, bUser.getDisplayName(), bUser.getUid(), data);
                databaseBacheca.child(bacheca.getId()).setValue(bacheca);
                Toast.makeText(getApplicationContext(), "Bacheca Modificata", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        databasePost= FirebaseDatabase.getInstance().getReference("post").child(id);
        databasePost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Post post = postSnapshot.getValue(Post.class);
                    listaPost.add(post);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cancellaBachecaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancellaBacheca(id);
                alertDialog.dismiss();
            }
        });

    }

    //si devono eliminare pure i commenti o lo fa automativamente?
    private void cancellaBacheca(String id){
        databaseBacheca.child(id).removeValue();
        for (Post elemento : listaPost){
            String idPost = elemento.getId();
            DatabaseReference commenti = FirebaseDatabase.getInstance().getReference("commento").child(idPost);
            DatabaseReference postDaEliminare = FirebaseDatabase.getInstance().getReference("post").child(id).child(idPost);
            postDaEliminare.removeValue();
            commenti.removeValue();
        }

        Toast.makeText(this,"Bacheca eliminata", Toast.LENGTH_SHORT).show();
    }

    private boolean confrontaBacheche(String titolo) {
        boolean value= true;
        if(!listaBacheca.isEmpty()) {
            for (Bacheca bacheca : listaBacheca) {
                if (bacheca.getTitle().equals(titolo)) {
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
        if (ruoloManager.equals("manager"))
            return true;
        else
            return false;
    }
}
