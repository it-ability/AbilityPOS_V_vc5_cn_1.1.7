package com.mobilepos.abc.abilitypos.Backup;

import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.ManagementActivity;
import com.mobilepos.abc.abilitypos.R;

import java.io.File;

import es.dmoral.toasty.Toasty;

public class LocalBackup {

    private ManagementActivity activity;

    public LocalBackup(ManagementActivity activity) {
        this.activity = activity;
    }

    //ask to the user a name for the backup and perform it. The backup will be saved to a custom folder.
    public void performBackup(final DB_Controller db, final String outFileName) {

        Permissions.verifyStoragePermissions(activity);
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getResources().getString(R.string.app_name));
        boolean success = true;

        if (!folder.exists()) {
            success = folder.mkdirs();
        }

        if (success) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Backup Name");
            final EditText input = new EditText(activity);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String m_Text = input.getText().toString();
                    String out = outFileName + m_Text + ".db";
                    db.backupDB(out);
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        } else {
            Toasty.info(activity, "Unable to Create Directory !", Toast.LENGTH_SHORT).show();
        }
    }

    //ask to the user what backup to restore
    public void performRestore(final DB_Controller db) {

        Permissions.verifyStoragePermissions(activity);
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getResources().getString(R.string.app_name));

        if (folder.exists()) {
            final File[] files = folder.listFiles();
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.select_dialog_item);
            for (File file : files) {
                arrayAdapter.add(file.getName());
            }

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(activity);
            builderSingle.setTitle("Restore:");
            builderSingle.setNegativeButton("cancel", null);
            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        db.importDB(files[which].getPath());
                    } catch (Exception e) {
                        Toast.makeText(activity, "Unable to restore. Retry", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builderSingle.show();
        } else {
            Toasty.info(activity, "Backup folder not present !", Toast.LENGTH_SHORT).show();
        }

    }
}