package com.sunway.android.memoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * Created by Mr_RexZ on 5/28/2016.
 */
public class TextDetailsMemoActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_details);
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
                    Intent showMainActivity = new Intent(TextDetailsMemoActivity.this, MainActivity.class)
                            .putExtra("TITLE",textviewTitle.getText().toString())
                            .putExtra("DETAILS",detailsviewTitle.getText().toString());
                  //  showMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                   // MainActivityFragment.listViewItems.add(new MemoItem(textviewTitle.getText().toString(),detailsviewTitle.getText().toString()));
                   // showMainActivity.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
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
