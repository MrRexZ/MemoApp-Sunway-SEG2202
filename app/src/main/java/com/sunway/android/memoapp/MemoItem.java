package com.sunway.android.memoapp;

/**
 * Created by Mr_RexZ on 5/27/2016.
 */
public class MemoItem {

    private String title;
    private String content;
    int image; // drawable reference id

    public MemoItem(String title, String content)
    {
        this.title = title;
        this.content =content;
    }

    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public void setContent(String content) {

        this.content=content;
    }


}
