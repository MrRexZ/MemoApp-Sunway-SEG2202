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

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }
        public TextView titleName;
        public TextView contentName;
        public String memoID;
        private Context context;
    private OnItemLongClickListener listenerlong;

    private Matcher m;

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
                    "Clicked Position = " + getAdapterPosition(), Toast.LENGTH_SHORT)
                    .show();
*/

            MemoItem memoitem= MemoItemAdapter.itemList.get(getAdapterPosition());

if (view.getId()==R.id.card_view_list_text) {
    Intent showDetail = new Intent(context, TextDetailsMemoActivity.class)
            .putExtra("ACTION_MODE", "EDIT")
            .putExtra("TEXTID", memoitem.getMemoID())
            .putExtra("TITLE", memoitem.getTitle())
            .putExtra("DETAILS", memoitem.getContent());
    context.startActivity(showDetail);
}
        }


    public int pos() {
        return getPosition();
    }

  /*  @Override
    public boolean onLongClick(View v) {
        getA(holder.getPosition());
        return false;
    }*/


}
