package com.sunway.android.memoapp.model;

/**
 * Created by Mr_RexZ on 5/27/2016.
 */
public class MemoTextItem implements MemoItem {

    int image; // drawable reference id
    private String title;
    private String content;
    private int memoid;

    public MemoTextItem(int memoid, String title, String content) {
        setTitle(title);
        setContent(content);
        setMemoID(memoid);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMemoID() {
        return Integer.toString(memoid);
    }

    public void setMemoID(int memoid) {
        this.memoid = memoid;

    }


}
