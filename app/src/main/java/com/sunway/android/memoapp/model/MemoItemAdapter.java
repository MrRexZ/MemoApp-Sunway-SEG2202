package com.sunway.android.memoapp.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.util.FileOperation;
import com.sunway.android.memoapp.util.ListOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr_RexZ on 5/27/2016.
 */
public class MemoItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private final static int TEXT = 1;
    private final static int DRAWING = 2;
    public List<MemoItem> nMemoList = new ArrayList<MemoItem>();
    private Context context;
    private Activity activity;
    private int position;
    private Bitmap bitmapHolder;
    private String displayState;
    private MemoItemFilter mFilter = new MemoItemFilter();
    private List<MemoItem> oldMemoList = new ArrayList<MemoItem>();


    public MemoItemAdapter(Activity context, String displayState) {
        this.context = context;
        this.activity = context;

        Drawable dh = context.getResources().getDrawable(R.drawable.ic_launcher);
        bitmapHolder = ((BitmapDrawable) dh).getBitmap();
        this.displayState = displayState;
        nMemoList.addAll(ListOperation.getListViewItems());

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TEXT:
                View v1 = inflater.inflate(
                        R.layout.list_item_text_memo, null);
                viewHolder = new MemoTextViewHolder(v1, this, activity);
                break;
            case DRAWING:
                View v2 = inflater.inflate(
                        R.layout.list_item_drawing_memo, null);
                viewHolder = new MemoDrawingViewHolder(v2, this, activity);
                break;
            default:
                View v3 = inflater.inflate(
                        R.layout.list_item_text_memo, null);
                viewHolder = new MemoTextViewHolder(v3, this, activity);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MemoItem memoItem = nMemoList.get(position);
        switch (holder.getItemViewType()) {
            case (TEXT):
                MemoTextViewHolder v1 = (MemoTextViewHolder) holder;
                configureTextViewHolder(v1, memoItem);
                break;
            case DRAWING:
                MemoDrawingViewHolder v2 = (MemoDrawingViewHolder) holder;
                configureDrawingViewHolder(v2, memoItem);
                break;


        }

    }

    @Override
    public int getItemViewType(int position) {
        if (nMemoList.get(position) instanceof MemoTextItem)
            return TEXT;
        else if (nMemoList.get(position) instanceof MemoDrawingItem)
            return DRAWING;

        return -1;
    }

    @Override
    public int getItemCount() {
        return nMemoList.size();
    }

    public int getPosition() {

        return position;
    }

    public void setPosition(int clickedposition) {
        this.position = clickedposition;
    }

    private void configureTextViewHolder(MemoTextViewHolder hText, MemoItem memoItem) {

        MemoTextItem memoTextItem = (MemoTextItem) memoItem;
        hText.titleName.setText(memoTextItem.getTitle());
        hText.contentName.setText(memoTextItem.getContent());
        hText.memoID = memoTextItem.getMemoID();
        hText.photosCount = memoTextItem.getPhotosCount();

        if (hText.photosRecyclerView.getChildCount() > 0) {
            hText.photosRecyclerView.removeAllViews();
            hText.imageViewList.clear();
        }
        int count = 0;


        while (count < hText.photosCount) {
            String filePath = "u_" + FileOperation.userID + "_img_" + hText.memoID + "_" + (count++) + ".jpg";
            File file = new File(FileOperation.mydir, filePath);
            if (file.exists()) {
                try {

                    hText.addPhotosToList(file.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        hText.setPhotosAdapter();
    }


    private void configureDrawingViewHolder(MemoDrawingViewHolder hDrawing, MemoItem memoItem) {

        if (hDrawing.drawingContainer.getChildCount() > 0) {
            hDrawing.drawingContainer.removeAllViews();
        }
        MemoDrawingItem memoDrawingItem = (MemoDrawingItem) memoItem;


        String filePath = "u_" + FileOperation.userID + "_drawing_" + memoDrawingItem.getMemoID() + ".jpg";
        File file = new File(FileOperation.mydir, filePath);
        if (file.exists()) {
            Bitmap b = null;
            try {
                b = BitmapFactory.decodeStream(new FileInputStream(file));
                ImageView img = new ImageView(hDrawing.itemView.getContext());
                img.setImageBitmap(b);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(450, 550);
                img.setLayoutParams(layoutParams);
                hDrawing.drawingContainer.addView(img);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public Filter getFilter() {

        if (mFilter == null)
            mFilter = new MemoItemFilter();

        return mFilter;
    }

    private class MemoItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0 || constraint.equals("")) {
                results.values = ListOperation.getListViewItems();
                results.count = ListOperation.getListViewItems().size();
                System.out.println("hey");
            } else {
                List<MemoItem> tMemoList = new ArrayList<MemoItem>();

                for (MemoItem p : ListOperation.getListViewItems()) {
                    if (p instanceof MemoTextItem) {
                        MemoTextItem memoTextItem = (MemoTextItem) p;
                        if (memoTextItem.getTitle().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
                            tMemoList.add(memoTextItem);
                            System.out.println("all values exec:");
                        }
                    }
                }

                results.values = tMemoList;
                results.count = tMemoList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {


            nMemoList.clear();
            nMemoList.addAll((List<MemoItem>) results.values);
            System.out.println("all values are :" + nMemoList.size());
            notifyDataSetChanged();


        }


    }
}


