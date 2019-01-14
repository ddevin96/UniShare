package com.example.dani.unishare;

import android.view.View;
import android.widget.ListView;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class PostListTest {
  ArrayList<Post> listaPost;
  PostActivity act;
  PostList lista;
  @Before
  public void setUp(){
    act = new PostActivity();
    listaPost = new ArrayList<>();
    listaPost.add(new Post("abc","Residenze","Descrizione","Daniele","ddv",new Date()));
    listaPost.add(new Post("ade","Appartamenti","Descrizione","Donatella","dcf",new Date()));
    act.listaPost = listaPost;
    lista = new PostList(act,listaPost);
  }


}