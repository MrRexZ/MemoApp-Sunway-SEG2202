package com.sunway.android.memoapp.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.sunway.android.memoapp.controller.BitmapReadingWorkerTask;

import java.lang.ref.WeakReference;


/**
 * Created by Mr_RexZ on 6/18/2016.
 */
public class BitmapOperation {


    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {


        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }


    public static Bitmap decodeSampledBitmapFromFile(String file,
                                                     int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;


            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public static boolean cancelPotentialWork(String filePath, ImageView imageView) {
        final BitmapReadingWorkerTask bitmapReadingWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapReadingWorkerTask != null) {
            final String bitmapData = bitmapReadingWorkerTask.filePath;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData == null || !bitmapData.equals(filePath)) {
                // Cancel previous task
                bitmapReadingWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private static BitmapReadingWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    public static void loadBitmap(String filePath, ImageView imageView, Resources resources, Bitmap bitmapHolder, String displayState) {
        if (BitmapOperation.cancelPotentialWork(filePath, imageView)) {

            final BitmapReadingWorkerTask task = new BitmapReadingWorkerTask(imageView);
            final BitmapOperation.AsyncDrawable asyncDrawable =
                    new BitmapOperation.AsyncDrawable(resources, bitmapHolder, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(filePath, displayState);
        }
    }

    static public class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapReadingWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapReadingWorkerTask bitmapReadingWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<BitmapReadingWorkerTask>(bitmapReadingWorkerTask);
        }

        public BitmapReadingWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }
}
