package easyfilepickerdialog.kingfisher.com.library.view;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import easyfilepickerdialog.kingfisher.com.library.R;
import easyfilepickerdialog.kingfisher.com.library.adapter.BaseViewHolder;
import easyfilepickerdialog.kingfisher.com.library.adapter.FileAdapter;
import easyfilepickerdialog.kingfisher.com.library.model.DialogConfig;
import easyfilepickerdialog.kingfisher.com.library.presenter.FilePickerPresenter;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by kingfisher on 5/22/17.
 */

@RuntimePermissions
public class FilePickerDialogFragment extends BaseNoFrameDialogFragment implements FilePickerView,
        BaseViewHolder.OnViewHolderClickListener, View.OnClickListener {

    private static final String TAG = "FilePickerDialog";

    RecyclerView recyclerview;
    TextView tvTitle;
    Toolbar toolbar;

    FileAdapter fileAdapter;
    FilePickerPresenter filePickerPresenter;
    OnFilesSelectedListener onFilesSelectedListener;
    DialogConfig dialogConfig;


    private static FilePickerDialogFragment newInstance(Builder builder) {
        FilePickerDialogFragment filePickerDialogFragment = new FilePickerDialogFragment();
        filePickerDialogFragment.onFilesSelectedListener = builder.onFilesSelectedListener;
        filePickerDialogFragment.dialogConfig = builder.dialogConfig;
        return filePickerDialogFragment;
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

            FilePickerDialogFragmentPermissionsDispatcher.loadFolderWithCheck(this, Environment.getExternalStorageDirectory().getAbsolutePath());
        }
        return view;
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void loadFolder(String path) {
        filePickerPresenter.loadFolder(path);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void onPermissionDenied() {
        Toast.makeText(getContext(), "Cannot load sdcard if you do not accept the required permission!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FilePickerDialogFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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
            if (onFilesSelectedListener != null) {
                onFilesSelectedListener.onFileSelected(fileAdapter.getCheckedFiles());
            }
            dismiss();
        }
    }


    @Override
    public void onItemClick(int position) {
//        Log.e(TAG, "item clicked: " + fileAdapter.getItem(position).getName());
        if (position == 0) {
            // click on Up folder
            filePickerPresenter.loadUpFolder();
        } else {
//            loadFolder(fileAdapter.getItem(position).getAbsolutePath());
            FilePickerDialogFragmentPermissionsDispatcher.loadFolderWithCheck(this, fileAdapter.getItem(position).getAbsolutePath());
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
