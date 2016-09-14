package com.sunway.android.memoapp.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.sunway.android.memoapp.model.MemoDrawingItem;
import com.sunway.android.memoapp.model.MemoTextItem;
import com.sunway.android.memoapp.model.Reminder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    public static File mydir = MyApplication.getAppContext().getDir("memoapp", Context.MODE_PRIVATE);
    public static int memoCountId = 0;

    public static void replaceSelected(String beforeReplace, String afterReplace) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(mydir, "u_" + userID + ".txt"));
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader file = new BufferedReader(inputStreamReader);
            String line;
            String input = "";

            while ((line = file.readLine()) != null) input += line + FileOperation.LINE_SEPERATOR;
            file.close();
            input = input.replace(beforeReplace,afterReplace);
            FileOutputStream fOut = new FileOutputStream(new File(mydir, "u_" + userID + ".txt"), false);
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
            FileInputStream inputStream = new FileInputStream(new File(mydir, fileName));

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
                        memoCountId = Integer.parseInt(m.group(1));
                    }
                }


                int startIndex = 0;
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
        for (int counterTextMemo = startIndex; counterTextMemo <= memoCountId; counterTextMemo++) {
            String pattern = DELIMITER_LINE + DELIMITER_UNIT + counterTextMemo + DELIMITER_UNIT + DELIMITER_LINE
                    + "photos=(\\d+)" + DELIMITER_LINE
                    + "((?:.)*?)" + DELIMITER_LINE
                    + "((?:.)*?)" + DELIMITER_LINE
                    + "reminder=(?:(\\d+),(\\d+),(\\d+),(\\d+),(\\d+),(\\d+))" + DELIMITER_LINE;
            Pattern r = Pattern.compile(pattern, Pattern.DOTALL);
            Matcher m = r.matcher(processedString);

            if (m.find()) {
                ListOperation.addToList(new MemoTextItem(counterTextMemo,
                        Integer.parseInt(m.group(1)), m.group(2), m.group(3),
                        new Reminder(Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)), Integer.parseInt(m.group(6)), Integer.parseInt(m.group(7)), Integer.parseInt(m.group(8)), Integer.parseInt(m.group(9))),
                        ListOperation.TEXT_MEMO_SORT_ORDER));
            } else {
                pattern = DELIMITER_LINE + DELIMITER_UNIT + (counterTextMemo) + DELIMITER_UNIT + DELIMITER_LINE + "Drawing" + DELIMITER_LINE
                        + "reminder=(?:(\\d+),(\\d+),(\\d+),(\\d+),(\\d+),(\\d+))" + DELIMITER_LINE;
                r = Pattern.compile(pattern, Pattern.DOTALL);
                m = r.matcher(processedString);
                if (m.find()) ListOperation.addToList(new MemoDrawingItem(counterTextMemo,
                        new Reminder(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)), Integer.parseInt(m.group(6))),
                        ListOperation.DRAWING_MEMO_SORT_ORDER));

            }

        }
    }


    public static void writeUserTextMemoFile(String input_title, String input_detail, int num_of_photos, int year, int month, int day, int hour, int minute, int second) throws IOException {

        FileOutputStream fOut = new FileOutputStream(new File(mydir, "u_" + userID + ".txt"), true);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);

        if (memoCountId == 0) {
            outputStreamWriter.append(DELIMITER_LINE);
            outputStreamWriter.append("counter=0");
            outputStreamWriter.append(DELIMITER_LINE);
        }
        outputStreamWriter.append(DELIMITER_LINE);
        outputStreamWriter.append(DELIMITER_UNIT);
        outputStreamWriter.append(String.valueOf(memoCountId++));
        outputStreamWriter.append(DELIMITER_UNIT);
        outputStreamWriter.append(DELIMITER_LINE);
        outputStreamWriter.append("photos=" + num_of_photos);
        outputStreamWriter.append(DELIMITER_LINE);
        outputStreamWriter.append(input_title);
        outputStreamWriter.append(DELIMITER_LINE);
        outputStreamWriter.append(input_detail);
        outputStreamWriter.append(DELIMITER_LINE);
        outputStreamWriter.append("reminder=" + year + "," + month + "," + day + "," + hour + "," + minute + "," + second);
        outputStreamWriter.append(DELIMITER_LINE);
        outputStreamWriter.close();

    }

    public static void writeDrawingMemo(int year, int month, int day, int hour, int minute, int second) throws IOException {


        FileOutputStream fOut = new FileOutputStream(new File(mydir, "u_" + userID + ".txt"), true);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);
        if (memoCountId == 0) {
            outputStreamWriter.append(DELIMITER_LINE);
            outputStreamWriter.append("counter=0");
            outputStreamWriter.append(DELIMITER_LINE);
        }
        outputStreamWriter.append(DELIMITER_LINE);
        outputStreamWriter.append(DELIMITER_UNIT);
        outputStreamWriter.append(String.valueOf(memoCountId++));
        outputStreamWriter.append(DELIMITER_UNIT);
        outputStreamWriter.append(DELIMITER_LINE);
        outputStreamWriter.append("Drawing");
        outputStreamWriter.append(DELIMITER_LINE);
        outputStreamWriter.append("reminder=" + year + "," + month + "," + day + "," + hour + "," + minute + "," + second);
        outputStreamWriter.append(DELIMITER_LINE);
        outputStreamWriter.close();


    }

    public static void deleteTextMemo(int memoID, int photosCount, String input_title, String input_details) {
        Reminder reminder = ListOperation.getMemoItemFromID(memoID).getReminder();
        replaceSelected(FileOperation.DELIMITER_LINE + FileOperation.DELIMITER_UNIT + memoID + FileOperation.DELIMITER_UNIT + FileOperation.DELIMITER_LINE + "photos=" + photosCount +
                        FileOperation.DELIMITER_LINE +
                        input_title + FileOperation.DELIMITER_LINE +
                        input_details + FileOperation.DELIMITER_LINE +
                        "reminder=" + reminder.getYear() + "," + reminder.getMonth() + "," + reminder.getDay() + "," +
                        reminder.getHour() + "," + reminder.getMinute() + "," + reminder.getSecond()
                ,
                "");
    }

    public static void deleteImagesMemo(int memoID, int photosCount) {
        int start = 0;
        while (start <= photosCount) {
            File f0 = new File(mydir, "u_" + FileOperation.userID + "_img_" + memoID + "_" + (start++) + ".jpg");
            f0.delete();
        }
    }

    public static void deleteDrawingMemo(int memoID) {
        Reminder reminder = ListOperation.getMemoItemFromID(memoID).getReminder();
        replaceSelected(DELIMITER_LINE + DELIMITER_UNIT + (memoID) + DELIMITER_UNIT + DELIMITER_LINE + "Drawing" + FileOperation.DELIMITER_LINE +
                        "reminder=" + reminder.getYear() + "," + reminder.getMonth() + "," + reminder.getDay() + "," +
                        reminder.getHour() + "," + reminder.getMinute() + "," + reminder.getSecond()
                ,
                "");
        File f0 = new File(mydir, "u_" + userID + "_drawing_" + memoID + ".jpg");
        f0.delete();

    }

    public static void deleteIndividualPhotosMemo(String filePath) {
        File f0 = new File(filePath);
        f0.delete();

    }

    public static void deleteTempFile() {
        if (mydir.isDirectory()) {
            String[] children = mydir.list();
            for (int i = 0; i < children.length; i++) {
                new File(mydir, children[i]).delete();
            }
        }


        Toast.makeText(MyApplication.getAppContext(), "Temp file deleted",
                Toast.LENGTH_LONG).show();


    }

    public static int getMemoTextCountId() {
        return memoCountId;
    }


    public static Bitmap loadImageFromStorage(String filePath, Activity activity) {

        try {
            File f = new File(filePath);
            Bitmap b = BitmapOperation.decodeSampledBitmapFromFile(filePath, 500, 500);


            int width = b.getWidth();
            int height = b.getHeight();
            float aspectRatio = (float) height / width;
            float resizedHeight = aspectRatio * 500;
            b = BitmapOperation.getResizedBitmap(b, 500, (int) resizedHeight);


            return b;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
