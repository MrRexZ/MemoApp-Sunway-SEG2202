package com.sunway.android.memoapp.model;

/**
 * Created by Mr_RexZ on 6/4/2016.
 */
public interface MemoItem  {


    String getTitle();
    String getContent();
    String getMemoID();


    void setTitle(String title);
    void setContent(String content);
    boolean equals(Object object);
}
