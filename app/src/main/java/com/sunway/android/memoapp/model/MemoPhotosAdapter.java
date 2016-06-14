package com.sunway.android.memoapp.model;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.util.DataConstant;

import java.util.List;

/**
 * Created by Mr_RexZ on 6/8/2016.
 */
public class MemoPhotosAdapter extends RecyclerView.Adapter<MemoPhotosHolder> {

    private Context context;
    private Activity activity;
    private List<ImageView> imageViewList;
    private int memoID;
    private int adapterPosition;
    private MemoItemAdapter memoItemAdapter;
    private int flag;


    public MemoPhotosAdapter(Activity activity, List<ImageView> imageViewList, int memoID) {
        this.context = activity;
        this.activity = activity;
        this.imageViewList = imageViewList;
        this.memoID = memoID;
        this.flag = DataConstant.TEXT_DETAILS_MEMO_RECYCLERVIEWADAPTER;
    }

    public MemoPhotosAdapter(Activity activity, List<ImageView> imageViewList, int memoID, MemoItemAdapter memoItemAdapter) {
        this.context = activity;
        this.activity = activity;
        this.imageViewList = imageViewList;
        this.memoID = memoID;
        this.memoItemAdapter = memoItemAdapter;
        this.flag = DataConstant.NESTED_TEXT_MEMO_RECYCLERVIEWADAPTER;
    }

    @Override
    public MemoPhotosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_photos_memo, parent, false);
        MemoPhotosHolder photosHolder = new MemoPhotosHolder(layoutView, context, activity, memoID, memoItemAdapter, this);

        return photosHolder;
    }


    @Override
    public void onBindViewHolder(MemoPhotosHolder holder, int position) {

        if (holder.linearLayout.getChildCount() > 0) {
            holder.linearLayout.removeAllViews();
        }

        if (imageViewList.get(position) != null) {
            if (imageViewList.get(position).getParent() != null)
                ((ViewGroup) imageViewList.get(position).getParent()).removeView(imageViewList.get(position));
            holder.linearLayout.addView(imageViewList.get(position));
        }

    }

    public int getPosition() {
        return adapterPosition;
    }

    public void setPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }

    public List<ImageView> getImagesList() {
        return imageViewList;
    }

    public void removeImagesList(int index) {
        imageViewList.remove(index);
    }
    @Override
    public int getItemCount() {
        return imageViewList.size();
    }
}
