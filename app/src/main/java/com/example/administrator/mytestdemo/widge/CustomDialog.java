package com.example.administrator.mytestdemo.widge;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

/**
 * Created by Administrator on 2016/4/11.
 */
@SuppressLint("ValidFragment")
public class CustomDialog extends DialogFragment {
    private int icon_id;
    private String title;
    private String message;
    private String postive;
    private String nagtive;
    private String[] items;
    private boolean cancelable;
    private CustomDialogClickListener listener;

    /**
     * @param title
     * @param message
     * @param postive
     * @param nagtive
     * @param cancelable
     * @param listener
     */
    public CustomDialog(String title, String message, String postive, String nagtive, boolean cancelable, CustomDialogClickListener listener) {
        this(-1, title, message, null, postive, nagtive, cancelable, listener);
    }

    /**
     * 在Dialog中添加item
     * @param title
     * @param message
     * @param items
     * @param postive
     * @param nagtive
     * @param cancelable
     * @param listener
     */
    public CustomDialog(String title, String message, String[] items, String postive, String nagtive, boolean cancelable, CustomDialogClickListener listener) {
        this(-1, title, message, items, postive, nagtive, cancelable, listener);
    }


    /**
     * @param icon_id
     * @param title
     * @param message
     * @param postive
     * @param nagtive
     * @param cancelable
     * @param listener
     */

    public CustomDialog(int icon_id, String title, String message, String[] items, String postive, String nagtive, boolean cancelable, CustomDialogClickListener listener) {
        this.icon_id = icon_id;
        this.title = title;
        this.message = message;
        this.items = items;
        this.postive = postive;
        this.nagtive = nagtive;
        this.cancelable = cancelable;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (icon_id != -1) {
            builder.setIcon(icon_id);
        }
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }

        if (items != null && items.length != 0) {
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onItemClick(dialog, which);
                    }
                }
            });
        }

        if (!TextUtils.isEmpty(postive)) {
            builder.setPositiveButton(postive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (null != listener) {
                        listener.onPostiveClick(dialog, which);
                    }
                }
            });
        }

        if (!TextUtils.isEmpty(nagtive)) {
            builder.setNegativeButton(nagtive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (null != listener) {
                        listener.onNagetiveClick(dialog, which);
                    }
                }
            });
        }
        this.setCancelable(cancelable);
        return builder.create();
    }

}

