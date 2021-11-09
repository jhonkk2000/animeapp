package com.jhonkkman.aniappinspiracy.data.update;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.jhonkkman.aniappinspiracy.AlertOptions;
import com.jhonkkman.aniappinspiracy.DownloadTask;
import com.jhonkkman.aniappinspiracy.R;

import static com.jhonkkman.aniappinspiracy.CenterActivity.PERMISSION_REQUEST_CODE;

public class NewUpdate {

    private Activity activity;
    private String urlApk;

    public NewUpdate(Activity activity,String urlApk){
        this.activity = activity;
        this.urlApk = urlApk;
    }

    public void loadDialogSecondApp() {
        AlertOptions alert = new AlertOptions();
        alert.showDialog(activity, activity.getString(R.string.message_update));
        alert.dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (alert.send) {
                    Toast.makeText(activity, "Descargando...", Toast.LENGTH_SHORT).show();
                    //downloadApk();
                    if (checkPermission()) {
                        // Code for above or equal 23 API Oriented Device
                        // Your Permission granted already .Do next code
                        downloadApk();
                    } else {
                        Toast.makeText(activity, "No permitido", Toast.LENGTH_SHORT).show();
                        requestPermission(); // Code for permission
                    }
                }
            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    public void downloadApk() {
        ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage("Actualizando");
        pd.setIndeterminate(true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable(false);
        final DownloadTask downloadTask = new DownloadTask(activity, pd);
        downloadTask.execute(urlApk);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });
    }
}
