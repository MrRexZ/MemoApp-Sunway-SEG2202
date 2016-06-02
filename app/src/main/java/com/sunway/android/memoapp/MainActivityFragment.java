package com.sunway.android.memoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mr_RexZ on 5/28/2016.
 */
public class MainActivityFragment extends Fragment {

    private StaggeredGridLayoutManager _sGridLayoutManager;
    private List<MemoItem> listViewItems = new ArrayList<>();
    private String input_title=null;
    private String input_details=null;
    private static int memoTextCountId=2;
    private static int userID=1;
    private  Matcher m;
    private  final String DELIMITER= Character.toString((char) 31);
    private final String LINE_SEPERATOR = System.getProperty("line.separator");
    private View rootView;

    private List<MemoItem> getListItemData() throws IOException {
        return listViewItems;
    }

    public MainActivityFragment() {
    }

    private void writeFile() {
        try {
            FileOutputStream fOut = getActivity().openFileOutput("output.txt", Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);
            outputStreamWriter.append(DELIMITER);
            outputStreamWriter.append("0");
            outputStreamWriter.append(LINE_SEPERATOR);
            outputStreamWriter.append("Title1");
            outputStreamWriter.append(LINE_SEPERATOR);
            outputStreamWriter.append("Text1");
            outputStreamWriter.append(LINE_SEPERATOR);
            outputStreamWriter.append("Text1");
            outputStreamWriter.append(LINE_SEPERATOR);
            outputStreamWriter.append(LINE_SEPERATOR);
            outputStreamWriter.append(DELIMITER);
            outputStreamWriter.append(DELIMITER);
            outputStreamWriter.append("1");
            outputStreamWriter.append(LINE_SEPERATOR);
            outputStreamWriter.append("Title2");
            outputStreamWriter.append(LINE_SEPERATOR);
            outputStreamWriter.append("Text2");
            outputStreamWriter.append(LINE_SEPERATOR);
            outputStreamWriter.append("Text2");
            outputStreamWriter.append(LINE_SEPERATOR);
            outputStreamWriter.append(LINE_SEPERATOR);
            outputStreamWriter.append(DELIMITER);
            outputStreamWriter.flush();
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("writeFile method", "File write failed: " + e.toString());
        }
    }

    private void writeUserFile(int identifierTextMemo) throws IOException {

            FileOutputStream fOut = getActivity().openFileOutput("u_"+userID+".txt", Context.MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);

        if (memoTextCountId==2) {
            outputStreamWriter.append(DELIMITER);
            outputStreamWriter.append("counter=2");
            outputStreamWriter.append(DELIMITER);
            outputStreamWriter.append(LINE_SEPERATOR);
            outputStreamWriter.append(LINE_SEPERATOR);
        }

            outputStreamWriter.append(DELIMITER);
            outputStreamWriter.append(String.valueOf(identifierTextMemo));
            outputStreamWriter.append(LINE_SEPERATOR);
            outputStreamWriter.append(input_title);
            outputStreamWriter.append(LINE_SEPERATOR);
            outputStreamWriter.append(input_details);
            outputStreamWriter.append(LINE_SEPERATOR);
            outputStreamWriter.append(LINE_SEPERATOR);
            outputStreamWriter.append(DELIMITER);
            outputStreamWriter.close();

        }



    public void replaceSelected() {
        try {
            InputStream inputStream = getActivity().openFileInput("u_"+userID+".txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader file = new BufferedReader(inputStreamReader);
            String line;
            String input = "";

            while ((line = file.readLine()) != null) input += line + LINE_SEPERATOR;
            file.close();
            System.out.println("HERE ARE THE REPLACED TEXT: " + input); // check that it's inputted right
            input = input.replace(DELIMITER+"counter="+ (memoTextCountId-1) +DELIMITER, DELIMITER+"counter="+ (memoTextCountId)+DELIMITER);

            FileOutputStream fOut = getActivity().openFileOutput("u_"+userID+".txt", Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);
            outputStreamWriter.write(input);
            outputStreamWriter.flush();
            fOut.close();

        } catch (Exception e) {
            System.out.println("Problem reading file.");
        }
    }

    private void readFile(String fileName,int startIndex) {
        String ret;

        try {
            InputStream inputStream = getActivity().openFileInput(fileName);

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
                ret = stringBuilder.toString();


                if (fileName.equals("u_"+userID+".txt")) {
                    String pattern = DELIMITER+"counter=(\\d+)"+DELIMITER;
                    Pattern r=Pattern.compile(pattern);
                    m = r.matcher(ret);
                    if (m.find()) {
                        memoTextCountId=Integer.parseInt(m.group(1));
                        System.out.println("Found : " + m.group(1));
                    }
                }

                for (int counterTextMemo=startIndex;counterTextMemo<=memoTextCountId;counterTextMemo++){
                    String pattern;
                    if (startIndex<2)
                 pattern = DELIMITER + (counterTextMemo) +"\\s(.*)\\s((?:.*\\s)+?)\\s"+DELIMITER;
                    else pattern = DELIMITER + (counterTextMemo - 1 ) +"\\s(.*)\\s((?:.*\\s)+?)\\s"+DELIMITER;


                 System.out.println(pattern);
                Pattern r = Pattern.compile(pattern, Pattern.MULTILINE);
                m = r.matcher(ret);
                if (m.find( )) {
                    listViewItems.add(new MemoItem(m.group(1),m.group(2)));
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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState==null) {
            writeFile();
            readFile("output.txt", 0);
            //replace the second argument to 0 when starting the app with no precustomised file
            readFile("u_"+userID+".txt",1);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.upper_main_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.rootView = inflater.inflate(R.layout.fragment_main, container, false);

       inflateLayout();

        Toolbar upper_toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_upper);
        ((AppCompatActivity)getActivity()).setSupportActionBar(upper_toolbar);
        upper_toolbar.setTitle("Memo App");

        Toolbar bottom_toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_bottom);
        bottom_toolbar.inflateMenu(R.menu.bottom_main_menu);
        bottom_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                int id = arg0.getItemId();
                if (id == R.id.action_open_create_text_memo) {
                    Intent showDetail = new Intent(getActivity(), TextDetailsMemoActivity.class);
                    startActivity(showDetail);
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }

    public void passInformation(String title, String details) {
        input_title=title;
        input_details=details;
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((input_details!=null && input_title!=null) && (!input_details.isEmpty() || !input_title.isEmpty())) {

        try {
            writeUserFile(memoTextCountId);
        }
        catch (Exception e) {
        System.err.println("Cannot write to file: " + e.getMessage());
        }
            memoTextCountId++;
            replaceSelected();
            readFile("u_"+userID+".txt",memoTextCountId);
            inflateLayout();
        }
    }

    private void inflateLayout() {

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        _sGridLayoutManager = new StaggeredGridLayoutManager( 3 , StaggeredGridLayoutManager.VERTICAL );
        recyclerView.setLayoutManager( _sGridLayoutManager );

        List<MemoItem> sList = null;
        try {
            sList = getListItemData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MemoItemAdapter rcAdapter = new MemoItemAdapter(
                getActivity(), sList);
        recyclerView.setAdapter(rcAdapter);
    }

}
