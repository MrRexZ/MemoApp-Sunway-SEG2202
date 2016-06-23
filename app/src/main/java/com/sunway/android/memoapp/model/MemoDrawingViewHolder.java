package com.sunway.android.memoapp.model;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.sunway.android.memoapp.R;


/**
 * Created by Mr_RexZ on 6/15/2016.
 */
public class MemoDrawingViewHolder extends RecyclerView.ViewHolder {
    public ImageView drawingContainer;
    private Activity activity;
    private MemoItemAdapter memoItemAdapter;

    public MemoDrawingViewHolder(View itemView, MemoItemAdapter memoItemAdapter, Activity activity) {
        super(itemView);
        this.memoItemAdapter = memoItemAdapter;
        this.activity = activity;
        drawingContainer = (ImageView) itemView.findViewById(R.id.imageview_list_item_drawing_memo);

    }
}
