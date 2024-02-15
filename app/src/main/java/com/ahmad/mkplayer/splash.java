package com.ahmad.mkplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        &&ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            splasher();
        } else {
            requestpermission();
        }
    }

    private void splasher() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(splash.this, AdvancePlayer.class));
            }
        }, 3000);
    }


    public void requestpermission() {
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO};
        requestPermissions(permission, 123);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 123) {
            boolean allpermission;
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                allpermission = true;
                splasher();
            } else {
                allpermission = false;
            }
            if (!allpermission) {
                if (shouldShowPermissionRationale()) {
                    showRationaleDialog();
                } else {
                    informuser();
                }
                showRationaleDialog();

            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void informuser() {
        new MaterialAlertDialogBuilder(this).setTitle("Permission Required")
                .setMessage("It seems like you have denied the required permissions. Please grant the permissions.").setPositiveButton("ok", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }).show();
    }

    private boolean shouldShowPermissionRationale() {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO);
    }

    private void showRationaleDialog() {
        new MaterialAlertDialogBuilder(this).setTitle("Permission Required")
                .setMessage("We need permissions to get music Please grant the permissions.").setPositiveButton("ok", (dialog, which) -> {
                    requestpermission();
                }).show();
    }
}