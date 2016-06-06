package com.sunway.android.memoapp.model;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.util.ListOperation;

import java.util.List;

/**
 * Created by Mr_RexZ on 5/27/2016.
 */
public class MemoItemAdapter extends RecyclerView.Adapter<MemoItemViewHolder> {

    private Context context;
    private int position;

    public MemoItemAdapter(Activity context, List<MemoItem> listMemo) {
        this.context=context;

    }



    @Override
    public MemoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_text_memo, null);
        final MemoItemViewHolder rcv = new MemoItemViewHolder(layoutView, context, this);


        return rcv;
    }

    @Override
    public void onBindViewHolder(MemoItemViewHolder holder, int position)
    {
        holder.titleName.setText(ListOperation.getListViewItems().get(position).getTitle());
        holder.contentName.setText(ListOperation.getListViewItems().get(position).getContent());
        holder.memoID = ListOperation.getListViewItems().get(position).getMemoID();


    }

    @Override
    public int getItemCount()
    {
        return ListOperation.getListViewItems().size();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int clickedposition) {
        this.position=clickedposition;
    }

}
