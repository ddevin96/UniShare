package com.example.dani.unishare;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BachecaList extends ArrayAdapter<Bacheca> {
    private Activity context;
    private List<Bacheca> listaBacheca;

    public BachecaList(Activity context, List<Bacheca> listaBacheca) {
        super(context, R.layout.list_view_bacheca, listaBacheca);
        this.context = context;
        this.listaBacheca = listaBacheca;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_view_bacheca, null, true);
        TextView title = (TextView) listViewItem.findViewById((R.id.textViewTitolo));

        Bacheca bacheca = listaBacheca.get(position);
        title.setText(bacheca.getTitle());

        return listViewItem;
    }
}
