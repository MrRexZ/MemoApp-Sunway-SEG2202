package com.sunway.android.memoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

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
        Toolbar bottomw_toolbar = (Toolbar) findViewById(R.id.toolbar_bottom);
        bottomw_toolbar.inflateMenu(R.menu.bottom_textdetailsmemo_menu);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upper_textdetailsmemo_menu, menu);
        return true;
    }
}
