package easyfilepickerdialog.kingfisher.com.library.view;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import easyfilepickerdialog.kingfisher.com.library.R;
import easyfilepickerdialog.kingfisher.com.library.adapter.BaseViewHolder;
import easyfilepickerdialog.kingfisher.com.library.adapter.FileAdapter;
import easyfilepickerdialog.kingfisher.com.library.model.DialogConfig;
import easyfilepickerdialog.kingfisher.com.library.presenter.FilePickerPresenter;

/**
 * Created by kingfisher on 5/22/17.
 */

public class FilePickerDialogFragment extends BaseNoFrameDialogFragment implements FilePickerView,
        BaseViewHolder.OnViewHolderClickListener, View.OnClickListener {

    private static final String TAG = "FilePickerDialog";

    RecyclerView recyclerview;
    TextView tvTitle;
    Toolbar toolbar;

    FileAdapter fileAdapter;
    FilePickerPresenter filePickerPresenter;
    OnAudioFilesSelected onAudioFilesSelected;
    DialogConfig dialogConfig;


    private static FilePickerDialogFragment newInstance(Builder builder) {
        FilePickerDialogFragment filePickerDialogFragment = new FilePickerDialogFragment();
        filePickerDialogFragment.onAudioFilesSelected = builder.onAudioFilesSelected;
        filePickerDialogFragment.dialogConfig = builder.dialogConfig;
        return filePickerDialogFragment;
    }

    public FilePickerDialogFragment audioFIleSelected(OnAudioFilesSelected onAudioFilesSelected) {
        this.onAudioFilesSelected = onAudioFilesSelected;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_audio_picker, container, false);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        if (fileAdapter == null) {
            fileAdapter = new FileAdapter(new ArrayList<File>(), dialogConfig, this);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(fileAdapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        view.findViewById(R.id.btnDone).setOnClickListener(this);

        setRetainInstance(true);

        if (filePickerPresenter == null) {
            filePickerPresenter = new FilePickerPresenter(this);
            filePickerPresenter.setDialogConfig(dialogConfig);
            filePickerPresenter.loadFolder(Environment.getExternalStorageDirectory().getAbsolutePath());
        }
        return view;
    }


    @Override
    public void showFolderContent(String path, List<File> files) {
        fileAdapter.clear();
        fileAdapter.addItem(files);
        fileAdapter.clearCheckedList();
        tvTitle.setText(path);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnDone) {
            if (onAudioFilesSelected != null) {
                onAudioFilesSelected.onAudioSelected(fileAdapter.getCheckedFiles());
            }
            dismiss();
        }
    }


    @Override
    public void onItemClick(int position) {
        Log.e(TAG, "item clicked: " + fileAdapter.getItem(position).getName());
        if (position == 0) {
            // click on Up folder
            filePickerPresenter.loadUpFolder();
        } else {
            filePickerPresenter.loadFolder(fileAdapter.getItem(position).getAbsolutePath());
        }
    }


    @Override
    void setupDialogPosition() {
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.height = ViewGroup.LayoutParams.MATCH_PARENT;

//        getDialog().getWindow().setGravity(Gravity.CENTER);
        getDialog().getWindow().setAttributes(p);
    }

    public interface OnAudioFilesSelected {
        void onAudioSelected(List<File> list);
    }


    public static final class Builder {
        private OnAudioFilesSelected onAudioFilesSelected;
        private DialogConfig dialogConfig;

        public Builder() {
        }

        public Builder onAudioFilesSelected(OnAudioFilesSelected onAudioFilesSelected) {
            this.onAudioFilesSelected = onAudioFilesSelected;
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
