package com.app.termproject;

public class PostListItem
{
    String image;
    String title;

    String getImage(){
        return this.image;
    }
    String getTitle(){
        return this.title;
    }

    PostListItem(String image, String title){
        this.image=image;
        this.title=title;
    }

}
