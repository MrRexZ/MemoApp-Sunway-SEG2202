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

/**
 * Created by Mr_RexZ on 6/19/2016.
 */
public class BitmapStoringWorkerTask extends AsyncTask<String, Void, Bitmap> {
    private String filePath;
    private int memoID;
    private MemoPhotosAdapter memoPhotosAdapter;
    private TextDetailsMemoActivity textDetailsMemoActivity;
    private String ACTION_MODE;

    public BitmapStoringWorkerTask(MemoPhotosAdapter memoPhotosAdapter, TextDetailsMemoActivity textDetailsMemoActivity, int memoID, String ACTION_MODE) {
        this.memoPhotosAdapter = memoPhotosAdapter;
        this.textDetailsMemoActivity = textDetailsMemoActivity;
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
        storeImage(bitmap, (textDetailsMemoActivity.detail_photosCount));


        textDetailsMemoActivity.imageViewArrayListDetails.add(FileOperation.mydir + File.separator + "u_" + FileOperation.userID + "_img_" + memoID + "_" + textDetailsMemoActivity.detail_photosCount + ".jpg");
        textDetailsMemoActivity.filePathList.add("u_" + FileOperation.userID + "_img_" + memoID + "_" + textDetailsMemoActivity.detail_photosCount + ".jpg");

        textDetailsMemoActivity.detail_photosCount++;

        memoPhotosAdapter.notifyDataSetChanged();

    }


    private void storeImage(Bitmap image, int photoID) {
        File pictureFile = getOutputMediaFile(photoID);
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


    private File getOutputMediaFile(int photoID) {


        //   String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = null;

        int memoID = 0;
        if (textDetailsMemoActivity.intent.hasExtra(C.ACTION_MODE) && ACTION_MODE.equals(C.EDIT)) {
            memoID = textDetailsMemoActivity.intent.getExtras().getInt(C.TEXT_ID);
        } else if (textDetailsMemoActivity.intent.hasExtra(C.ACTION_MODE) && ACTION_MODE.equals(C.ADD)) {
            memoID = FileOperation.getMemoTextCountId();
        }

        mImageName = "u_" + FileOperation.userID + "_img_" + memoID + "_" + photoID + ".jpg";

        mediaFile = new File(FileOperation.mydir, mImageName);

        return mediaFile;
    }

}


