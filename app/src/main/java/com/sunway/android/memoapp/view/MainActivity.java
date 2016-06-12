package com.sunway.android.memoapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sunway.android.memoapp.R;

import java.io.Serializable;


public class MainActivity extends AppCompatActivity implements Serializable {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        MainActivityFragment mainActivityFragment= (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        mainActivityFragment.passInformation(intent.getExtras().getString("TITLE"),
                                            intent.getExtras().getString("DETAILS"),
                intent.getExtras().getString("ACTION_MODE"),
                intent.getExtras().getInt("PHOTOS"));
        // FileOperation.passToFileOp(intent.getExtras().getString("TITLE"),intent.getExtras().getString("DETAILS"));
    }
}
