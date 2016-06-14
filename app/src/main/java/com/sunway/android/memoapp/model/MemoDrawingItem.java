package com.sunway.android.memoapp.model;

/**
 * Created by Mr_RexZ on 6/14/2016.
 */
public class MemoDrawingItem implements MemoItem {

    private int memoid;

    public MemoDrawingItem(int memoid) {
        setMemoID(memoid);

    }

    @Override
    public int getMemoID() {
        return memoid;
    }

    @Override
    public void setMemoID(int memoid) {
        this.memoid = memoid;
    }
}
