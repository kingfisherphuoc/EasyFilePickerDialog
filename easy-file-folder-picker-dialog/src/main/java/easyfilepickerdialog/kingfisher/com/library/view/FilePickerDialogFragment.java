package easyfilepickerdialog.kingfisher.com.library.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.List;

import easyfilepickerdialog.kingfisher.com.library.R;
import easyfilepickerdialog.kingfisher.com.library.model.DialogConfig;

/**
 * Created by kingfisher on 5/22/17.
 */

public class FilePickerDialogFragment extends BaseFilePickerDialogFragment {

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.dialog_audio_picker;
    }

    @NonNull
    @Override
    protected int getRecyclerViewId() {
        return R.id.recyclerview;
    }

    @Nullable
    @Override
    protected int getTextViewCurrentFolderId() {
        return R.id.tvTitle;
    }

    @Override
    protected int getToolbarId() {
        return R.id.toolbar;
    }

    @Nullable
    @Override
    protected int getDoneButtonId() {
        return R.id.btnDone;
    }

    @Override
    protected int getLayoutRowId() {
        return R.layout.item_file_picker;
    }

    @Override
    protected int getImageViewIconId() {
        return R.id.ivImage;
    }

    @Override
    protected int getTextViewNameId() {
        return R.id.tvName;
    }

    @Override
    protected int getCheckBoxId() {
        return R.id.checkbox;
    }

    private static FilePickerDialogFragment newInstance(Builder builder) {
        FilePickerDialogFragment filePickerDialogFragment = new FilePickerDialogFragment();
        filePickerDialogFragment.onFilesSelectedListener = builder.onFilesSelectedListener;
        filePickerDialogFragment.dialogConfig = builder.dialogConfig;
        return filePickerDialogFragment;
    }


    public interface OnFilesSelectedListener {
        void onFileSelected(List<File> list);
    }

    public static final class Builder {
        private OnFilesSelectedListener onFilesSelectedListener;
        private DialogConfig dialogConfig;

        public Builder() {
        }

        public Builder onFilesSelected(OnFilesSelectedListener onFilesSelectedListener) {
            this.onFilesSelectedListener = onFilesSelectedListener;
            return this;
        }

        public Builder configs(DialogConfig dialogConfig) {
            this.dialogConfig = dialogConfig;
            return this;
        }


        public FilePickerDialogFragment build() {
            return FilePickerDialogFragment.newInstance(this);
        }
    }

}
