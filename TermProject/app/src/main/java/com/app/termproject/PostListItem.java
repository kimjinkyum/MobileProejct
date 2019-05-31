package com.app.termproject;

public class PostListItem
{
    String image;
    String title;
    String content;
    String postKey;
    String pinnumber;
    String getImage(){
        return this.image;
    }
    String getTitle(){
        return this.title;
    }
    String getContent(){return this.content;}
    String getPostKey(){return this.postKey;}
    String getPinnumber(){return this.pinnumber;}
    PostListItem(String image, String title,String content,String postKey,String pinnumber){
        this.image=image;
        this.title=title;
        this.content=content;
        this.postKey=postKey;
        this.pinnumber=pinnumber;

    }

}
