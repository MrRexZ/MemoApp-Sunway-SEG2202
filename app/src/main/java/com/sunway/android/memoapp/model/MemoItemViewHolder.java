package com.sunway.android.memoapp.model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.view.TextDetailsMemoActivity;

import java.util.regex.Matcher;

/**
 * Created by Mr_RexZ on 5/30/2016.
 */
public class MemoItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView titleName;
        public TextView contentName;
        public String memoID;
        private Context context;

    private Matcher m;

    private  final String DELIMITER= Character.toString((char) 31);
    private final String LINE_SEPERATOR = System.getProperty("line.separator");

        public MemoItemViewHolder(View itemView, Context context)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            titleName = (TextView) itemView.findViewById(R.id.list_item_title);
            contentName = (TextView) itemView.findViewById(R.id.list_item_content);
            this.context=context;
        }

        @Override
        public void onClick(View view)
        {

            /*
            Toast.makeText(view.getContext(),
                    "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT)
                    .show();
                    */
            Intent showDetail = new Intent(context, TextDetailsMemoActivity.class)
                    .putExtra("ACTION_MODE","EDIT")
                    .putExtra("TEXTID",memoID)
                    .putExtra("TITLE",titleName.getText().toString())
                    .putExtra("DETAILS",contentName.getText().toString());
            context.startActivity(showDetail);
        }


}
