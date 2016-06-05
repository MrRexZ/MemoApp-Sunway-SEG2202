package com.sunway.android.memoapp.util;

import android.content.Context;
import android.util.Log;

import com.sunway.android.memoapp.model.MemoTextItem;

import java.io.BufferedReader;
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

    public  static int userID=1;
    private  static int memoTextCountId=0;
    private static String input_title=null;
    private  static String input_detail=null;
    public final static String DELIMITER_LINE       = Character.toString((char) 30);
    public final static String DELIMITER_UNIT       = Character.toString((char) 31);

    public static final String LINE_SEPERATOR  = System.getProperty("line.separator");
    private static Context appContext;


    public static void passAppContext(Context context) {
        appContext=context;

    }


    public static void replaceSelected(String beforeReplace, String afterReplace) {
        try {
            InputStream inputStream = appContext.openFileInput("u_"+userID+".txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader file = new BufferedReader(inputStreamReader);
            String line;
            String input = "";


            while ((line = file.readLine()) != null) input += line + FileOperation.LINE_SEPERATOR;
            file.close();
            System.out.println("HERE ARE THE REPLACED TEXT: " + input); // check that it's inputted right
            input = input.replace(beforeReplace,afterReplace);
String output=input;
            FileOutputStream fOut = appContext.openFileOutput("u_"+userID+".txt", Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);
            outputStreamWriter.write(input);
            outputStreamWriter.flush();
            fOut.close();

        } catch (Exception e) {
            System.out.println("Problem reading file.");
        }
    }

    public static void readFile(String mode, String fileName,int startIndex) {
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
                        System.out.println("Found : " + m.group(1));
                    }
                }

                for (int counterTextMemo=startIndex;counterTextMemo<=memoTextCountId;counterTextMemo++){
                    String pattern = DELIMITER_LINE +DELIMITER_UNIT+(counterTextMemo)+DELIMITER_UNIT +"((?:.+?\\s*?)+?)"+DELIMITER_LINE+"(?:\\s*?)((?:.+?\\s*?)+?)"+DELIMITER_LINE;


                    System.out.println(pattern);
                    Pattern r = Pattern.compile(pattern, Pattern.MULTILINE);
                    m = r.matcher(processedString);


                    if (m.find( ) && mode.equals("ADD")) {
                        System.out.println("REGEX : "+ m.group(1) + "and"+ m.group(2));
                        ListOperation.addToList(new MemoTextItem(counterTextMemo,m.group(1),m.group(2)));
                    } else {
                        System.out.println("NO MATCH");
                    }

                }
            }
        }
        catch (FileNotFoundException e) {
            Log.e("readFile section", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("readFile section", "Can not read file: " + e.toString());
        }

    }


    public static void writeUserFile(int identifierTextMemo) throws IOException {

        FileOutputStream fOut = appContext.openFileOutput("u_"+userID+".txt", Context.MODE_APPEND);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);

        if (memoTextCountId==0) {
            outputStreamWriter.append(DELIMITER_LINE);
            outputStreamWriter.append("counter=0");
            outputStreamWriter.append(DELIMITER_LINE);
        }

        outputStreamWriter.append(DELIMITER_LINE);
        outputStreamWriter.append(DELIMITER_UNIT);
        outputStreamWriter.append(String.valueOf(identifierTextMemo));
        outputStreamWriter.append(DELIMITER_UNIT);
        outputStreamWriter.append(input_title);
        outputStreamWriter.append(DELIMITER_LINE);
        outputStreamWriter.append(input_detail);
        outputStreamWriter.append(DELIMITER_LINE);
        outputStreamWriter.close();

        memoTextCountId++;

    }

    public static void passToFileOp(String ipt_title,String ipt_details) {
        input_title=ipt_title;
        input_detail=ipt_details;
    }

    public String getInput_title(){
        return input_title;
    }

    public String getInput_detail(){
        return input_detail;
    }

    public static int getMemoTextCountId() {
        return memoTextCountId;
    }
}
