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

public class PostList extends ArrayAdapter<Post> {
    private Activity context;
    private List<Post> listaPost;

    public PostList(Activity context, List<Post> listaPost) {
        super(context, R.layout.list_vew_post, listaPost);
        this.context = context;
        this.listaPost = listaPost;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_vew_post, null, true);
        TextView title = (TextView) listViewItem.findViewById((R.id.textViewTitoloPost));

        Post post = listaPost.get(position);
        title.setText(post.getTitle());

        return listViewItem;
    }
}
