package com.sunway.android.memoapp.controller;

import com.sunway.android.memoapp.model.MemoItem;

import java.util.Comparator;

/**
 * Created by Mr_RexZ on 6/25/2016.
 */
public class MemoItemComparator implements Comparator<MemoItem> {
    @Override
    public int compare(MemoItem lhs, MemoItem rhs) {
        if (lhs.getSortOrder() > rhs.getSortOrder())
            return 1;
        else if (lhs.getSortOrder() < rhs.getSortOrder())
            return -1;
        else
            return 0;
    }
}
