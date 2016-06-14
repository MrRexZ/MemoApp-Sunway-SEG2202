package com.sunway.android.memoapp.util;

import com.sunway.android.memoapp.model.MemoDrawingItem;
import com.sunway.android.memoapp.model.MemoItem;
import com.sunway.android.memoapp.model.MemoTextItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr_RexZ on 6/4/2016.
 */
public class ListOperation {


    private static List<MemoItem> listViewItems = new ArrayList<>();

    public static void clearListView() {
        listViewItems.clear();
    }

    public static void addToList(MemoItem memoType) {
        listViewItems.add(memoType);
    }

    public static void modifyTextList(int textid, int photosCount, String oldTitle, String oldDetails, String newTitle, String newDetails) {

        for (int counter=0;counter<listViewItems.size();counter++) {


                MemoItem memoItem= listViewItems.get(counter);
            if (memoItem instanceof MemoTextItem) {
                MemoTextItem memoTextItem = (MemoTextItem) memoItem;
                if (memoTextItem.getTitle().equals(oldTitle) && memoTextItem.getContent().equals(oldDetails) && memoTextItem.getMemoID() == textid) {
                    memoTextItem.setTitle(newTitle);
                    memoTextItem.setContent(newDetails);
                    memoTextItem.setPhotosCount(photosCount);
            }
            }
        }
    }

    public static void modifyDrawingList(int textid) {

        for (int counter = 0; counter < listViewItems.size(); counter++) {

            MemoItem memoItem = listViewItems.get(counter);
            if (memoItem instanceof MemoDrawingItem) {
                MemoDrawingItem memoDrawingItem = (MemoDrawingItem) memoItem;
                if (memoDrawingItem.getMemoID() == textid)
                    memoDrawingItem.setMemoID(textid);

            }
        }
    }


    public static void deleteList(int textid, String oldTitle, String oldDetails) {

        for (int counter = 0; counter < listViewItems.size(); counter++) {


            MemoItem memoItem = listViewItems.get(counter);
            if (memoItem instanceof MemoTextItem) {
                MemoTextItem memoTextItem = (MemoTextItem) memoItem;
                if (memoTextItem.getTitle().equals(oldTitle) && memoTextItem.getContent().equals(oldDetails) && memoTextItem.getMemoID() == textid) {
                    listViewItems.remove(counter);
                }
            }
        }
    }

    public static void deleteDrawingMemoList(int position) {
        listViewItems.remove(position);
    }



    public static List<MemoItem> getListViewItems() {
        return listViewItems;
    }

    public static MemoItem getIndividualMemoItem(int index) {
        return listViewItems.get(index);
    }

}
