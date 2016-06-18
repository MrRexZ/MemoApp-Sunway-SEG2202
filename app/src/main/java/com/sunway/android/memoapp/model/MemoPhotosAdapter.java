package com.sunway.android.memoapp.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.util.C;

import java.util.List;

/**
 * Created by Mr_RexZ on 6/8/2016.
 */
public class MemoPhotosAdapter extends RecyclerView.Adapter<MemoPhotosHolder> {

    private Context context;
    private Activity activity;
    private List<Bitmap> imageViewList;
    private int memoID;
    private int adapterPosition;
    private MemoItemAdapter memoItemAdapter;
    private int flag;


    public MemoPhotosAdapter(Activity activity, List<Bitmap> imageViewList, int memoID) {
        this.context = activity;
        this.activity = activity;
        this.imageViewList = imageViewList;
        this.memoID = memoID;
        this.flag = C.TEXT_DETAILS_MEMO_RECYCLERVIEWADAPTER;
    }

    public MemoPhotosAdapter(Activity activity, List<Bitmap> imageViewList, int memoID, MemoItemAdapter memoItemAdapter) {
        this.context = activity;
        this.activity = activity;
        this.imageViewList = imageViewList;
        this.memoID = memoID;
        this.memoItemAdapter = memoItemAdapter;
        this.flag = C.NESTED_TEXT_MEMO_RECYCLERVIEWADAPTER;
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

        Bitmap b = imageViewList.get(position);
        holder.imageView.setImageBitmap(b);

    }

    public int getPosition() {
        return adapterPosition;
    }

    public void setPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }

    public List<Bitmap> getImagesList() {
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
