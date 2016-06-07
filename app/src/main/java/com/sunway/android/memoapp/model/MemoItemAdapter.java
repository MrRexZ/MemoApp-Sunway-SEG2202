package com.sunway.android.memoapp.model;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.util.FileOperation;
import com.sunway.android.memoapp.util.ListOperation;

import java.util.List;

/**
 * Created by Mr_RexZ on 5/27/2016.
 */
public class MemoItemAdapter extends RecyclerView.Adapter<MemoItemViewHolder> {

    private Context context;
    private Activity activity;
    private int position;

    public MemoItemAdapter(Activity context, List<MemoItem> listMemo) {
        this.context=context;
        this.activity = context;

    }



    @Override
    public MemoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_text_memo, null);
        MemoItemViewHolder rcv = new MemoItemViewHolder(layoutView, context, this);


        return rcv;
    }

    @Override
    public void onBindViewHolder(MemoItemViewHolder holder, int position)
    {
        holder.titleName.setText(ListOperation.getListViewItems().get(position).getTitle());
        holder.contentName.setText(ListOperation.getListViewItems().get(position).getContent());
        holder.memoID = ListOperation.getListViewItems().get(position).getMemoID();
        holder.photosCount = ListOperation.getListViewItems().get(position).getPhotosCount();

        int start = 0;
        while (start <= holder.photosCount) {
            FileOperation.loadImageFromStorage(context.getFilesDir().getPath().toString(), "u_" + FileOperation.userID + "_img_" + holder.memoID + "_" + (start++) + ".jpg", holder.photosHolder, 100, activity);
        }
        

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
