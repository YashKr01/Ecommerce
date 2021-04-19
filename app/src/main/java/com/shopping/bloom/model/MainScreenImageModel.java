package com.shopping.bloom.model;

public class MainScreenImageModel {

   int id , order ;
   String path ;

   public MainScreenImageModel(int id , String path , int order){
       this.id = id ;
       this.path = path;
       this.order = order;

   }

    public int getId() {
        return id;
    }

    public int getOrder() {
        return order;
    }

    public String getImagepath() {
        return path;
    }
}
