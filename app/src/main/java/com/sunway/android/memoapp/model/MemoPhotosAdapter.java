package com.sunway.android.memoapp.model;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sunway.android.memoapp.R;

import java.util.List;

/**
 * Created by Mr_RexZ on 6/8/2016.
 */
public class MemoPhotosAdapter extends RecyclerView.Adapter<MemoPhotosHolder> {

    private Context context;
    private Activity activity;
    private List<ImageView> imageViewList;
    private String position;
    private MemoItemAdapter memoItemAdapter;

    public MemoPhotosAdapter(Activity activity, List<ImageView> imageViewList, String memoID, MemoItemAdapter memoItemAdapter) {
        this.context = activity;
        this.activity = activity;
        this.imageViewList = imageViewList;
        this.position = memoID;
        this.memoItemAdapter = memoItemAdapter;
    }

    @Override
    public MemoPhotosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_photos_memo, parent, false);
        MemoPhotosHolder photosHolder = new MemoPhotosHolder(layoutView, context, memoItemAdapter, activity, position);
        layoutView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int pos = Integer.parseInt(position);
                memoItemAdapter.setPosition(pos);
                return false;
            }
        });


        return photosHolder;
    }


    @Override
    public void onBindViewHolder(MemoPhotosHolder holder, int position) {

        if (imageViewList.get(position).getParent() != null)
            ((ViewGroup) imageViewList.get(position).getParent()).removeView(imageViewList.get(position));
        holder.linearLayout.addView(imageViewList.get(position));

    }

    public void setPosition(String position) {
        this.position = position;
        memoItemAdapter.setPosition(Integer.parseInt(position));
    }

    @Override
    public int getItemCount() {
        return imageViewList.size();
    }
}
