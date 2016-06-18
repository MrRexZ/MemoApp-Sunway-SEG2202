package com.sunway.android.memoapp.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.model.MemoPhotosAdapter;
import com.sunway.android.memoapp.model.MyApplication;
import com.sunway.android.memoapp.util.BitmapOperation;
import com.sunway.android.memoapp.util.C;
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
public class TextDetailsMemoActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

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
    private int memoID;
    private List<Bitmap> imageViewArrayListDetails = new ArrayList<>();
    private MemoPhotosAdapter memoPhotosAdapter;
    private ArrayList<String> filePathList = new ArrayList<>();
    private ArrayList<Integer> positionsToBeDeleted = new ArrayList<>();
    private List<Boolean> existingPhotos = new ArrayList<>();
    private Intent intent;
    private RecyclerView photosRecyclerView;
    //  private HashMap<ImageView, Boolean> imageViewBooleanHashMap = new HashMap<ImageView,Boolean>();


    private List<Bitmap> getImageViewList() {
        return imageViewArrayListDetails;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_details);
        intent = getIntent();
        ACTION_MODE = intent.getStringExtra(C.ACTION_MODE);
        detail_photosCount = intent.getExtras().getInt(C.PHOTOS);

        photosRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_details);
        memoID = intent.getExtras().getInt(C.TEXT_ID);

        StaggeredGridLayoutManager photosGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        photosRecyclerView.setLayoutManager(photosGridLayoutManager);
        memoPhotosAdapter = new MemoPhotosAdapter(this, imageViewArrayListDetails, memoID);
        photosRecyclerView.setAdapter(memoPhotosAdapter);

        registerForContextMenu(photosRecyclerView);

        if (ACTION_MODE.equals(C.EDIT) || ACTION_MODE.equals(C.VIEW_MEMO_NOTIFICATION)) {
            EditText textviewTitle = (EditText) findViewById(R.id.title_text_input);
            EditText detailsviewTitle = (EditText) findViewById(R.id.details_text_input);

            oldTitle = intent.getStringExtra(C.INPUT_TITLE);
            oldDetails = intent.getStringExtra(C.INPUT_DETAILS);
            textviewTitle.setText(oldTitle);
            detailsviewTitle.setText(oldDetails);

            int count = 0;
            while (count < detail_photosCount) {
                File fileName = new File("u_" + FileOperation.userID + "_img_" + memoID + "_" + (count++) + ".jpg");

                File filePath = new File(FileOperation.mydir, fileName.toString());
                if (filePath.exists()) {

                    imageViewArrayListDetails.add(FileOperation.loadImageFromStorage(filePath.toString(), this));
                    filePathList.add(filePath.toString());
                    existingPhotos.add(true);
                }
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


                Bitmap yourSelectedImage = BitmapOperation.decodeSampledBitmapFromFile(filePath, 500, 500);
                int width = yourSelectedImage.getWidth();
                int height = yourSelectedImage.getHeight();
                float aspectRatio = (float) height / width;
                float resizedHeight = aspectRatio * 500;
                yourSelectedImage = BitmapOperation.getResizedBitmap(yourSelectedImage, 500, (int) resizedHeight);

                //WANT TO ADD FILEPATH FOR THIS, BUT NO FILEPATH. FIND A WAY!
                imageViewArrayListDetails.add(yourSelectedImage);
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
        if (intent.hasExtra(C.ACTION_MODE) && ACTION_MODE.equals(C.EDIT)) {
            memoID = intent.getExtras().getInt(C.TEXT_ID);
        } else if (intent.hasExtra(C.ACTION_MODE) && ACTION_MODE.equals(C.ADD)) {
            memoID = FileOperation.getMemoTextCountId();
        }

        mImageName = "u_" + FileOperation.userID + "_img_" + memoID + "_" + photoID + ".jpg";

        latestImageName = mImageName;
        mediaFile = new File(FileOperation.mydir, latestImageName);

        return mediaFile;
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
            Intent showReminder = new Intent(this, ReminderActivity.class)
                    .putExtra(C.INPUT_TITLE, oldTitle)
                    .putExtra(C.INPUT_DETAILS, oldDetails)
                    .putExtra(C.TEXT_ID, memoID)
                    .putExtra(C.PHOTOS, intent.getExtras().getInt(C.PHOTOS));
            startActivity(showReminder);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_submit_text_memo) {
            EditText textviewTitle = (EditText) findViewById(R.id.title_text_input);
            EditText detailsviewTitle = (EditText) findViewById(R.id.details_text_input);

            newTitle = textviewTitle.getText().toString();
            newDetails = detailsviewTitle.getText().toString();

            if (!newTitle.isEmpty() || !newDetails.isEmpty()) {

                Intent showMainActivity = new Intent(TextDetailsMemoActivity.this, MainActivity.class)
                        .putExtra(C.INPUT_TITLE, newTitle)
                        .putExtra(C.INPUT_DETAILS, newDetails)
                        .putExtra(C.PHOTOS, detail_photosCount);


                int oldPhotosCount = intent.getExtras().getInt(C.PHOTOS);
                if (ACTION_MODE.equals(C.EDIT) || ACTION_MODE.equals("DELETE")) {
                    Iterator iteratorDeletion = positionsToBeDeleted.iterator();
                    while (iteratorDeletion.hasNext()) {
                        int position = (int) iteratorDeletion.next();

                        if (existingPhotos.get(position) == true) {
                            File file = new File(FileOperation.mydir, filePathList.get(position));
                            FileOperation.deleteIndividualPhotosMemo(filePathList.get(position));
                            filePathList.remove(position);
                        }
                        existingPhotos.remove(position);

                    }
                    int memoID = intent.getExtras().getInt(C.TEXT_ID);

                    FileOperation.replaceSelected(
                            FileOperation.DELIMITER_LINE + FileOperation.DELIMITER_UNIT + memoID + FileOperation.DELIMITER_UNIT + FileOperation.DELIMITER_LINE + "photos=" + oldPhotosCount + FileOperation.DELIMITER_LINE + oldTitle + FileOperation.DELIMITER_LINE + oldDetails + FileOperation.DELIMITER_LINE,
                            FileOperation.DELIMITER_LINE + FileOperation.DELIMITER_UNIT + memoID + FileOperation.DELIMITER_UNIT + FileOperation.DELIMITER_LINE + "photos=" + detail_photosCount + FileOperation.DELIMITER_LINE + newTitle + FileOperation.DELIMITER_LINE + newDetails + FileOperation.DELIMITER_LINE);
                    showMainActivity.putExtra("ACTION_MODE", "EDIT");


                    ListOperation.modifyTextList(memoID, detail_photosCount, oldTitle, oldDetails, newTitle, newDetails);
                } else
                    showMainActivity.putExtra(C.ACTION_MODE, C.ADD);


                Iterator iteratorArrayListImage = imageViewArrayListDetails.iterator();
                Iterator existingPhotosIterator = existingPhotos.iterator();


                while (iteratorArrayListImage.hasNext()) {
                    //If not existing photos (if false) and the arraylist has content :

                    Bitmap bMap = (Bitmap) iteratorArrayListImage.next();
                    if ((Boolean) existingPhotosIterator.next() == false) {
                        storeImage(bMap, oldPhotosCount);
                        oldPhotosCount++;
                    }
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
