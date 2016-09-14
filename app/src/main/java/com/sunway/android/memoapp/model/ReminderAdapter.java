package com.sunway.android.memoapp.model;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunway.android.memoapp.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Mr_RexZ on 6/24/2016.
 */
public class ReminderAdapter extends RecyclerView.Adapter<ReminderViewHolder> {

    public List<MemoItem> memoItemArrayList = new ArrayList<>();
    private Context context;
    private Activity activity;
    private int position;

    public ReminderAdapter(Activity activity, List<MemoItem> memoItemArrayList) {

        Iterator iteratorList = memoItemArrayList.iterator();
        while (iteratorList.hasNext()) {
            MemoItem memoItem = (MemoItem) iteratorList.next();
            Reminder reminder = memoItem.getReminder();

            if (reminder.getYear() != 0 || reminder.getMonth() != 0 || reminder.getDay() != 0 || reminder.getHour() != 0 || reminder.getMinute() != 0 || reminder.getSecond() != 0)
                this.memoItemArrayList.add(memoItem);
        }
        this.activity = activity;
        context = activity;
    }

    @Override
    public ReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reminder_memo, parent, false);
        return new ReminderViewHolder(layoutView, this);
    }

    @Override
    public void onBindViewHolder(ReminderViewHolder holder, int position) {

        MemoItem selectedMemoItem = memoItemArrayList.get(position);
        Reminder selectedReminder = selectedMemoItem.getReminder();
        int month = selectedReminder.getMonth();
        String selectedMonth = null;
        switch (month) {
            case (1):
                selectedMonth = "January";
                break;
            case (2):
                selectedMonth = "February";
                break;
            case (3):
                selectedMonth = "March";
                break;
            case (4):
                selectedMonth = "April";
                break;
            case (5):
                selectedMonth = "May";
                break;
            case (6):
                selectedMonth = "June";
                break;
            case (7):
                selectedMonth = "July";
                break;
            case (8):
                selectedMonth = "August";
                break;
            case (9):
                selectedMonth = "September";
                break;
            case (10):
                selectedMonth = "October";
                break;
            case (11):
                selectedMonth = "November";
                break;
            case (12):
                selectedMonth = "December";
                break;
        }

        String title = selectedMemoItem.getTitle();
        String content = selectedMemoItem.getContent();

        if (title.length() > 30)
            title = new StringBuilder(title).substring(0, 30) + "...";
        if (content.length() > 80)
            content = new StringBuilder(content).substring(0, 80) + "...";
        holder.year_display.setText(Integer.toString(selectedReminder.getYear()) + ",");
        holder.month_display.setText(selectedMonth + " ");
        holder.day_display.setText(Integer.toString(selectedReminder.getDay()) + " ");
        holder.hour_display.setText(Integer.toString(selectedReminder.getHour()) + " : ");
        holder.minute_display.setText(Integer.toString(selectedReminder.getMinute()) + " : ");
        holder.second_display.setText(Integer.toString(selectedReminder.getSecond()) + "");
        holder.title_reminder.setText(title);
        holder.details_reminder.setText(content);
    }

    @Override
    public int getItemCount() {
        return memoItemArrayList.size();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<MemoItem> getMemoItemArrayList() {
        return memoItemArrayList;
    }
}
