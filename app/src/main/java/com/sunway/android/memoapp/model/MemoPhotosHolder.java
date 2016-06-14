package com.sunway.android.memoapp.model;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.sunway.android.memoapp.R;

/**
 * Created by Mr_RexZ on 6/8/2016.
 */
public class MemoPhotosHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

    public MemoItemAdapter memoItemAdapter;
    public LinearLayout linearLayout;
    private MemoPhotosAdapter memoPhotosAdapter;
    private Context context;
    private Activity activity;
    private int position;


    public MemoPhotosHolder(View itemView, Context context, Activity activity, int position, MemoItemAdapter memoItemAdapter, MemoPhotosAdapter memoPhotosAdapter) {
        super(itemView);
        this.context = context;
        this.memoItemAdapter = memoItemAdapter;
        this.activity = activity;
        linearLayout = (LinearLayout) itemView.findViewById(R.id.linearlayout_list_item_photos_memo);
        this.memoPhotosAdapter = memoPhotosAdapter;
        this.position = position;
        linearLayout.setOnLongClickListener(this);
    }


    @Override
    public boolean onLongClick(View v) {
        memoPhotosAdapter.setPosition(getAdapterPosition());
        return false;
    }
}
