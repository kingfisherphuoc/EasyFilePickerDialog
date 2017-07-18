package easyfilepickerdialog.kingfisher.com.library.presenter;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import easyfilepickerdialog.kingfisher.com.library.model.DialogConfig;
import easyfilepickerdialog.kingfisher.com.library.model.SupportFile;
import easyfilepickerdialog.kingfisher.com.library.view.FilePickerView;

/**
 * Created by kingfisher on 5/22/17.
 */

public class FilePickerPresenter {
    private FilePickerView dialogAudioPickerView;

    public FilePickerPresenter(FilePickerView dialogAudioPickerView) {
        this.dialogAudioPickerView = dialogAudioPickerView;
    }

    private String folderPath;
    private DialogConfig dialogConfig;

    public void setDialogConfig(DialogConfig dialogConfig) {
        this.dialogConfig = dialogConfig;
    }

    public boolean loadFolder(String folderPath) {
        File file = new File(folderPath);
        if (file.exists() && file.isDirectory()) {
            this.folderPath = folderPath;
            if (file.listFiles() == null) {
                return false;
            } else {
                List<File> list = new ArrayList<>();
                for (File fileInFolder : file.listFiles()) {
                    if (fileInFolder.isDirectory() || isFileInFilterList(fileInFolder)) {
                        list.add(fileInFolder);
                    }
                }

                Collections.sort(list, new Comparator<File>() {
                    @Override
                    public int compare(File file, File t1) {
                        if (file.isDirectory() && !t1.isDirectory()) {
                            return -1;
                        } else if (!file.isDirectory() && t1.isDirectory()) {
                            return 1;
                        } else {
                            return file.getName().compareTo(t1.getName());
                        }
                    }
                });
                list.add(0, new File("Up"));
                dialogAudioPickerView.showFolderContent(folderPath, list);
            }
        }
        return true;
    }

    public void loadUpFolder() {
        if (folderPath != null) {
            File file = new File(folderPath);
            if (file.exists() && file.getParentFile() != null) {
                loadFolder(file.getParentFile().getAbsolutePath());
            }
        }
    }

    /**
     * is this file in the {@link SupportFile} list of {@link DialogConfig}
     *
     * @param file
     * @return
     */
    private boolean isFileInFilterList(File file) {
        // default show all files
        if (dialogConfig == null || dialogConfig.getSupportFiles() == null || dialogConfig.getSupportFiles().isEmpty()) {
            return true;
        }
        String ext = getExtension(file); // usually ".mp3"...
        if (!TextUtils.isEmpty(ext)) {
            for (SupportFile supportFile : dialogConfig.getSupportFiles()) {
                if (supportFile.getExt().equals(ext)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param file
     * @return The file extension. If file has no extension, it returns null.
     */
    public static String getExtension(@NonNull File file) {
        String path = file.getPath();
        int i = path.lastIndexOf(".");
        if (i < 0) {
            return null;
        } else {
            return path.substring(i);
        }
    }
}
