package com.sunway.android.memoapp.model;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunway.android.memoapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr_RexZ on 5/27/2016.
 */
public class MemoItemAdapter extends RecyclerView.Adapter<MemoItemViewHolder> {

    public static List<MemoItem> itemList;
    private Context context;
    private int position;
    private ArrayList<MemoItemViewHolder> holderArray;

    public MemoItemAdapter(Activity context, List<MemoItem> listMemo) {
        this.context=context;
        this.itemList=listMemo;

    }



    @Override
    public MemoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_text_memo, null);
       final MemoItemViewHolder rcv = new MemoItemViewHolder(layoutView,context);
        rcv.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(rcv.pos());
                return false;
            }
        });


        return rcv;
    }

    @Override
    public void onBindViewHolder(MemoItemViewHolder holder, int position)
    {
        holder.titleName.setText(itemList.get(position).getTitle());
        holder.contentName.setText(itemList.get(position).getContent());
        holder.memoID=itemList.get(position).getMemoID();


    }

    @Override
    public int getItemCount()
    {
        return this.itemList.size();
    }


    public void setPosition(int clickedposition) {
        this.position=clickedposition;
    }

    public int getPosition() {
        return position;
    }

 /*   public void setOnItemLongClickListener(MemoItemViewHolder.OnItemLongClickListener listener){

        holderObject.listener.setonItemLongClickListener(listener);
    }*/

}
