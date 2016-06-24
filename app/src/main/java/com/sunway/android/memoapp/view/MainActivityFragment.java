package com.sunway.android.memoapp.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import com.sunway.android.memoapp.controller.MainActivityFragmentTouchListener;
import com.sunway.android.memoapp.model.MemoDrawingItem;
import com.sunway.android.memoapp.model.MemoItem;
import com.sunway.android.memoapp.model.MemoItemAdapter;
import com.sunway.android.memoapp.model.MemoTextItem;
import com.sunway.android.memoapp.util.C;
import com.sunway.android.memoapp.util.FileOperation;
import com.sunway.android.memoapp.util.ListOperation;
import com.sunway.android.memoapp.util.MyApplication;

import java.io.File;

/**
 * Created by Mr_RexZ on 5/28/2016.
 */
public class MainActivityFragment extends Fragment implements SearchView.OnQueryTextListener {

    private StaggeredGridLayoutManager _sGridLayoutManager;
    private String input_title=null;
    private String input_details=null;
    private String mode = "START";
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
            ListOperation.clearListView();
            File mydir = MyApplication.getAppContext().getDir("memoapp", Context.MODE_PRIVATE);
            if (!mydir.exists())
                mydir.mkdirs();
            FileOperation.readFile("START", "u_" + FileOperation.userID + ".txt");


        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.upper_main_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.memo_search_view);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);

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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        rcAdapter.getFilter().filter(newText);
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
                            .putExtra(C.INPUT_TITLE, "")
                            .putExtra(C.INPUT_DETAILS, "")
                            .putExtra(C.ACTION_MODE, C.ADD)
                            .putExtra(C.PHOTOS, 0)
                            .putExtra(C.MEMO_ID, FileOperation.getMemoTextCountId());
                    startActivity(showDetail);
                    return true;
                } else if (id == R.id.action_create_drawing_memo) {
                    Intent showDrawing = new Intent(getActivity(), DrawingMemoActivity.class)
                            .putExtra(C.ACTION_MODE, C.ADDDRAWING)
                            .putExtra(C.MEMO_ID, FileOperation.getMemoTextCountId());
                    startActivity(showDrawing);
                    return true;
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

    }

    @Override
    public void onResume() {
        super.onResume();

        refreshAdapter();


    }


    private void inflateLayout() {

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        _sGridLayoutManager = new StaggeredGridLayoutManager( 3 , StaggeredGridLayoutManager.VERTICAL );
        recyclerView.setLayoutManager( _sGridLayoutManager );

        rcAdapter = new MemoItemAdapter(
                getActivity(), C.MAIN_ACTIVITY_FRAGMENT_DISPLAY);
        recyclerView.setAdapter(rcAdapter);


        recyclerView.addOnItemTouchListener(new MainActivityFragmentTouchListener(this, rcAdapter));

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
        MemoItem memoItem = rcAdapter.nMemoList.get(position);

        if (memoItem instanceof MemoTextItem) {
            MemoTextItem memoTextItem = (MemoTextItem) memoItem;
            FileOperation.deleteTextMemo(memoTextItem.getMemoID(), memoTextItem.getPhotosCount(), memoTextItem.getTitle(), memoTextItem.getContent());
            FileOperation.deleteImagesMemo(memoTextItem.getMemoID(), memoTextItem.getPhotosCount());
            ListOperation.deleteList(memoTextItem.getMemoID(), memoTextItem.getTitle(), memoTextItem.getContent());

        } else if (memoItem instanceof MemoDrawingItem) {
            MemoDrawingItem memoDrawingItem = (MemoDrawingItem) memoItem;
            int memoid = memoDrawingItem.getMemoID();
            FileOperation.deleteDrawingMemo(memoid);
            ListOperation.deleteDrawingMemoList(position);
            mode = C.DELETE_DRAWINGS;
        }

        refreshAdapter();

        return super.onContextItemSelected(item);
    }


    private void refreshAdapter() {

        rcAdapter.nMemoList.clear();
        rcAdapter.nMemoList.addAll(ListOperation.getListViewItems());
        rcAdapter.notifyDataSetChanged();
    }

}
