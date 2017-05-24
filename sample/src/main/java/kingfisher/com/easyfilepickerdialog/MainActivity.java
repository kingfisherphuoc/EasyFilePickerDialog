package kingfisher.com.easyfilepickerdialog;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.List;

import easyfilepickerdialog.kingfisher.com.library.model.DialogConfig;
import easyfilepickerdialog.kingfisher.com.library.model.SupportFile;
import easyfilepickerdialog.kingfisher.com.library.view.FilePickerDialogFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showFilePicker(View v) {
        DialogConfig dialogConfig = new DialogConfig.Builder()
                .enableMultipleSelect(true)
                .enableFolderSelect(true)
                .initialDirectory(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Android")
                .supportFiles(new SupportFile(".3gpp", R.drawable.ic_audio), new SupportFile(".mp3", 0), new SupportFile(".pdf", R.drawable.ic_pdf))
                .build();

        new FilePickerDialogFragment.Builder()
                .configs(dialogConfig)
                .onFilesSelected(new FilePickerDialogFragment.OnFilesSelectedListener() {
                    @Override
                    public void onFileSelected(List<File> list) {
                        Log.e(TAG, "total Selected file: " + list.size());
                        for (File file : list) {
                            Log.e(TAG, "Selected file: " + file.getAbsolutePath());
                        }
                    }
                })
                .build()
                .show(getSupportFragmentManager(), null);
    }
}
