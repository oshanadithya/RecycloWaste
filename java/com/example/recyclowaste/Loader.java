package com.example.recyclowaste;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import androidx.core.app.ActivityCompat;

public class Loader {
    private Activity activity;
    private AlertDialog alertDialog;

    Loader (Activity activity) {
        this.activity = activity;
    }

    public void showLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_layout, null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismissLoadingDialog() {
        alertDialog.dismiss();
    }
}
