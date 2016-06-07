package com.sunway.android.memoapp.util;

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

    public static void modifyTextList(String textid, int photosCount, String oldTitle, String oldDetails, String newTitle, String newDetails) {

        for (int counter=0;counter<listViewItems.size();counter++) {


                MemoItem memoItem= listViewItems.get(counter);
            if (memoItem instanceof MemoTextItem) {

                if (memoItem.getTitle().equals(oldTitle) && memoItem.getContent().equals(oldDetails) && memoItem.getMemoID().equals(textid)){
                memoItem.setTitle(newTitle);
                memoItem.setContent(newDetails);
                    memoItem.setPhotosCount(photosCount);
            }
            }
        }
    }

    public static void deleteList(String textid, String oldTitle, String oldDetails) {

        for (int counter = 0; counter < listViewItems.size(); counter++) {


            MemoItem memoItem = listViewItems.get(counter);
            if (memoItem instanceof MemoTextItem) {

                if (memoItem.getTitle().equals(oldTitle) && memoItem.getContent().equals(oldDetails) && memoItem.getMemoID().equals(textid)) {
                    listViewItems.remove(counter);
                }
            }
        }
    }



    public static List<MemoItem> getListViewItems() {
        return listViewItems;
    }

    public static MemoItem getIndividualMemoItem(int index) {
        return listViewItems.get(index);
    }

}
