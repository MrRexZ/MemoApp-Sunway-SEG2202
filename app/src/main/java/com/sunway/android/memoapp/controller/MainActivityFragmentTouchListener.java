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
import com.sunway.android.memoapp.model.MyApplication;
import com.sunway.android.memoapp.util.DataConstant;
import com.sunway.android.memoapp.util.ListOperation;
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

                MemoItem memoitem = ListOperation.getIndividualMemoItem(position);
                if (memoitem instanceof MemoTextItem) {
                    MemoTextItem memoTextItem = (MemoTextItem) memoitem;
                    Intent showDetail = new Intent(fragment.getActivity(), TextDetailsMemoActivity.class)
                            .putExtra(DataConstant.ACTION_MODE, DataConstant.EDIT)
                            .putExtra(DataConstant.TEXT_ID, memoitem.getMemoID())
                            .putExtra("TITLE", memoTextItem.getTitle())
                            .putExtra("DETAILS", memoTextItem.getContent())
                            .putExtra("PHOTOS", memoTextItem.getPhotosCount());
                    fragment.getActivity().startActivity(showDetail);
                } else if (memoitem instanceof MemoDrawingItem) {
                    MemoDrawingItem memoDrawingItem = (MemoDrawingItem) memoitem;
                    Intent showDrawing = new Intent(fragment.getActivity(), DrawingMemoActivity.class)
                            .putExtra(DataConstant.ACTION_MODE, DataConstant.EDITDRAWING)
                            .putExtra(DataConstant.TEXT_ID, memoitem.getMemoID());
                    fragment.getActivity().startActivity(showDrawing);


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