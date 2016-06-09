package com.sunway.android.memoapp.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sunway.android.memoapp.model.MemoTextItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mr_RexZ on 6/3/2016.
 */
public class FileOperation {

    public final static String DELIMITER_LINE       = Character.toString((char) 30);
    public final static String DELIMITER_UNIT       = Character.toString((char) 31);
    public static final String LINE_SEPERATOR  = System.getProperty("line.separator");
    public static int userID = 1;
    public static Context appContext;
    private static int memoTextCountId = 0;

    public static void passAppContext(Context context) {
        appContext = context;
    }

    public static void replaceSelected(String beforeReplace, String afterReplace) {
        try {
            InputStream inputStream = appContext.openFileInput("u_"+userID+".txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader file = new BufferedReader(inputStreamReader);
            String line;
            String input = "";

            System.out.println("called!");
            while ((line = file.readLine()) != null) input += line + FileOperation.LINE_SEPERATOR;
            file.close();
            input = input.replace(beforeReplace,afterReplace);
            FileOutputStream fOut = appContext.openFileOutput("u_"+userID+".txt", Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);
            outputStreamWriter.write(input);
            outputStreamWriter.flush();
            fOut.close();

        } catch (Exception e) {
            System.out.println("Problem reading file.");
        }
    }

    public static void readFile(String mode, String fileName) {
        String processedString;
        Matcher m;

        try {
            InputStream inputStream = appContext.openFileInput(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                    stringBuilder.append(LINE_SEPERATOR);
                }
                inputStream.close();
                processedString = stringBuilder.toString();


                if (fileName.equals("u_"+userID+".txt")) {
                    String pattern = DELIMITER_LINE+"counter=(\\d+)"+DELIMITER_LINE;
                    Pattern r=Pattern.compile(pattern);
                    m = r.matcher(processedString);
                    if (m.find()) {
                        memoTextCountId=Integer.parseInt(m.group(1));
                        System.out.println("the ID IN FIND : " + m.group(1));

                    }
                }


                int startIndex = 0;
                if (mode.equals("START")) startIndex = 0;
                else if (mode.equals("ADD")) startIndex = (memoTextCountId - 1);
                else if (mode.equals("EDIT") || mode.equals("DELETE")) startIndex = memoTextCountId;

                searchAndAdd(startIndex, processedString);

            }
        }
        catch (FileNotFoundException e) {
            Log.e("readFile section", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("readFile section", "Can not read file: " + e.toString());
        }

    }

    private static void searchAndAdd(int startIndex, String processedString) {
        for (int counterTextMemo = startIndex; counterTextMemo <= memoTextCountId; counterTextMemo++) {
            String pattern = DELIMITER_LINE + DELIMITER_UNIT + (counterTextMemo) + DELIMITER_UNIT + DELIMITER_LINE + "photos=(\\d+)" + DELIMITER_LINE + "((?:.)*?)" + DELIMITER_LINE + "((?:.)*?)" + DELIMITER_LINE;


            System.out.println(pattern);
            Pattern r = Pattern.compile(pattern, Pattern.DOTALL);
            Matcher m = r.matcher(processedString);

            if (m.find()) {
                ListOperation.addToList(new MemoTextItem(counterTextMemo, Integer.parseInt(m.group(1)), m.group(2), m.group(3)));
            } else {
                System.out.println("NO MATCH");
            }

        }
    }


    public static void writeUserTextMemoFile(String input_title, String input_detail, int num_of_photos) throws IOException {


        FileOutputStream fOut = appContext.openFileOutput("u_"+userID+".txt", Context.MODE_APPEND);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);

        if (memoTextCountId==0) {
            outputStreamWriter.append(DELIMITER_LINE);
            outputStreamWriter.append("counter=0");
            outputStreamWriter.append(DELIMITER_LINE);
        }

        outputStreamWriter.append(DELIMITER_LINE);
        outputStreamWriter.append(DELIMITER_UNIT);
        outputStreamWriter.append(String.valueOf(memoTextCountId));
        outputStreamWriter.append(DELIMITER_UNIT);
        outputStreamWriter.append(DELIMITER_LINE);
        outputStreamWriter.append("photos=" + num_of_photos);
        outputStreamWriter.append(DELIMITER_LINE);
        outputStreamWriter.append(input_title);
        outputStreamWriter.append(DELIMITER_LINE);
        outputStreamWriter.append(input_detail);
        outputStreamWriter.append(DELIMITER_LINE);
        outputStreamWriter.close();


        System.out.println("the ID before : " + memoTextCountId);
        memoTextCountId = memoTextCountId + 1;

        System.out.println("the ID after : " + memoTextCountId);

    }

    public static void deleteTextMemo(String memoID, int photosCount, String input_title, String input_details) {
        replaceSelected(FileOperation.DELIMITER_LINE + FileOperation.DELIMITER_UNIT + memoID + FileOperation.DELIMITER_UNIT + FileOperation.DELIMITER_LINE + "photos=" + photosCount + FileOperation.DELIMITER_LINE + input_title + FileOperation.DELIMITER_LINE + input_details + FileOperation.DELIMITER_LINE,
                "");


    }

    public static void deleteImagesMemo(String memoID, int photosCount) {
        int start = 0;
        while (start <= photosCount) {
            String dir = appContext.getFilesDir().getAbsolutePath();
            File f0 = new File(dir, "u_" + FileOperation.userID + "_img_" + memoID + "_" + (start++) + ".jpg");
            f0.delete();
        }


    }

    public static void deleteTempFile() {
        String dir = appContext.getFilesDir().getAbsolutePath();

        int start = 0;
        while (start < memoTextCountId) {

            deleteImagesMemo(Integer.toString(start), ListOperation.getIndividualMemoItem(start).getPhotosCount());
            start++;
        }
        File f0 = new File(dir, "u_" + FileOperation.userID + ".txt");

        f0.delete();

        Toast.makeText(appContext, "Temp file deleted",
                Toast.LENGTH_LONG).show();


    }

    public static int getMemoTextCountId() {
        return memoTextCountId;
    }


    public static void loadImageFromStorage(String path, String filename, LinearLayout linearLayout, int imageSize, Activity activity) {

        try {
            File f = new File(path, filename);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img = new ImageView(activity);
            img.setImageBitmap(b);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageSize, imageSize);
            img.setLayoutParams(layoutParams);
            linearLayout.addView(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
