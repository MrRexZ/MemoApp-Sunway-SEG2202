package com.sunway.android.memoapp.view;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.controller.AlarmReceiver;
import com.sunway.android.memoapp.controller.BitmapStoringWorkerTask;
import com.sunway.android.memoapp.model.MemoPhotosAdapter;
import com.sunway.android.memoapp.util.C;
import com.sunway.android.memoapp.util.FileOperation;
import com.sunway.android.memoapp.util.ListOperation;
import com.sunway.android.memoapp.util.MyApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Mr_RexZ on 5/28/2016.
 */
public class TextDetailsMemoActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    public int detail_photosCount = 0;
    public List<String> imageViewArrayListDetails = new ArrayList<>();
    public ArrayList<String> filePathList = new ArrayList<>();
    public Intent intent;
    private String oldTitle;
    private String oldDetails;
    private String newTitle;
    private String newDetails;
    private String ACTION_MODE;
    private int SELECT_PICTURE = 1;
    private int REQUEST_PERMISSION = 2;
    private String TAG = "SAVING IMAGE";
    private String latestImageName;
    private int memoID;
    private MemoPhotosAdapter memoPhotosAdapter;
    private ArrayList<Integer> positionsToBeDeleted = new ArrayList<>();
    private RecyclerView photosRecyclerView;

    private List<String> getImageViewList() {
        return imageViewArrayListDetails;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_details);
        intent = getIntent();
        ACTION_MODE = intent.getStringExtra(C.ACTION_MODE);
        detail_photosCount = intent.getExtras().getInt(C.PHOTOS);
        oldTitle = intent.getStringExtra(C.INPUT_TITLE);
        oldDetails = intent.getStringExtra(C.INPUT_DETAILS);
        EditText textviewTitle = (EditText) findViewById(R.id.title_text_input);
        EditText detailsviewTitle = (EditText) findViewById(R.id.details_text_input);
        textviewTitle.setText(oldTitle);
        detailsviewTitle.setText(oldDetails);


        photosRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_details);
        memoID = intent.getExtras().getInt(C.TEXT_ID);

        StaggeredGridLayoutManager photosGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        photosRecyclerView.setLayoutManager(photosGridLayoutManager);
        memoPhotosAdapter = new MemoPhotosAdapter(this, imageViewArrayListDetails, memoID, C.DETAILS_ACTIVITY_DISPLAY);
        photosRecyclerView.setAdapter(memoPhotosAdapter);

        registerForContextMenu(photosRecyclerView);

            int count = 0;
            while (count < detail_photosCount) {
                File fileName = new File("u_" + FileOperation.userID + "_img_" + memoID + "_" + (count++) + ".jpg");
                File filePath = new File(FileOperation.mydir, fileName.toString());

                if (filePath.exists()) {
                    imageViewArrayListDetails.add(filePath.toString());
                    filePathList.add(filePath.toString());
                }
            }


        Toolbar upper_toolbar = (Toolbar) findViewById(R.id.toolbar_upper);
        setSupportActionBar(upper_toolbar);
        Toolbar bottom_toolbar = (Toolbar) findViewById(R.id.toolbar_bottom);
        bottom_toolbar.inflateMenu(R.menu.bottom_textdetailsmemo_menu);
        bottom_toolbar.setOnMenuItemClickListener(this);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {


                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();


                loadImageAfterAdd(filePath);


            }
        }
    }

    private void loadImageAfterAdd(String filePath) {
        final BitmapStoringWorkerTask task = new BitmapStoringWorkerTask(memoPhotosAdapter, this, memoID, ACTION_MODE);
        task.execute(filePath);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upper_textdetailsmemo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reminder) {
            EditText textviewTitle = (EditText) findViewById(R.id.title_text_input);
            EditText textviewDetails = (EditText) findViewById(R.id.details_text_input);
            Intent showReminder = new Intent(this, ReminderActivity.class)
                    .putExtra(C.INPUT_TITLE, textviewTitle.getText().toString())
                    .putExtra(C.INPUT_DETAILS, textviewDetails.getText().toString())
                    .putExtra(C.TEXT_ID, memoID)
                    .putExtra(C.PHOTOS, detail_photosCount)
                    .putExtra(C.MEMO_TYPE, C.TEXT_MEMO)
                    .putExtra(C.ACTION_MODE, ACTION_MODE);
            startActivity(showReminder);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_submit_text_memo) {
            EditText textviewTitle = (EditText) findViewById(R.id.title_text_input);
            EditText textviewDetails = (EditText) findViewById(R.id.details_text_input);

            newTitle = textviewTitle.getText().toString();
            newDetails = textviewDetails.getText().toString();

            if (!newTitle.isEmpty() || !newDetails.isEmpty()) {

                Intent showMainActivity = new Intent(TextDetailsMemoActivity.this, MainActivity.class)
                        .putExtra(C.INPUT_TITLE, newTitle)
                        .putExtra(C.INPUT_DETAILS, newDetails)
                        .putExtra(C.PHOTOS, detail_photosCount);

                Iterator iteratorDeletion = positionsToBeDeleted.iterator();
                while (iteratorDeletion.hasNext()) {
                    int position = (int) iteratorDeletion.next();

                    File file = new File(FileOperation.mydir, filePathList.get(position));
                    FileOperation.deleteIndividualPhotosMemo(filePathList.get(position));
                    filePathList.remove(position);
                }

                if (ACTION_MODE.equals(C.EDIT) || ACTION_MODE.equals("DELETE")) {

                    int oldPhotosCount = intent.getExtras().getInt(C.PHOTOS);
                    FileOperation.replaceSelected(
                            FileOperation.DELIMITER_LINE + FileOperation.DELIMITER_UNIT + memoID + FileOperation.DELIMITER_UNIT + FileOperation.DELIMITER_LINE + "photos=" + oldPhotosCount + FileOperation.DELIMITER_LINE + oldTitle + FileOperation.DELIMITER_LINE + oldDetails + FileOperation.DELIMITER_LINE,
                            FileOperation.DELIMITER_LINE + FileOperation.DELIMITER_UNIT + memoID + FileOperation.DELIMITER_UNIT + FileOperation.DELIMITER_LINE + "photos=" + detail_photosCount + FileOperation.DELIMITER_LINE + newTitle + FileOperation.DELIMITER_LINE + newDetails + FileOperation.DELIMITER_LINE);
                    showMainActivity.putExtra(C.ACTION_MODE, C.EDIT);


                    ListOperation.modifyTextList(memoID, detail_photosCount, oldTitle, oldDetails, newTitle, newDetails);
                } else
                    showMainActivity.putExtra(C.ACTION_MODE, C.ADD);

                boolean HAS_REMINDER = intent.getExtras().getBoolean(C.HAS_REMINDER);
                boolean alarmUp = (PendingIntent.getBroadcast(getBaseContext(), memoID,
                        new Intent(getBaseContext(), AlarmReceiver.class),
                        PendingIntent.FLAG_NO_CREATE) != null);

                if (HAS_REMINDER) {
                    Calendar targetCal = (Calendar) intent.getSerializableExtra(C.REMINDER_DETAILS);

                    IntentFilter filter = new IntentFilter();
                    filter.addAction("android.provider.Telephony.SMS_RECEIVED");

                    Intent intent = new Intent(getBaseContext(), AlarmReceiver.class)
                            .putExtra(C.INPUT_TITLE, textviewTitle.getText().toString())
                            .putExtra(C.INPUT_DETAILS, textviewDetails.getText().toString())
                            .putExtra(C.TEXT_ID, memoID)
                            .putExtra(C.PHOTOS, detail_photosCount)
                            .putExtra(C.ACTION_MODE, C.EDIT)
                            .putExtra(C.MEMO_TYPE, C.TEXT_MEMO);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), memoID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
                } else if (alarmUp) {
                    Intent intent = new Intent(getBaseContext(), AlarmReceiver.class)
                            .putExtra(C.INPUT_TITLE, textviewTitle.getText().toString())
                            .putExtra(C.INPUT_DETAILS, textviewDetails.getText().toString())
                            .putExtra(C.TEXT_ID, memoID)
                            .putExtra(C.PHOTOS, detail_photosCount)
                            .putExtra(C.ACTION_MODE, C.EDIT)
                            .putExtra(C.MEMO_TYPE, C.TEXT_MEMO);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), memoID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                }

                startActivity(showMainActivity);
                return true;

            } else {
                Toast.makeText(MyApplication.getAppContext(), "Please enter text into the memo field", Toast.LENGTH_SHORT).show();
            }
        }

        if (id == R.id.action_add_picture) {

            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

            if (ContextCompat.checkSelfPermission(TextDetailsMemoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(TextDetailsMemoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION);
            }

            startActivityForResult(i, SELECT_PICTURE);
            return true;

        }

        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.floating_context_memoitem_long_click, menu);


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        //Memo photo recently added does not have filepath variable
        int position = memoPhotosAdapter.getPosition();
        positionsToBeDeleted.add(position);
        imageViewArrayListDetails.remove(position);
        //watch out for action_mode compatibility with MainActivityFragment in readFile operation

        memoPhotosAdapter.notifyDataSetChanged();

        return super.onContextItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}
