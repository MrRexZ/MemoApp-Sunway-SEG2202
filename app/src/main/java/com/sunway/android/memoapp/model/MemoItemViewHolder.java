package com.sunway.android.memoapp.model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.util.ListOperation;
import com.sunway.android.memoapp.view.TextDetailsMemoActivity;

/**
 * Created by Mr_RexZ on 5/30/2016.
 */
public class MemoItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView titleName;
    public TextView contentName;
    public LinearLayout photosHolder;
    public String memoID;
    public int photosCount;
    private Context context;
    private MemoItemAdapter mAdapter;
    private OnItemLongClickListener ma;

    public MemoItemViewHolder(View itemView, Context context, MemoItemAdapter mAdapter) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);


        titleName = (TextView) itemView.findViewById(R.id.list_item_title);
        contentName = (TextView) itemView.findViewById(R.id.list_item_content);
        photosHolder = (LinearLayout) itemView.findViewById(R.id.photosHolder);
        photosHolder.setOnClickListener(this);
        this.context = context;
        this.mAdapter = mAdapter;


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

        if (view.getId() == R.id.photosHolder) {
            System.out.println("IT WORKS!!!");
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
