package kingfisher.com.easyfilepickerdialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.List;

import easyfilepickerdialog.kingfisher.com.library.model.DialogConfig;
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
//                .supportFiles(new SupportFile(".3gpp", R.drawable.ic_audio), new SupportFile(".mp3", 0), new SupportFile(".pdf", R.drawable.ic_pdf))
                .build();

        new FilePickerDialogFragment.Builder()
                .configs(dialogConfig)
                .onAudioFilesSelected(new FilePickerDialogFragment.OnAudioFilesSelected() {
                    @Override
                    public void onAudioSelected(List<File> list) {
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
