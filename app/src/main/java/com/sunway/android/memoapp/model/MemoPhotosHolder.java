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
public class MemoPhotosHolder extends RecyclerView.ViewHolder {

    public MemoItemAdapter photosAdapter;
    public LinearLayout linearLayout;
    private Context context;
    private Activity activity;
    private int position;


    public MemoPhotosHolder(View itemView, Context context, MemoItemAdapter mAdapter, Activity activity, String position) {
        super(itemView);
        this.context = context;
        this.photosAdapter = mAdapter;
        this.activity = activity;
        linearLayout = (LinearLayout) itemView.findViewById(R.id.linearlayout_list_item_photos_memo);

        this.position = Integer.parseInt(position);
    }


}
