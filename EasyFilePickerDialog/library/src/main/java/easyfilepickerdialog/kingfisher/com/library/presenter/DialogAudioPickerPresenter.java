package easyfilepickerdialog.kingfisher.com.library.presenter;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import easyfilepickerdialog.kingfisher.com.library.view.DialogAudioPickerView;
import timber.log.Timber;

/**
 * Created by kingfisher on 5/22/17.
 */

public class DialogAudioPickerPresenter {
    private DialogAudioPickerView dialogAudioPickerView;

    public DialogAudioPickerPresenter(DialogAudioPickerView dialogAudioPickerView) {
        this.dialogAudioPickerView = dialogAudioPickerView;
    }

    private String folderPath;

    public void loadFolder(String folderPath) {
        File file = new File(folderPath);
        if (file.exists() && file.isDirectory()) {
            Timber.e("Load folder: " + folderPath);
            this.folderPath = folderPath;
            Timber.e(file.listFiles().toString());

            List<File> list = new ArrayList<>();
            list.add(new File("Up"));
            Collections.addAll(list, file.listFiles());
            dialogAudioPickerView.showFolderContent(list);
        }
    }

    public void loadUpFolder() {
        if (folderPath != null) {
            File file = new File(folderPath);
            if (file.exists() && file.getParentFile() != null) {
                loadFolder(file.getParentFile().getAbsolutePath());
            } else {
                Timber.e("No up folder");
            }
        }
    }
}
