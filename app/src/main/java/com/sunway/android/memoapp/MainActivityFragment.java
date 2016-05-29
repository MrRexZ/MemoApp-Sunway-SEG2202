package com.sunway.android.memoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr_RexZ on 5/28/2016.
 */
public class MainActivityFragment extends Fragment {

    private MemoItemAdapter memoItemAdapter;
    private StaggeredGridLayoutManager _sGridLayoutManager;

    private List<MemoItem> getListItemData() {
    List<MemoItem> listViewItems = new ArrayList<MemoItem>();




        listViewItems.add(new MemoItem("Task1 : ", "Revamp the UI design (change color and implement background image and theme)."));
        listViewItems.add(new MemoItem("Task2 :"," Replace the icon in both toolbars in every activity"));
        listViewItems.add(new MemoItem("Task3:" , "Save the output memo in either File format"));
        listViewItems.add(new MemoItem("Task4 :"," Think of how to store a good file structure in NoSQL for Firebase implementation and the filesharing logic by using either Real Time Database API or  Storage API or both"));
        listViewItems.add(new MemoItem("Task5 :"," Implement the Firebase Storage API to upload all content, and the reference for each download link is kept in Firebase NoSQL RealTimeDatabase to the respective users"));
        listViewItems.add(new MemoItem("Task6 :"," Create account login, account registration feature, and account tagging"));
        listViewItems.add( new MemoItem("Task7 :"," Relax!"));

                    return listViewItems;
    }

    public MainActivityFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        _sGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(_sGridLayoutManager);

        List<MemoItem> sList = getListItemData();

        MemoItemAdapter rcAdapter = new MemoItemAdapter(
                getActivity(), sList);
        recyclerView.setAdapter(rcAdapter);

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
}
