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

/**
 * <p>Classe usata per la visualizzazione della ListView dei post.</p>
 */
public class PostList extends ArrayAdapter<Post> {
  private Activity context;
  private List<Post> listaPost;

  /**
   * Classe PostList.
   *
   * @see PostList
   *
   * @param context oggetto di tipo Activity in cui è utilizzata la listView
   * @param listaPost Oggetto di tipo List che contiene i post presenti nel database
   */
  public PostList(Activity context, List<Post> listaPost) {
    super(context, R.layout.list_vew_post, listaPost);
    this.context = context;
    this.listaPost = listaPost;
  }

  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    LayoutInflater inflater = context.getLayoutInflater();
    View listViewItem = inflater.inflate(R.layout.list_vew_post, null, true);

    TextView autore = (TextView) listViewItem.findViewById((R.id.textViewAutorePost));
    TextView title = (TextView) listViewItem.findViewById((R.id.textViewTitoloPost));

    Post post = listaPost.get(position);
    autore.setText(post.getAuthor());
    title.setText(post.getTitle());

    return listViewItem;
  }
}
