package com.sunway.android.memoapp.controller;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.sunway.android.memoapp.model.MemoDrawingItem;
import com.sunway.android.memoapp.model.MemoItem;
import com.sunway.android.memoapp.model.MemoTextItem;
import com.sunway.android.memoapp.model.ReminderAdapter;
import com.sunway.android.memoapp.util.C;
import com.sunway.android.memoapp.util.MyApplication;
import com.sunway.android.memoapp.view.DrawingMemoActivity;
import com.sunway.android.memoapp.view.ReminderListActivity;
import com.sunway.android.memoapp.view.TextDetailsMemoActivity;

import java.lang.ref.WeakReference;

/**
 * Created by Mr_RexZ on 6/25/2016.
 */
public class ReminderActivityTouchListener implements RecyclerView.OnItemTouchListener {

    private WeakReference<ReminderListActivity> weakReferenceReminderListActivity;
    private RecyclerView recyclerView;
    private View childView;
    private ReminderAdapter reminderAdapter;

    private GestureDetector gestureDetector = new GestureDetector(MyApplication.getAppContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            if (childView != null) {
                int position = recyclerView.getChildAdapterPosition(childView);

                MemoItem memoitem = reminderAdapter.getMemoItemArrayList().get(position);
                if (memoitem instanceof MemoTextItem) {
                    MemoTextItem memoTextItem = (MemoTextItem) memoitem;
                    Intent showDetail = new Intent(weakReferenceReminderListActivity.get(), TextDetailsMemoActivity.class)
                            .putExtra(C.ACTION_MODE, C.EDIT)
                            .putExtra(C.MEMO_ID, memoitem.getMemoID())
                            .putExtra(C.INPUT_TITLE, memoTextItem.getTitle())
                            .putExtra(C.INPUT_DETAILS, memoTextItem.getContent())
                            .putExtra(C.PHOTOS, memoTextItem.getPhotosCount());
                    weakReferenceReminderListActivity.get().startActivity(showDetail);
                    return true;
                } else if (memoitem instanceof MemoDrawingItem) {
                    MemoDrawingItem memoDrawingItem = (MemoDrawingItem) memoitem;
                    Intent showDrawing = new Intent(weakReferenceReminderListActivity.get(), DrawingMemoActivity.class)
                            .putExtra(C.ACTION_MODE, C.EDITDRAWING)
                            .putExtra(C.MEMO_ID, memoDrawingItem.getMemoID());
                    weakReferenceReminderListActivity.get().startActivity(showDrawing);
                    return true;


                }
            }


            return super.onSingleTapUp(e);
        }


        @Override
        public void onLongPress(MotionEvent e) {
            if (childView != null) {
                int position = recyclerView.getChildAdapterPosition(childView);
                reminderAdapter.setPosition(position);
                weakReferenceReminderListActivity.get().registerForContextMenu(recyclerView);
                weakReferenceReminderListActivity.get().openContextMenu(recyclerView);

            }
        }

    });


    public ReminderActivityTouchListener(ReminderListActivity reminderListActivity, RecyclerView recyclerView, ReminderAdapter reminderAdapter) {
        weakReferenceReminderListActivity = new WeakReference<ReminderListActivity>(reminderListActivity);
        this.recyclerView = recyclerView;
        this.reminderAdapter = reminderAdapter;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        childView = rv.findChildViewUnder(e.getX(), e.getY());
        recyclerView = rv;

        return childView != null && gestureDetector.onTouchEvent(e);

    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
