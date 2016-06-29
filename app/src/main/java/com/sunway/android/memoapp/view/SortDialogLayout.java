package com.sunway.android.memoapp.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.util.ListOperation;

/**
 * Created by Mr_RexZ on 6/29/2016.
 */
public class SortDialogLayout extends DialogFragment {
    public EditText textMemo;
    public EditText drawingMemo;
    SortDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (SortDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.activity_sorting_pop_up_window, null);

        textMemo = (EditText) view.findViewById(R.id.text_memo_input_order);
        drawingMemo = (EditText) view.findViewById(R.id.drawing_memo_input_order);
        textMemo.setText(String.valueOf(ListOperation.TEXT_MEMO_SORT_ORDER));
        drawingMemo.setText(String.valueOf(ListOperation.DRAWING_MEMO_SORT_ORDER));
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        if ((textMemo.getText().toString().matches("")) ||
                                (drawingMemo.getText().toString().matches(""))) {
                            Toast.makeText(getActivity(), "You did not enter a correct value", Toast.LENGTH_SHORT).show();
                        } else {

                            mListener.onDialogPositiveClick(SortDialogLayout.this);

                        }

                        dismiss();


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mListener.onDialogNegativeClick(SortDialogLayout.this);
                        dismiss();
                    }
                });

        return builder.create();
    }


    public interface SortDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);

        void onDialogNegativeClick(DialogFragment dialog);
    }


}
