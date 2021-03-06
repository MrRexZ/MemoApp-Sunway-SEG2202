package com.sunway.android.memoapp.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.util.BitmapOperation;

import java.util.List;

/**
 * Created by Mr_RexZ on 6/8/2016.
 */
public class MemoPhotosAdapter extends RecyclerView.Adapter<MemoPhotosHolder> {

    private Context context;
    private Activity activity;
    private List<String> imageViewList;
    private int memoID;
    private int adapterPosition;
    private MemoItemAdapter memoItemAdapter;
    private String displayState;
    private Bitmap bitmapHolder;


    public MemoPhotosAdapter(Activity activity, List<String> imageViewList, int memoID, String displayState) {
        this.context = activity;
        this.activity = activity;
        this.imageViewList = imageViewList;
        this.memoID = memoID;
        Drawable dh = context.getResources().getDrawable(R.drawable.ic_launcher);
        bitmapHolder = ((BitmapDrawable) dh).getBitmap();
        bitmapHolder = BitmapOperation.getResizedBitmap(bitmapHolder, 400, 400);
        this.displayState = displayState;
    }

    public MemoPhotosAdapter(Activity activity, List<String> imageViewList, int memoID, MemoItemAdapter memoItemAdapter, String displayState) {
        this.context = activity;
        this.activity = activity;
        this.imageViewList = imageViewList;
        this.memoID = memoID;
        this.memoItemAdapter = memoItemAdapter;
        this.displayState = displayState;


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

        String filePath = imageViewList.get(position);
        BitmapOperation.loadBitmap(filePath, holder.imageView, context.getResources(), bitmapHolder, displayState);

    }


    public int getPosition() {
        return adapterPosition;
    }

    public void setPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }

    public List<String> getImagesList() {
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
