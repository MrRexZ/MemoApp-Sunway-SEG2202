package com.sunway.android.memoapp.model;

/**
 * Created by Mr_RexZ on 6/4/2016.
 */
public interface MemoItem {


    int getMemoID();

    void setMemoID(int memoid);

    Reminder getReminder();

    void setReminder(Reminder reminder);
}
