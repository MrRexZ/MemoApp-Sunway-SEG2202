package com.sunway.android.memoapp.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import com.sunway.android.memoapp.controller.MemoItemComparator;
import com.sunway.android.memoapp.model.MemoDrawingItem;
import com.sunway.android.memoapp.model.MemoItem;
import com.sunway.android.memoapp.model.MemoItemAdapter;
import com.sunway.android.memoapp.model.MemoTextItem;
import com.sunway.android.memoapp.util.C;
import com.sunway.android.memoapp.util.FileOperation;
import com.sunway.android.memoapp.util.ListOperation;
import com.sunway.android.memoapp.util.MyApplication;

import java.io.File;
import java.util.Collections;
import java.util.ListIterator;

/**
 * Created by Mr_RexZ on 5/28/2016.
 */
public class MainActivityFragment extends Fragment implements SearchView.OnQueryTextListener, SortDialogLayout.SortDialogListener {

    private StaggeredGridLayoutManager _sGridLayoutManager;
    private View rootView;
    private RecyclerView recyclerView;
    private MemoItemAdapter rcAdapter;
    private SortDialogLayout sortDialogLayout;

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete_all_memo) {
            FileOperation.deleteTempFile();
            ListOperation.getListViewItems().clear();
            refreshAdapter();
            File mydir = MyApplication.getAppContext().getDir("memoapp", Context.MODE_PRIVATE);
            FileOperation.memoCountId = 0;
            if (!mydir.exists())
                mydir.mkdirs();
            FileOperation.readFile("START", "u_" + FileOperation.userID + ".txt");
            return true;
        } else if (id == R.id.action_view_reminder_list) {
            Intent showReminderList = new Intent(getActivity(), ReminderListActivity.class);
            startActivity(showReminderList);
        } else if (id == R.id.action_set_sort_order) {
            sortDialogLayout = new SortDialogLayout();
            sortDialogLayout.setTargetFragment(MainActivityFragment.this, 300);
            sortDialogLayout.show(this.getFragmentManager(), "fragment_edit_name");
            return true;
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

        this.rootView = inflater.inflate(R.layout.fragment_main_screen, container, false);


        inflateLayout();

        Toolbar upper_toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_upper);
        ((AppCompatActivity)getActivity()).setSupportActionBar(upper_toolbar);
        upper_toolbar.setTitle("Memo App");


        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) rootView.findViewById(R.id.memo_search_view);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);

        Toolbar bottom_toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_bottom);
        bottom_toolbar.inflateMenu(R.menu.bottom_main_menu);
        bottom_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                int id = arg0.getItemId();
                if (id == R.id.action_open_create_text_memo) {
                    Intent showDetail = new Intent(getActivity(), TextDetailsMemoActivity.class)
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

        int id = item.getItemId();
        if (id == R.id.action_delete_memo) {
            int position = rcAdapter.getPosition();
            MemoItem memoItem = rcAdapter.nMemoList.get(position);

            if (memoItem instanceof MemoTextItem) {
                MemoTextItem memoTextItem = (MemoTextItem) memoItem;
                FileOperation.deleteTextMemo(memoTextItem.getMemoID(), memoTextItem.getPhotosCount(), memoTextItem.getTitle(), memoTextItem.getContent());
                FileOperation.deleteImagesMemo(memoTextItem.getMemoID(), memoTextItem.getPhotosCount());
            } else if (memoItem instanceof MemoDrawingItem) {
                FileOperation.deleteDrawingMemo(memoItem.getMemoID());
            }

            ListOperation.deleteList(memoItem.getMemoID());
            refreshAdapter();
            return super.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);

    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        SortDialogLayout sortDialogLayout = (SortDialogLayout) dialog;
        ListOperation.TEXT_MEMO_SORT_ORDER = Integer.parseInt(sortDialogLayout.textMemo.getText().toString());
        ListOperation.DRAWING_MEMO_SORT_ORDER = Integer.parseInt(sortDialogLayout.drawingMemo.getText().toString());
        ListIterator<MemoItem> listViewItems = ListOperation.getListViewItems().listIterator();


        while (listViewItems.hasNext()) {
            MemoItem currentMemo = listViewItems.next();
            if (currentMemo instanceof MemoTextItem)
                currentMemo.setSortOrder(ListOperation.TEXT_MEMO_SORT_ORDER);
            else if (currentMemo instanceof MemoDrawingItem)
                currentMemo.setSortOrder(ListOperation.DRAWING_MEMO_SORT_ORDER);
        }


        Collections.sort(ListOperation.getListViewItems(), new MemoItemComparator());
        refreshAdapter();

        GridLayoutManager layoutManagerSorting = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManagerSorting);

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        ListOperation.TEXT_MEMO_SORT_ORDER = 0;
        ListOperation.DRAWING_MEMO_SORT_ORDER = 0;
        recyclerView.setLayoutManager(_sGridLayoutManager);
        Collections.sort(ListOperation.getListViewItems(), new MemoItemComparator());
        refreshAdapter();
        dialog.getDialog().cancel();

    }


    private void refreshAdapter() {

        rcAdapter.nMemoList.clear();
        rcAdapter.nMemoList.addAll(ListOperation.getListViewItems());
        rcAdapter.notifyDataSetChanged();
    }


}