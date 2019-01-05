package com.example.dani.unishare;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UtenteList extends ArrayAdapter<Utente> {
private Activity context;
private List<Utente> listaUtente;

public UtenteList(Activity context, List<Utente> listaUtente) {
        super(context, R.layout.list_view_utente, listaUtente);
        this.context = context;
        this.listaUtente = listaUtente;
        }

@NonNull
@Override
public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_view_utente, null, true);
        TextView nomeUtente = (TextView) listViewItem.findViewById((R.id.textViewNome));
        TextView cognomeUtente= (TextView) listViewItem.findViewById((R.id.textViewCognome));
        Utente utente = listaUtente.get(position);
        nomeUtente.setText(utente.getNome());
        cognomeUtente.setText(utente.getCognome());

        return listViewItem;
        }
        }