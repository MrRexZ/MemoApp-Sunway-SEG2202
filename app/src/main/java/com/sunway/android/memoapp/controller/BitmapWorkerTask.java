package com.sunway.android.memoapp.controller;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.sunway.android.memoapp.util.BitmapOperation;

import java.lang.ref.WeakReference;

/**
 * Created by Mr_RexZ on 6/18/2016.
 */
public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private String filePath = null;

    public BitmapWorkerTask(ImageView imageView) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(String... params) {
        filePath = params[0];
        return BitmapOperation.decodeSampledBitmapFromFile(filePath, 500, 500);
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}