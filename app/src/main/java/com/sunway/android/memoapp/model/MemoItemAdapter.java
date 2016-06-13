package com.sunway.android.memoapp.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.util.FileOperation;
import com.sunway.android.memoapp.util.ListOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        MemoItemViewHolder rcv = new MemoItemViewHolder(layoutView, this, activity);


        return rcv;
    }

    @Override
    public void onBindViewHolder(MemoItemViewHolder holder, int position)
    {

        System.out.println("the pos is :" + position);
        holder.titleName.setText(ListOperation.getListViewItems().get(position).getTitle());
        holder.contentName.setText(ListOperation.getListViewItems().get(position).getContent());
        holder.memoID = ListOperation.getListViewItems().get(position).getMemoID();
        holder.photosCount = ListOperation.getListViewItems().get(position).getPhotosCount();

        if (holder.photosRecyclerView.getChildCount() > 0) {
            holder.photosRecyclerView.removeAllViews();
            holder.imageViewList.clear();
        }
        int count = 0;


        while (count < holder.photosCount) {
            String filePath = "u_" + FileOperation.userID + "_img_" + holder.memoID + "_" + (count++) + ".jpg";
            File file = new File(MyApplication.getAppContext().getFilesDir().getPath().toString(), filePath);
            if (file.exists()) {
                Bitmap b = null;
                try {
                    b = BitmapFactory.decodeStream(new FileInputStream(file));
                    ImageView img = new ImageView(holder.itemView.getContext());
                    img.setImageBitmap(b);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
                    img.setLayoutParams(layoutParams);
                    holder.addPhotosToList(img);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
/*
        while (start <= holder.photosCount) {

         //   File f = new File(MyApplication.getAppContext().getFilesDir().getPath().toString(), "u_" + FileOperation.userID + "_img_" + holder.memoID + "_" + (start++) + ".jpg");

            Bitmap b = null;
            try {
                b = BitmapFactory.decodeStream(new FileInputStream(f));
                ImageView img = new ImageView(holder.itemView.getContext());
                img.setImageBitmap(b);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
                img.setLayoutParams(layoutParams);
                holder.addPhotosToList(img);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        */
        holder.setPhotosAdapter();


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
