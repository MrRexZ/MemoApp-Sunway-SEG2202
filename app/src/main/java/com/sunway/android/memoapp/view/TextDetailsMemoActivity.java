package com.sunway.android.memoapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.util.FileOperation;
import com.sunway.android.memoapp.util.ListOperation;

/**
 * Created by Mr_RexZ on 5/28/2016.
 */
public class TextDetailsMemoActivity extends AppCompatActivity {


    private String oldTitle;
    private String oldDetails;
    private String newTitle;
    private String newDetails;
    private String ACTION_MODE;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_details);
        ACTION_MODE=getIntent().getStringExtra("ACTION_MODE");
        if (getIntent().hasExtra("ACTION_MODE") && ACTION_MODE.equals("EDIT")) {
            EditText textviewTitle = (EditText) findViewById(R.id.title_text_input);
            EditText detailsviewTitle = (EditText) findViewById(R.id.details_text_input);

            oldTitle=getIntent().getStringExtra("TITLE");
            oldDetails=getIntent().getStringExtra("DETAILS");
            textviewTitle.setText(oldTitle);
            detailsviewTitle.setText(oldDetails);
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
                            .putExtra("DETAILS",newDetails);

                    if (getIntent().hasExtra("ACTION_MODE") && ACTION_MODE.equals("EDIT")){
                        String memoID=getIntent().getStringExtra("TEXTID");

                        FileOperation.replaceSelected(FileOperation.DELIMITER_UNIT+memoID+FileOperation.DELIMITER_UNIT+FileOperation.LINE_SEPERATOR+oldTitle+oldDetails ,
                                FileOperation.DELIMITER_UNIT+memoID+FileOperation.DELIMITER_UNIT+FileOperation.LINE_SEPERATOR+newTitle+newDetails);

                        showMainActivity.putExtra("ACTION_MODE","EDIT");


                       ListOperation.modifyList(memoID,oldTitle,oldDetails,newTitle,newDetails);
                    }
                    else
                        showMainActivity.putExtra("ACTION_MODE","ADD");

                    startActivity(showMainActivity);
                    return true;
                }
                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upper_textdetailsmemo_menu, menu);
        return true;
    }

}
