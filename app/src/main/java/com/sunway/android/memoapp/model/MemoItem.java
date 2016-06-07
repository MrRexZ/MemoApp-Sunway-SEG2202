package com.sunway.android.memoapp.model;

/**
 * Created by Mr_RexZ on 6/4/2016.
 */
public interface MemoItem  {


    String getTitle();

    void setTitle(String title);

    String getContent();

    void setContent(String content);

    String getMemoID();

    int getPhotosCount();

    void setPhotosCount(int photosCount);
    boolean equals(Object object);
}
