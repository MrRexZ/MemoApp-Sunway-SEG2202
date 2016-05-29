package com.sunway.android.memoapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Mr_RexZ on 5/30/2016.
 */
public class MemoItemViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener {


        public TextView titleName;
        public TextView contentName;

        public MemoItemViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            titleName = (TextView) itemView.findViewById(R.id.list_item_title);
            contentName = (TextView) itemView.findViewById(R.id.list_item_content);
        }

        @Override
        public void onClick(View view)
        {
            Toast.makeText(view.getContext(),
                    "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT)
                    .show();
        }
}
