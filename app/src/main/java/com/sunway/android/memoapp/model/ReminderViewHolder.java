package com.sunway.android.memoapp.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sunway.android.memoapp.R;

/**
 * Created by Mr_RexZ on 6/24/2016.
 */
public class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
    public TextView year_display;
    public TextView month_display;
    public TextView day_display;
    public TextView hour_display;
    public TextView minute_display;
    public TextView second_display;
    public TextView title_reminder;
    public TextView details_reminder;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private ReminderAdapter reminderAdapter;


    public ReminderViewHolder(View itemView, ReminderAdapter reminderAdapter) {
        super(itemView);
        this.reminderAdapter = reminderAdapter;
        year_display = (TextView) itemView.findViewById(R.id.year_text);
        month_display = (TextView) itemView.findViewById(R.id.month_text);
        day_display = (TextView) itemView.findViewById(R.id.day_text);
        hour_display = (TextView) itemView.findViewById(R.id.hour_text);
        minute_display = (TextView) itemView.findViewById(R.id.minute_text);
        second_display = (TextView) itemView.findViewById(R.id.second_text);
        title_reminder = (TextView) itemView.findViewById(R.id.title_reminder);
        details_reminder = (TextView) itemView.findViewById(R.id.details_reminder);


    }

    @Override
    public boolean onLongClick(View v) {

        reminderAdapter.setPosition(getAdapterPosition());
        return false;
    }
}
