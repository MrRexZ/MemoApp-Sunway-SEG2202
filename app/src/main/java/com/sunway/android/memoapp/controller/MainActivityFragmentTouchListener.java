package com.sunway.android.memoapp.controller;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.sunway.android.memoapp.model.MemoDrawingItem;
import com.sunway.android.memoapp.model.MemoItem;
import com.sunway.android.memoapp.model.MemoItemAdapter;
import com.sunway.android.memoapp.model.MemoTextItem;
import com.sunway.android.memoapp.util.C;
import com.sunway.android.memoapp.util.MyApplication;
import com.sunway.android.memoapp.view.DrawingMemoActivity;
import com.sunway.android.memoapp.view.MainActivityFragment;
import com.sunway.android.memoapp.view.TextDetailsMemoActivity;

/**
 * Created by Mr_RexZ on 6/13/2016.
 */
public class MainActivityFragmentTouchListener implements RecyclerView.OnItemTouchListener {

    private MainActivityFragment fragment;
    private View child;
    private RecyclerView viewRecycle;
    private MemoItemAdapter memoItemAdapter;
    private GestureDetector gestureDetector = new GestureDetector(MyApplication.getAppContext(), new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (child != null) {
                int position = viewRecycle.getChildAdapterPosition(child);

                MemoItem memoitem = memoItemAdapter.nMemoList.get(position);
                if (memoitem instanceof MemoTextItem) {
                    MemoTextItem memoTextItem = (MemoTextItem) memoitem;
                    Intent showDetail = new Intent(fragment.getActivity(), TextDetailsMemoActivity.class)
                            .putExtra(C.ACTION_MODE, C.EDIT)
                            .putExtra(C.MEMO_ID, memoitem.getMemoID())
                            .putExtra(C.INPUT_TITLE, memoTextItem.getTitle())
                            .putExtra(C.INPUT_DETAILS, memoTextItem.getContent())
                            .putExtra(C.PHOTOS, memoTextItem.getPhotosCount())
                            .putExtra(C.MEMO_OBJECT, memoTextItem);
                    fragment.getActivity().startActivity(showDetail);
                    return true;
                } else if (memoitem instanceof MemoDrawingItem) {
                    MemoDrawingItem memoDrawingItem = (MemoDrawingItem) memoitem;
                    Intent showDrawing = new Intent(fragment.getActivity(), DrawingMemoActivity.class)
                            .putExtra(C.ACTION_MODE, C.EDITDRAWING)
                            .putExtra(C.MEMO_ID, memoDrawingItem.getMemoID())
                            .putExtra(C.MEMO_OBJECT, memoDrawingItem);
                    fragment.getActivity().startActivity(showDrawing);
                    return true;


                }

            }


            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (child != null) {
                int position = viewRecycle.getChildAdapterPosition(child);
                memoItemAdapter.setPosition(position);
                fragment.registerForContextMenu(viewRecycle);
                fragment.getActivity().openContextMenu(viewRecycle);

            }
        }
    });

    public MainActivityFragmentTouchListener(MainActivityFragment fragment, MemoItemAdapter memoItemAdapter) {
        this.fragment = fragment;
        this.memoItemAdapter = memoItemAdapter;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        child = rv.findChildViewUnder(e.getX(), e.getY());
        viewRecycle = rv;

        return child != null && gestureDetector.onTouchEvent(e);

    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }


    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

}
