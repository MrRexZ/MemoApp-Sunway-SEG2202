package com.sunway.android.memoapp.model;

/**
 * Created by Mr_RexZ on 5/27/2016.
 */
public class MemoTextItem implements MemoItem {

    private String title;
    private String content;
    private int memoid;
    int image; // drawable reference id

    public MemoTextItem(int memoid, String title, String content)
    {
        setTitle(title);
        setContent(content);
        setMemoID(memoid);
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

    public void setMemoID(int memoid) {
        this.memoid=memoid;

    }

    public String getMemoID(){
        return Integer.toString(memoid);
    }


}
