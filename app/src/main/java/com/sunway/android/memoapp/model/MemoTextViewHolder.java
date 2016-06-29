package com.sunway.android.memoapp.model;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.util.C;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr_RexZ on 5/30/2016.
 */
public class MemoTextViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

    public TextView titleName;
    public TextView contentName;
    public RecyclerView photosRecyclerView;
    public int memoID;
    public int photosCount;
    public List<String> imageViewList = new ArrayList<>();
    public MemoPhotosAdapter memoPhotosAdapter;
    private MemoItemAdapter memoItemAdapter;
    private Activity activity;


    public MemoTextViewHolder(View itemView, MemoItemAdapter memoItemAdapter, Activity activity) {
        super(itemView);
        this.memoItemAdapter = memoItemAdapter;
        this.activity = activity;
        titleName = (TextView) itemView.findViewById(R.id.list_item_title);
        contentName = (TextView) itemView.findViewById(R.id.list_item_content);

        photosRecyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_photos_memo);
    }


    private List<String> getImageViewList() {
        return imageViewList;
    }

    public void setPhotosAdapter() {


        StaggeredGridLayoutManager photosGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        photosRecyclerView.setLayoutManager(photosGridLayoutManager);
        imageViewList = getImageViewList();
        memoPhotosAdapter = new MemoPhotosAdapter(activity, imageViewList, memoID, memoItemAdapter, C.MAIN_ACTIVITY_FRAGMENT_DISPLAY);
        photosRecyclerView.setAdapter(memoPhotosAdapter);

    }
    public void addPhotosToList(String uri) {
        imageViewList.add(uri);
    }


    @Override
    public boolean onLongClick(View v) {

        memoItemAdapter.setPosition(getAdapterPosition());
        return true;
    }


}
