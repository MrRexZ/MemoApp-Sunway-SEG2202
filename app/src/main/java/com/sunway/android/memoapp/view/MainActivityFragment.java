package com.sunway.android.memoapp.view;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

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
public class MainActivityFragment extends Fragment implements SearchView.OnQueryTextListener {

    private StaggeredGridLayoutManager _sGridLayoutManager;
    private String input_title=null;
    private String input_details=null;
    private String mode = "START";
    private View rootView;
    private RecyclerView recyclerView;
    private MemoItemAdapter rcAdapter;
    private PopupWindow changeSortPopUp;
    private FrameLayout layout_MainMenu;

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
            return true;
        } else if (id == R.id.action_view_reminder_list) {
            Intent showReminderList = new Intent(getActivity(), ReminderListActivity.class);
            startActivity(showReminderList);
        } else if (id == R.id.action_set_sort_order) {
            showSortPopup(getActivity(), new Point(250, 250));
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

        this.rootView = inflater.inflate(R.layout.fragment_main, container, false);


        layout_MainMenu = (FrameLayout) rootView.findViewById(R.id.mainmenu);
        layout_MainMenu.getForeground().setAlpha(0);
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


        } else if (memoItem instanceof MemoDrawingItem) {
            FileOperation.deleteDrawingMemo(memoItem.getMemoID());
        }

        ListOperation.deleteList(memoItem.getMemoID());


        refreshAdapter();

        return super.onContextItemSelected(item);
    }


    private void showSortPopup(final Activity context, Point p) {

        layout_MainMenu.getForeground().setAlpha(220);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.activity_sorting_pop_up_window, null);
        final EditText text_order_input = (EditText) layout.findViewById(R.id.text_memo_input_order);
        final EditText drawing_order_input = (EditText) layout.findViewById(R.id.drawing_memo_input_order);

        text_order_input.setText(Integer.toString(ListOperation.TEXT_MEMO_SORT_ORDER));
        drawing_order_input.setText(Integer.toString(ListOperation.DRAWING_MEMO_SORT_ORDER));
        changeSortPopUp = new PopupWindow(context);
        changeSortPopUp.setContentView(layout);
        changeSortPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeSortPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeSortPopUp.setFocusable(true);
        changeSortPopUp.setAnimationStyle(android.R.style.Animation_Dialog);

        int OFFSET_X = -20;
        int OFFSET_Y = 95;

        changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());

        changeSortPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);


        Button close = (Button) layout.findViewById(R.id.close_popup);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ListOperation.TEXT_MEMO_SORT_ORDER = Integer.parseInt(text_order_input.getText().toString());
                ListOperation.DRAWING_MEMO_SORT_ORDER = Integer.parseInt(drawing_order_input.getText().toString());
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
                layout_MainMenu.getForeground().setAlpha(0);

                GridLayoutManager layoutManagerSorting = new GridLayoutManager(getActivity(), 3);
                recyclerView.setLayoutManager(layoutManagerSorting);
                changeSortPopUp.dismiss();
            }
        });

        Button reset = (Button) layout.findViewById(R.id.reset_sorting);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListOperation.TEXT_MEMO_SORT_ORDER = 0;
                ListOperation.DRAWING_MEMO_SORT_ORDER = 0;
                recyclerView.setLayoutManager(_sGridLayoutManager);
                Collections.sort(ListOperation.getListViewItems(), new MemoItemComparator());
                refreshAdapter();
                layout_MainMenu.getForeground().setAlpha(0);
                changeSortPopUp.dismiss();
            }
        });

    }


    private void refreshAdapter() {

        rcAdapter.nMemoList.clear();
        rcAdapter.nMemoList.addAll(ListOperation.getListViewItems());
        rcAdapter.notifyDataSetChanged();
    }


}
