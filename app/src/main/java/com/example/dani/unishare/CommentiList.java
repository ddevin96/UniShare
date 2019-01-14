package com.example.dani.unishare;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * <p>Classe usata per la visualizzazione della ListView dei commenti.</p>
 */
public class CommentiList extends ArrayAdapter<Commento> {
  Activity context;
  List<Commento> lista;

  /**
   * Classe CommentiList.
   *
   * @param context oggetto di tipo Activity in cui è utilizzata la listView
   * @param lista   Oggetto di tipo List che contiene i commenti presenti nel database
   * @see CommentiList
   */
  public CommentiList(Activity context, List<Commento> lista) {
    super(context, R.layout.list_view_commenti, lista);
    this.context = context;
    this.lista = lista;
  }


  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = context.getLayoutInflater();
    View listViewItem = inflater.inflate(R.layout.list_view_commenti, null, true);

    TextView textViewCommentDescription = (TextView)
            listViewItem.findViewById(R.id.textViewCommentDescription);
    TextView textViewCommentAuthor = (TextView)
            listViewItem.findViewById(R.id.textViewCommentAuthor);

    Commento commento = lista.get(position);
    textViewCommentDescription.setText(commento.getDescription());
    textViewCommentAuthor.setText(commento.getAuthor());

    return listViewItem;
  }
}
