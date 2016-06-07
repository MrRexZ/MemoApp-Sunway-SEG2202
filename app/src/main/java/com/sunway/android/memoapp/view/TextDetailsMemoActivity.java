package com.sunway.android.memoapp.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.util.FileOperation;
import com.sunway.android.memoapp.util.ListOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Mr_RexZ on 5/28/2016.
 */
public class TextDetailsMemoActivity extends AppCompatActivity {

    private String oldTitle;
    private String oldDetails;
    private String newTitle;
    private String newDetails;
    private String ACTION_MODE;
    private int SELECT_PICTURE = 1;
    private String selectedImagePath;
    private int REQUEST_PERMISSION = 2;
    private String TAG = "SAVING IMAGE";
    private int detail_photosCount = 0;
    private String latestImageName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_details);
        ACTION_MODE=getIntent().getStringExtra("ACTION_MODE");

        detail_photosCount = getIntent().getExtras().getInt("PHOTOS");


        if (getIntent().hasExtra("ACTION_MODE") && ACTION_MODE.equals("EDIT")) {
            EditText textviewTitle = (EditText) findViewById(R.id.title_text_input);
            EditText detailsviewTitle = (EditText) findViewById(R.id.details_text_input);

            oldTitle=getIntent().getStringExtra("TITLE");
            oldDetails=getIntent().getStringExtra("DETAILS");
            textviewTitle.setText(oldTitle);
            detailsviewTitle.setText(oldDetails);

            String memoID = getIntent().getExtras().getString("TEXTID");
            int start = 0;
            while (start <= detail_photosCount) {

                FileOperation.loadImageFromStorage(getApplicationContext().getFilesDir().getPath().toString(), "u_" + FileOperation.userID + "_img_" + memoID + "_" + (start++) + ".jpg", (LinearLayout) findViewById(R.id.linearlayout_details), 550, this);

            }

        }

        Toolbar upper_toolbar = (Toolbar) findViewById(R.id.toolbar_upper);
        setSupportActionBar(upper_toolbar);
        Toolbar bottom_toolbar = (Toolbar) findViewById(R.id.toolbar_bottom);
        bottom_toolbar.inflateMenu(R.menu.bottom_textdetailsmemo_menu);
        bottom_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                int id = arg0.getItemId();
                if (id == R.id.action_submit_text_memo) {
                    EditText textviewTitle = (EditText) findViewById(R.id.title_text_input);
                    EditText detailsviewTitle = (EditText) findViewById(R.id.details_text_input);

                    newTitle=textviewTitle.getText().toString();
                    newDetails=detailsviewTitle.getText().toString();


                    Intent showMainActivity = new Intent(TextDetailsMemoActivity.this, MainActivity.class)
                            .putExtra("TITLE",newTitle)
                            .putExtra("DETAILS", newDetails)
                            .putExtra("PHOTOS", detail_photosCount);


                    if (getIntent().hasExtra("ACTION_MODE") && ACTION_MODE.equals("EDIT")){

                        String memoID=getIntent().getStringExtra("TEXTID");
                        int oldPhotosCount = getIntent().getExtras().getInt("PHOTOS");

                        FileOperation.replaceSelected(
                                FileOperation.DELIMITER_LINE + FileOperation.DELIMITER_UNIT + memoID + FileOperation.DELIMITER_UNIT + FileOperation.DELIMITER_LINE + "photos=" + oldPhotosCount + FileOperation.DELIMITER_LINE + oldTitle + FileOperation.DELIMITER_LINE + oldDetails + FileOperation.DELIMITER_LINE,
                                FileOperation.DELIMITER_LINE + FileOperation.DELIMITER_UNIT + memoID + FileOperation.DELIMITER_UNIT + FileOperation.DELIMITER_LINE + "photos=" + detail_photosCount + FileOperation.DELIMITER_LINE + newTitle + FileOperation.DELIMITER_LINE + newDetails + FileOperation.DELIMITER_LINE);
                        showMainActivity.putExtra("ACTION_MODE", "EDIT");


                        ListOperation.modifyTextList(memoID, detail_photosCount, oldTitle, oldDetails, newTitle, newDetails);
                    }
                    else
                        showMainActivity.putExtra("ACTION_MODE","ADD");

                    startActivity(showMainActivity);
                    return true;
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

                }
                return false;
            }
        });


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

                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);

                storeImage(yourSelectedImage);


                System.out.println("PATH with : " + getApplicationContext().getFilesDir().getPath().toString() + "and" + latestImageName);
                FileOperation.loadImageFromStorage(getApplicationContext().getFilesDir().getPath().toString(), latestImageName, (LinearLayout) findViewById(R.id.linearlayout_details), 550, this);
//loadImageFromStorage(getApplicationContext().getFilesDir().getPath().toString(),latestImageName);
            }
        }
    }

    private void loadImageFromStorage(String path, String filename) {

        try {
            File f = new File(path, filename);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            //   ImageView img=(ImageView)findViewById(R.id.imgPicker);
            ImageView img = new ImageView(this);
            img.setImageBitmap(b);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(550, 550);
            img.setLayoutParams(layoutParams);
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearlayout_details);

            linearLayout.addView(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }


    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(
                "/data/data/"
                        + getApplicationContext().getPackageName()
                        + "/files");

        //   String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = null;

        int memoID = 0;
        if (getIntent().hasExtra("ACTION_MODE") && ACTION_MODE.equals("EDIT")) {
            memoID = Integer.parseInt(getIntent().getStringExtra("TEXTID"));
        } else if (getIntent().hasExtra("ACTION_MODE") && ACTION_MODE.equals("ADD")) {
            memoID = FileOperation.getMemoTextCountId();
        }

        mImageName = "u_" + FileOperation.userID + "_img_" + memoID + "_" + detail_photosCount++ + ".jpg";

        latestImageName = mImageName;
        mediaFile = new File(getApplicationContext().getFilesDir().getPath().toString() + File.separator + latestImageName);

        return mediaFile;
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upper_textdetailsmemo_menu, menu);
        return true;
    }

}
