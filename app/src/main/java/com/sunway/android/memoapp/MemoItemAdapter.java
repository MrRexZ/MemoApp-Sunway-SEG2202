package com.sunway.android.memoapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mr_RexZ on 5/27/2016.
 */
public class MemoItemAdapter extends ArrayAdapter<MemoItem> {

    public MemoItemAdapter(Activity context, List<MemoItem> listMemo) {
        super(context, 0, listMemo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        MemoItem memoItem = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_memo, parent, false);
        }

        TextView versionNameView = (TextView) convertView.findViewById(R.id.list_item_title);
        versionNameView.setText(memoItem.title);

        TextView versionNumberView = (TextView) convertView.findViewById(R.id.list_item_content);
        versionNumberView.setText(memoItem.content);
        return convertView;
    }
}
