package com.example.dani.unishare;

import com.example.dani.unishare.Bean.Post;
import com.example.dani.unishare.GestioneUtente.PostActivity;
import com.example.dani.unishare.List.PostList;

import org.junit.Before;

import java.util.ArrayList;
import java.util.Date;

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