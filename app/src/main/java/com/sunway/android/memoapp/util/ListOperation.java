package com.sunway.android.memoapp.util;

import com.sunway.android.memoapp.model.MemoDrawingItem;
import com.sunway.android.memoapp.model.MemoItem;
import com.sunway.android.memoapp.model.MemoTextItem;
import com.sunway.android.memoapp.model.Reminder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Mr_RexZ on 6/4/2016.
 */
public class ListOperation {

    public static int TEXT_MEMO_SORT_ORDER = 0;
    public static int DRAWING_MEMO_SORT_ORDER = 0;

    private static List<MemoItem> listViewItems = new ArrayList<>();

    public static void clearListView() {
        listViewItems.clear();
    }

    public static void addToList(MemoItem memoType) {
        listViewItems.add(memoType);
    }

    public static void modifyTextList(int textid, int photosCount, String newTitle, String newDetails, int year, int month, int day, int hour, int minute, int second) {

        for (int counter=0;counter<listViewItems.size();counter++) {


                MemoItem memoItem= listViewItems.get(counter);
            if (memoItem instanceof MemoTextItem) {
                MemoTextItem memoTextItem = (MemoTextItem) memoItem;
                if (memoTextItem.getMemoID() == textid) {
                    memoTextItem.setTitle(newTitle);
                    memoTextItem.setContent(newDetails);
                    memoTextItem.setPhotosCount(photosCount);
                    memoTextItem.setReminder(new Reminder(year, month, day, hour, minute, second));
            }
            }
        }
    }

    public static void modifyDrawingList(int memoID, int year, int month, int day, int hour, int minute, int second) {

        for (int counter = 0; counter < listViewItems.size(); counter++) {

            MemoItem memoItem = listViewItems.get(counter);
            if (memoItem instanceof MemoDrawingItem) {
                MemoDrawingItem memoDrawingItem = (MemoDrawingItem) memoItem;
                if (memoDrawingItem.getMemoID() == memoID)
                    memoDrawingItem.setReminder(new Reminder(year, month, day, hour, minute, second));

            }
        }
    }


    public static void deleteList(int textid) {

        for (int counter = 0; counter < listViewItems.size(); counter++) {


            MemoItem memoItem = listViewItems.get(counter);

            if (memoItem.getMemoID() == textid) {
                    listViewItems.remove(counter);
                }
            }
        }


    public static void deleteDrawingMemoList(int position) {
        listViewItems.remove(position);
    }



    public static List<MemoItem> getListViewItems() {
        return listViewItems;
    }

    public static List<Reminder> getListViewReminderItems() {
        Iterator iteratorReminder = listViewItems.iterator();
        ArrayList<Reminder> reminderArrayList = new ArrayList<>();
        while (iteratorReminder.hasNext()) {
            reminderArrayList.add(((MemoItem) iteratorReminder.next()).getReminder());
        }
        return reminderArrayList;

    }

    public static MemoItem getIndividualMemoItem(int index) {
        return listViewItems.get(index);
    }

}
