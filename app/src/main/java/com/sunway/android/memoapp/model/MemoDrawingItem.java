package com.sunway.android.memoapp.model;

import java.io.Serializable;

/**
 * Created by Mr_RexZ on 6/14/2016.
 */
public class MemoDrawingItem implements MemoItem, Serializable {

    private int memoid;
    private Reminder reminder;
    private String title;
    private String content;

    public MemoDrawingItem(int memoid, Reminder newReminder) {
        setMemoID(memoid);
        setReminder(newReminder);
        title = "Drawing Memo";
        content = "Click to view info";

    }

    @Override
    public int getMemoID() {
        return memoid;
    }

    @Override
    public void setMemoID(int memoid) {
        this.memoid = memoid;
    }

    public Reminder getReminder() {
        return this.reminder;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
