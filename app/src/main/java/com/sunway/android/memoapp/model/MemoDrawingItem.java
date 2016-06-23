package com.sunway.android.memoapp.model;

/**
 * Created by Mr_RexZ on 6/14/2016.
 */
public class MemoDrawingItem implements MemoItem {

    private int memoid;
    private Reminder reminder;

    public MemoDrawingItem(int memoid, Reminder newReminder) {
        setMemoID(memoid);
        setReminder(newReminder);

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
}
