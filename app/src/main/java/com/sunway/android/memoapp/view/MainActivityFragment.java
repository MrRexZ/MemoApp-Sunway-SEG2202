package com.sunway.android.memoapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.model.MemoItem;
import com.sunway.android.memoapp.model.MemoItemAdapter;
import com.sunway.android.memoapp.util.FileOperation;
import com.sunway.android.memoapp.util.ListOperation;

import java.util.List;

/**
 * Created by Mr_RexZ on 5/28/2016.
 */
public class MainActivityFragment extends Fragment {

    private StaggeredGridLayoutManager _sGridLayoutManager;
    private String input_title=null;
    private String input_details=null;
    private String mode=null;
    private int photosCount = 0;
    private View rootView;
    private RecyclerView recyclerView;
    private MemoItemAdapter rcAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState==null) {
            //writeFile();

            FileOperation.passAppContext(getActivity().getApplicationContext());
            ListOperation.clearListView();
            FileOperation.readFile("START", "u_" + FileOperation.userID + ".txt");

        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.upper_main_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete_temp_file) {
            FileOperation.deleteTempFile();
        }
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
                    Intent showDetail = new Intent(getActivity(), TextDetailsMemoActivity.class)
                            .putExtra("ACTION_MODE", "ADD")
                            .putExtra("PHOTOS", 0)
                            .putExtra("TEXTID", FileOperation.getMemoTextCountId());
                    startActivity(showDetail);
                    return true;
                } else if (id == R.id.action_create_drawing_memo) {
                }
                return false;
            }
        });

        return rootView;
    }

    public void passInformation(String title, String details, String mode, int photosCount) {
        input_title=title;
        input_details=details;
        this.mode=mode;
        this.photosCount = photosCount;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (((input_details != null && input_title != null) && (!input_details.isEmpty() || !input_title.isEmpty()))
                || (mode != null && input_details.isEmpty() && input_title.isEmpty() && mode.equals("EDIT"))) {

            if (mode.equals("ADD")) {
                try {

                    FileOperation.writeUserTextMemoFile(input_title, input_details, photosCount);

                } catch (Exception e) {
                    System.err.println("can write NOT " + e.getMessage());
                }

                System.out.println("the ID before add :" + ((FileOperation.getMemoTextCountId()) - 1));
                System.out.println("the ID after add :" + ((FileOperation.getMemoTextCountId())));
                FileOperation.replaceSelected(
                        FileOperation.DELIMITER_LINE + "counter=" + ((FileOperation.getMemoTextCountId()) - 1) + FileOperation.DELIMITER_LINE,
                        FileOperation.DELIMITER_LINE+"counter="+ ((FileOperation.getMemoTextCountId())) +FileOperation.DELIMITER_LINE);
                System.out.println("the ID : " + FileOperation.getMemoTextCountId());
            }

            FileOperation.readFile(mode, "u_" + FileOperation.userID + ".txt");
            System.out.println("the ID after readfile : " + FileOperation.getMemoTextCountId());

            rcAdapter.notifyDataSetChanged();
            //inflateLayout();
            input_title = "";
            input_details = "";


        }

    }


    private void inflateLayout() {

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        _sGridLayoutManager = new StaggeredGridLayoutManager( 3 , StaggeredGridLayoutManager.VERTICAL );
        recyclerView.setLayoutManager( _sGridLayoutManager );

        List<MemoItem> sList;
        sList = ListOperation.getListViewItems();

        rcAdapter = new MemoItemAdapter(
                getActivity(), sList);
        recyclerView.setAdapter(rcAdapter);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerForContextMenu(recyclerView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.floating_context_memoitem_long_click, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        int position =rcAdapter.getPosition();
        System.out.println("THE POSITION IS : " + position);
        MemoItem memoItem = ListOperation.getIndividualMemoItem(position);
        switch (item.getItemId()) {
            case (R.id.action_delete_memo): {

                FileOperation.deleteTextMemo(memoItem.getMemoID(), memoItem.getPhotosCount(), memoItem.getTitle(), memoItem.getContent());
                FileOperation.deleteImagesMemo(memoItem.getMemoID(), memoItem.getPhotosCount());

                ListOperation.deleteList(memoItem.getMemoID(), memoItem.getTitle(), memoItem.getContent());
                mode = "DELETE";
                FileOperation.readFile(mode, "u_" + FileOperation.userID + ".txt");

                //inflateLayout();
                rcAdapter.notifyDataSetChanged();
            }

        }
        return super.onContextItemSelected(item);
    }
}
