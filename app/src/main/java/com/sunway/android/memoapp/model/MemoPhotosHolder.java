package com.sunway.android.memoapp.model;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.sunway.android.memoapp.R;

/**
 * Created by Mr_RexZ on 6/8/2016.
 */
public class MemoPhotosHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

    public MemoItemAdapter memoItemAdapter;
    public ImageView imageView;
    private MemoPhotosAdapter memoPhotosAdapter;
    private Context context;
    private Activity activity;
    private int position;


    public MemoPhotosHolder(View itemView, Context context, Activity activity, int position, MemoItemAdapter memoItemAdapter, MemoPhotosAdapter memoPhotosAdapter) {
        super(itemView);
        this.context = context;
        this.memoItemAdapter = memoItemAdapter;
        this.activity = activity;
        imageView = (ImageView) itemView.findViewById(R.id.imageview_list_item_photos_memo);
        this.memoPhotosAdapter = memoPhotosAdapter;
        this.position = position;
        imageView.setOnLongClickListener(this);
    }



    @Override
    public boolean onLongClick(View v) {
        System.out.println("WAH");
        memoPhotosAdapter.setPosition(getAdapterPosition());
        return false;
    }
}
