package com.sunway.android.memoapp.model;

import java.io.Serializable;

/**
 * Created by Mr_RexZ on 5/27/2016.
 */
public class MemoTextItem implements MemoItem, Serializable {

    int image; // drawable reference id
    private String title;
    private String content;
    private int memoid;
    private int photosCount;
    private Reminder reminder;

    public MemoTextItem(int memoid, int photosCount, String title, String content, Reminder reminder) {
        setTitle(title);
        setContent(content);
        setMemoID(memoid);
        setPhotosCount(photosCount);
        setReminder(reminder);
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

    public int getMemoID() {
        return memoid;
    }

    public void setMemoID(int memoid) {
        this.memoid = memoid;

    }

    public int getPhotosCount() {
        return photosCount;
    }

    public void setPhotosCount(int photosCount) {
        this.photosCount = photosCount;
    }

    public Reminder getReminder() {
        return this.reminder;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }
}
