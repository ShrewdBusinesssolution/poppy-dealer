package com.shrewd.poppydealers.utilities;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shrewd.poppydealers.R;


public class LoadingProgress {
    public static LoadingProgress loadingProgress = null;
    Dialog dialog;
    ProgressBar mProgressBar;
    Handler handler = new Handler();
    Runnable runnable;

    public static LoadingProgress getInstance() {
        if (loadingProgress == null) {
            loadingProgress = new LoadingProgress();
        }
        return loadingProgress;
    }

    public void ShowProgress(Context context, String message, boolean cancelable) {
        dialog = new Dialog(context, R.style.MyTheme);
        // no tile for the dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_dialog);
        mProgressBar = (ProgressBar) dialog.findViewById(R.id.progressbar1);
        //  mProgressBar.getIndeterminateDrawable().setColorFilter(context.getResources()
        // .getColor(R.color.material_blue_gray_500), PorterDuff.Mode.SRC_IN);
        TextView progressText = (TextView) dialog.findViewById(R.id.loadingTV);
        progressText.setText("" + message);
        progressText.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        // you can change or add this line according to your need
        mProgressBar.setIndeterminate(true);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(cancelable);

        runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog != null) {
                    dialog.dismiss();
                    Toast.makeText(context, "something went wrong. please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        handler.postDelayed(runnable, 5000);

        dialog.show();


    }

    public void ShowFileProgress(Context context, String message, boolean cancelable) {
        dialog = new Dialog(context, R.style.MyTheme);
        // no tile for the dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_dialog);
        mProgressBar = (ProgressBar) dialog.findViewById(R.id.progressbar1);
        //  mProgressBar.getIndeterminateDrawable().setColorFilter(context.getResources()
        // .getColor(R.color.material_blue_gray_500), PorterDuff.Mode.SRC_IN);
        TextView progressText = (TextView) dialog.findViewById(R.id.loadingTV);
        progressText.setText("" + message);
        progressText.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        // you can change or add this line according to your need
        mProgressBar.setIndeterminate(true);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(cancelable);

        dialog.show();


    }

    public void hideProgress() {
        if (dialog != null) {
            handler.removeCallbacks(runnable);
            dialog.dismiss();
            dialog = null;
        }
    }
}

