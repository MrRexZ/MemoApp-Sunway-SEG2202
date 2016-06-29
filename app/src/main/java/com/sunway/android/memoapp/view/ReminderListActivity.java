package com.sunway.android.memoapp.view;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.controller.AlarmReceiver;
import com.sunway.android.memoapp.controller.ReminderActivityTouchListener;
import com.sunway.android.memoapp.model.MemoDrawingItem;
import com.sunway.android.memoapp.model.MemoItem;
import com.sunway.android.memoapp.model.MemoTextItem;
import com.sunway.android.memoapp.model.Reminder;
import com.sunway.android.memoapp.model.ReminderAdapter;
import com.sunway.android.memoapp.util.FileOperation;
import com.sunway.android.memoapp.util.ListOperation;

public class ReminderListActivity extends AppCompatActivity {

    private ReminderAdapter reminderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_list);

        Toolbar upper_toolbar = (Toolbar) findViewById(R.id.toolbar_upper);
        setSupportActionBar(upper_toolbar);
        upper_toolbar.setTitle("Memo App");

        Toolbar bottom_toolbar = (Toolbar) findViewById(R.id.toolbar_bottom);
        bottom_toolbar.inflateMenu(R.menu.bottom_reminder_list_menu);
        bottom_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_reminder);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        reminderAdapter = new ReminderAdapter(
                this, ListOperation.getListViewItems());
        recyclerView.setAdapter(reminderAdapter);

        recyclerView.addOnItemTouchListener(new ReminderActivityTouchListener(this, recyclerView, reminderAdapter));

        TextView backButton = (TextView) findViewById(R.id.action_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.floating_context_reminderitem_long_click, menu);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upper_reminder_list_menu, menu);
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        int position = reminderAdapter.getPosition();
        MemoItem memoItem = reminderAdapter.getMemoItemArrayList().get(position);

        Reminder selectedReminder = memoItem.getReminder();
        int memoID = memoItem.getMemoID();
        int oldYear = selectedReminder.getYear();
        int oldMonth = selectedReminder.getMonth();
        int oldDay = selectedReminder.getDay();
        int oldHour = selectedReminder.getHour();
        int oldMinute = selectedReminder.getMinute();
        int oldSecond = selectedReminder.getSecond();
        if (memoItem instanceof MemoTextItem) {
            MemoTextItem memoTextItem = (MemoTextItem) memoItem;
            FileOperation.replaceSelected(
                    FileOperation.DELIMITER_LINE + FileOperation.DELIMITER_UNIT + memoItem.getMemoID() + FileOperation.DELIMITER_UNIT + FileOperation.DELIMITER_LINE +
                            "photos=" + memoTextItem.getPhotosCount() + FileOperation.DELIMITER_LINE +
                            memoID + FileOperation.DELIMITER_LINE +
                            memoTextItem.getTitle() + FileOperation.DELIMITER_LINE +
                            memoTextItem.getContent() + FileOperation.DELIMITER_LINE +
                            "reminder=" + oldYear + "," + oldMonth + "," + oldDay + "," + oldHour + "," + oldMinute + "," + oldSecond + FileOperation.DELIMITER_LINE,

                    FileOperation.DELIMITER_LINE + FileOperation.DELIMITER_UNIT + memoID + FileOperation.DELIMITER_UNIT + FileOperation.DELIMITER_LINE +
                            "photos=" + memoTextItem.getPhotosCount() + FileOperation.DELIMITER_LINE +
                            memoTextItem.getTitle() + FileOperation.DELIMITER_LINE +
                            memoTextItem.getContent() + FileOperation.DELIMITER_LINE +
                            "reminder=" + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + FileOperation.DELIMITER_LINE);
            ListOperation.modifyTextList(memoID, memoTextItem.getPhotosCount(), memoTextItem.getTitle(), memoTextItem.getContent(), 0, 0, 0, 0, 0, 0);

        } else if (memoItem instanceof MemoDrawingItem) {

            FileOperation.replaceSelected(
                    FileOperation.DELIMITER_LINE + FileOperation.DELIMITER_UNIT + (memoID) + FileOperation.DELIMITER_UNIT + FileOperation.DELIMITER_LINE +
                            "Drawing" + FileOperation.DELIMITER_LINE
                            + "reminder=" + oldYear + "," + oldMonth + "," + oldDay + "," + oldHour + "," + oldMinute + "," + oldSecond + FileOperation.DELIMITER_LINE,
                    FileOperation.DELIMITER_LINE + FileOperation.DELIMITER_UNIT + (memoID) + FileOperation.DELIMITER_UNIT + FileOperation.DELIMITER_LINE +
                            "Drawing" + FileOperation.DELIMITER_LINE
                            + "reminder=" + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + FileOperation.DELIMITER_LINE
            );
            ListOperation.modifyDrawingList(memoID, 0, 0, 0, 0, 0, 0);
        }


        reminderAdapter.getMemoItemArrayList().remove(position);

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent.getBroadcast(getBaseContext(), memoID, intent,
                PendingIntent.FLAG_UPDATE_CURRENT).cancel();

        refreshAdapter();

        return super.onContextItemSelected(item);
    }

    private void refreshAdapter() {

        reminderAdapter.notifyDataSetChanged();
    }
}
