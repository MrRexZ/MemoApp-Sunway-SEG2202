package com.sunway.android.memoapp.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.util.ListOperation;
import com.sunway.android.memoapp.view.TextDetailsMemoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr_RexZ on 5/30/2016.
 */
public class MemoItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView titleName;
    public TextView contentName;
    public RecyclerView photosRecyclerView;
    public LinearLayout recyclerViewHolder;
    public String memoID;
    public int photosCount;
    public List<ImageView> imageViewList = new ArrayList<>();
    public MemoPhotosAdapter memoPhotosAdapter;
    private Context context;
    private MemoItemAdapter mAdapter;
    private Activity activity;
    private View view;


    public MemoItemViewHolder(View itemView, Context context, MemoItemAdapter mAdapter, Activity activity) {
        super(itemView);

        this.context = context;
        this.mAdapter = mAdapter;
        this.activity = activity;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);


        titleName = (TextView) itemView.findViewById(R.id.list_item_title);
        contentName = (TextView) itemView.findViewById(R.id.list_item_content);


        recyclerViewHolder = (LinearLayout) itemView.findViewById(R.id.linearlayout_photos_recyclerview_container);
        recyclerViewHolder.setOnLongClickListener(this);
        recyclerViewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("SUR!!");
            }
        });

        photosRecyclerView = (RecyclerView) recyclerViewHolder.findViewById(R.id.recycler_view_photos_memo);


    }

    private List<ImageView> getImageViewList() {
        return imageViewList;
    }

    public void setPhotosAdapter() {


        StaggeredGridLayoutManager photosGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        photosRecyclerView.setLayoutManager(photosGridLayoutManager);
        imageViewList = getImageViewList();
        memoPhotosAdapter = new MemoPhotosAdapter(activity, imageViewList, memoID, mAdapter);
        System.out.println("Memo ID is : " + memoID);
        photosRecyclerView.setAdapter(memoPhotosAdapter);
        photosRecyclerView.setOnLongClickListener(this);


    }

    public void addPhotosToList(ImageView newImageView) {
        imageViewList.add(newImageView);
    }

    @Override
    public void onClick(View view) {

        MemoItem memoitem = ListOperation.getListViewItems().get(getAdapterPosition());

        if (view.getId() == R.id.card_view_list_text) {
            Intent showDetail = new Intent(context, TextDetailsMemoActivity.class)
                    .putExtra("ACTION_MODE", "EDIT")
                    .putExtra("TEXTID", memoitem.getMemoID())
                    .putExtra("TITLE", memoitem.getTitle())
                    .putExtra("DETAILS", memoitem.getContent())
                    .putExtra("PHOTOS", memoitem.getPhotosCount());
            context.startActivity(showDetail);
        }

    }

    @Override
    public boolean onLongClick(View v) {

        mAdapter.setPosition(getAdapterPosition());
        return false;
    }


    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }


}
