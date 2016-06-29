package com.sunway.android.memoapp.controller;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.sunway.android.memoapp.model.MemoPhotosAdapter;
import com.sunway.android.memoapp.util.BitmapOperation;
import com.sunway.android.memoapp.util.C;
import com.sunway.android.memoapp.util.FileOperation;
import com.sunway.android.memoapp.view.TextDetailsMemoActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by Mr_RexZ on 6/19/2016.
 */
public class BitmapStoringWorkerTask extends AsyncTask<String, Void, Bitmap> {
    private String filePath;
    private int memoID;
    private MemoPhotosAdapter memoPhotosAdapter;
    private String ACTION_MODE;
    private WeakReference<TextDetailsMemoActivity> weakTextDetailsMemoActivity;

    public BitmapStoringWorkerTask(MemoPhotosAdapter memoPhotosAdapter, TextDetailsMemoActivity textDetailsMemoActivity, int memoID, String ACTION_MODE) {
        this.memoPhotosAdapter = memoPhotosAdapter;
        weakTextDetailsMemoActivity = new WeakReference<TextDetailsMemoActivity>(textDetailsMemoActivity);
        this.memoID = memoID;
        this.ACTION_MODE = ACTION_MODE;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        filePath = params[0];

        Bitmap yourSelectedImage = BitmapOperation.decodeSampledBitmapFromFile(filePath, 500, 500);
        int width = yourSelectedImage.getWidth();
        int height = yourSelectedImage.getHeight();
        float aspectRatio = (float) height / width;
        float resizedHeight = aspectRatio * 500;

        return BitmapOperation.getResizedBitmap(yourSelectedImage, 500, (int) resizedHeight);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        TextDetailsMemoActivity textDetailsMemoActivityAsync = weakTextDetailsMemoActivity.get();
        if (textDetailsMemoActivityAsync != null) {
            storeImage(bitmap, textDetailsMemoActivityAsync.detail_photosCount, textDetailsMemoActivityAsync);

            File fileName = new File("u_" + FileOperation.userID + "_img_" + memoID + "_" + textDetailsMemoActivityAsync.detail_photosCount + ".jpg");
        File filePath = new File(FileOperation.mydir, fileName.toString());
            textDetailsMemoActivityAsync.imageViewArrayListDetails.add(filePath.toString());
            textDetailsMemoActivityAsync.existingImage.add(filePath.toString());
            textDetailsMemoActivityAsync.detail_photosCount++;

        memoPhotosAdapter.notifyDataSetChanged();
        }

    }


    private void storeImage(Bitmap image, int photoID, TextDetailsMemoActivity textDetailsMemoActivityAsync) {
        File pictureFile = getOutputMediaFile(photoID, textDetailsMemoActivityAsync);
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);

            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    private File getOutputMediaFile(int photoID, TextDetailsMemoActivity textDetailsMemoActivityAsync) {

        File mediaFile;
        String mImageName = null;

        int memoID = 0;
        if (textDetailsMemoActivityAsync.intent.hasExtra(C.ACTION_MODE) && ACTION_MODE.equals(C.EDIT)) {
            memoID = textDetailsMemoActivityAsync.intent.getExtras().getInt(C.MEMO_ID);
        } else if (textDetailsMemoActivityAsync.intent.hasExtra(C.ACTION_MODE) && ACTION_MODE.equals(C.ADD)) {
            memoID = FileOperation.getMemoTextCountId();
        }
        mImageName = "u_" + FileOperation.userID + "_img_" + memoID + "_" + photoID + ".jpg";
        mediaFile = new File(FileOperation.mydir, mImageName);

        return mediaFile;
    }


}


