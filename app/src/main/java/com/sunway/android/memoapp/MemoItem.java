package com.sunway.android.memoapp;

/**
 * Created by Mr_RexZ on 5/27/2016.
 */
public class MemoItem {

    String title;
    String content;
    int image; // drawable reference id

    public MemoItem(String title, String content)
    {
        this.title = title;
        this.content =content;
    }

}
