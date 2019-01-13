package com.example.dani.unishare;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class PostListTest {
  ArrayList<Post> listaPost;
  PostActivity act;
  @Before
  public void setUp(){
    act = new PostActivity();
    listaPost = new ArrayList<>();
    listaPost.add(new Post("abc","Residenze","Descrizione","Daniele","ddv",new Date()));
    listaPost.add(new Post("ade","Appartamenti","Descrizione","Donatella","dcf",new Date()));
    act.listaPost = listaPost;
    PostList lista = new PostList(act,listaPost);
  }

  @Test
  public void getView() {
  }
}