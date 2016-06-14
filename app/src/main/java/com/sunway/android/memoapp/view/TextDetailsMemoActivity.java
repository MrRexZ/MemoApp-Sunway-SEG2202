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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.model.MemoPhotosAdapter;
import com.sunway.android.memoapp.util.DataConstant;
import com.sunway.android.memoapp.util.FileOperation;
import com.sunway.android.memoapp.util.ListOperation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    private int REQUEST_PERMISSION = 2;
    private String TAG = "SAVING IMAGE";
    private int detail_photosCount = 0;
    private String latestImageName;
    private List<ImageView> imageViewArrayListDetails = new ArrayList<>();
    private MemoPhotosAdapter memoPhotosAdapter;
    private int memoID;
    private ArrayList<String> filePathList = new ArrayList<>();
    private ArrayList<Integer> positionsToBeDeleted = new ArrayList<>();
    private List<Boolean> existingPhotos = new ArrayList<>();
    //  private HashMap<ImageView, Boolean> imageViewBooleanHashMap = new HashMap<ImageView,Boolean>();


    private List<ImageView> getImageViewList() {
        return imageViewArrayListDetails;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_details);

        ACTION_MODE = getIntent().getStringExtra(DataConstant.ACTION_MODE);
        positionsToBeDeleted.clear();
        existingPhotos.clear();
        detail_photosCount = getIntent().getExtras().getInt(DataConstant.PHOTOS);

        RecyclerView photosRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_details);
        memoID = getIntent().getExtras().getInt(DataConstant.TEXT_ID);

        StaggeredGridLayoutManager photosGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        photosRecyclerView.setLayoutManager(photosGridLayoutManager);
        memoPhotosAdapter = new MemoPhotosAdapter(this, imageViewArrayListDetails, memoID);
        photosRecyclerView.setAdapter(memoPhotosAdapter);

        registerForContextMenu(photosRecyclerView);

        if (ACTION_MODE.equals("EDIT")) {
            EditText textviewTitle = (EditText) findViewById(R.id.title_text_input);
            EditText detailsviewTitle = (EditText) findViewById(R.id.details_text_input);

            oldTitle=getIntent().getStringExtra("TITLE");
            oldDetails=getIntent().getStringExtra("DETAILS");
            textviewTitle.setText(oldTitle);
            detailsviewTitle.setText(oldDetails);

            int count = 0;
            while (count < detail_photosCount) {
                String filePath = "u_" + FileOperation.userID + "_img_" + memoID + "_" + (count++) + ".jpg";
                File file = new File(getApplicationContext().getFilesDir().getPath().toString(), filePath);
                if (file.exists()) {

                    imageViewArrayListDetails.add(FileOperation.loadImageFromStorage(getApplicationContext().getFilesDir().getPath().toString(), filePath, 550, this));
                    filePathList.add(filePath);
                    existingPhotos.add(true);
                }
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


                    int oldPhotosCount = getIntent().getExtras().getInt("PHOTOS");
                    if (ACTION_MODE.equals("EDIT") || ACTION_MODE.equals("DELETE")) {
                        Iterator iteratorDeletion = positionsToBeDeleted.iterator();
                        while (iteratorDeletion.hasNext()) {
                            int position = (int) iteratorDeletion.next();

                            if (existingPhotos.get(position) == true) {
                                File file = new File(getApplicationContext().getFilesDir().getPath().toString(), filePathList.get(position));
                                FileOperation.deleteIndividualPhotosMemo(filePathList.get(position));
                                filePathList.remove(position);
                            }
                            existingPhotos.remove(position);

                        }
                        int memoID = getIntent().getExtras().getInt(DataConstant.TEXT_ID);

                        FileOperation.replaceSelected(
                                FileOperation.DELIMITER_LINE + FileOperation.DELIMITER_UNIT + memoID + FileOperation.DELIMITER_UNIT + FileOperation.DELIMITER_LINE + "photos=" + oldPhotosCount + FileOperation.DELIMITER_LINE + oldTitle + FileOperation.DELIMITER_LINE + oldDetails + FileOperation.DELIMITER_LINE,
                                FileOperation.DELIMITER_LINE + FileOperation.DELIMITER_UNIT + memoID + FileOperation.DELIMITER_UNIT + FileOperation.DELIMITER_LINE + "photos=" + detail_photosCount + FileOperation.DELIMITER_LINE + newTitle + FileOperation.DELIMITER_LINE + newDetails + FileOperation.DELIMITER_LINE);
                        showMainActivity.putExtra("ACTION_MODE", "EDIT");


                        ListOperation.modifyTextList(memoID, detail_photosCount, oldTitle, oldDetails, newTitle, newDetails);
                    }
                    else
                        showMainActivity.putExtra(DataConstant.ACTION_MODE, DataConstant.ADD);


                    Iterator iteratorArrayListImage = imageViewArrayListDetails.iterator();
                    Iterator existingPhotosIterator = existingPhotos.iterator();


                    while (iteratorArrayListImage.hasNext()) {
                        //If not existing photos (if false) and the arraylist has content :

                        ImageView imView = (ImageView) iteratorArrayListImage.next();
                        if ((Boolean) existingPhotosIterator.next() == false) {
                            imView.buildDrawingCache();
                            Bitmap bMap = imView.getDrawingCache();
                            storeImage(bMap, oldPhotosCount);
                            oldPhotosCount++;
                        }
                    }

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
                    return true;

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
                ImageView img = new ImageView(this);
                img.setImageBitmap(yourSelectedImage);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(550, 550);
                img.setLayoutParams(layoutParams);

                //WANT TO ADD FILEPATH FOR THIS, BUT NO FILEPATH. FIND A WAY!
                imageViewArrayListDetails.add(img);
                memoPhotosAdapter.notifyDataSetChanged();

                existingPhotos.add(false);

                detail_photosCount++;
            }
        }
    }


    private void storeImage(Bitmap image, int photoID) {
        File pictureFile = getOutputMediaFile(photoID);
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");
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


    private File getOutputMediaFile(int photoID) {


        //   String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = null;

        int memoID = 0;
        if (getIntent().hasExtra(DataConstant.ACTION_MODE) && ACTION_MODE.equals(DataConstant.EDIT)) {
            memoID = getIntent().getExtras().getInt(DataConstant.TEXT_ID);
        } else if (getIntent().hasExtra(DataConstant.ACTION_MODE) && ACTION_MODE.equals(DataConstant.ADD)) {
            memoID = FileOperation.getMemoTextCountId();
        }

        mImageName = "u_" + FileOperation.userID + "_img_" + memoID + "_" + photoID + ".jpg";

        latestImageName = mImageName;
        mediaFile = new File(getApplicationContext().getFilesDir().getPath().toString() + File.separator + latestImageName);

        return mediaFile;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upper_textdetailsmemo_menu, menu);
        return true;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");

        Intent showMainActivity = new Intent(TextDetailsMemoActivity.this, MainActivity.class)
                .putExtra("TITLE", oldTitle == null ? "" : oldTitle)
                .putExtra("DETAILS", oldDetails == null ? "" : oldDetails)
                .putExtra("ACTION_MODE", "BACK")
                .putExtra("PHOTOS", detail_photosCount);
        startActivity(showMainActivity);
    }
}
