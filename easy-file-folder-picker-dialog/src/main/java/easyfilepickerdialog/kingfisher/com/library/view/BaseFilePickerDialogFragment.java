package easyfilepickerdialog.kingfisher.com.library.view;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
 * Created by kingfisher_goong on 7/18/17.
 */
@RuntimePermissions
public abstract class BaseFilePickerDialogFragment extends BaseNoFrameDialogFragment implements BaseViewHolder.OnViewHolderClickListener, View.OnClickListener, FilePickerView {

    TextView tvCurrentPath;
    protected Toolbar toolbar;
    RecyclerView recyclerview;

    FileAdapter fileAdapter;
    FilePickerPresenter filePickerPresenter;
    protected FilePickerDialogFragment.OnFilesSelectedListener onFilesSelectedListener;
    protected DialogConfig dialogConfig;

    @NonNull
    /**
     * Layout resource for the dialog view
     */
    protected abstract int getLayoutId();

    @NonNull
    /**
     * Recyclerview to display list of item
     */
    protected abstract int getRecyclerViewId();


    /**
     * Id of the textview which is used to display current folder path
     *
     * @return
     */
    @Nullable
    protected abstract int getTextViewCurrentFolderId();

    /**
     * id of the toolbar
     *
     * @return
     */
    protected abstract int getToolbarId();

    /**
     * The done button in the view. Can be null.
     *
     * @return
     */
    @Nullable
    protected abstract int getDoneButtonId();

    /**
     * The layout of each row in list
     *
     * @return
     */
    @NonNull
    protected abstract int getLayoutRowId();

    /**
     * The id of imageview
     *
     * @return
     */
    @NonNull
    protected abstract int getImageViewIconId();

    /**
     * Id of textview as name of file
     *
     * @return
     */
    @NonNull
    protected abstract int getTextViewNameId();

    /**
     * The checkbox icon
     *
     * @return
     */
    @NonNull
    protected abstract int getCheckBoxId();

    /**
     * Get initial layout manager of this recyclerview
     *
     * @return
     */
    protected RecyclerView.LayoutManager getInitialLayoutManager() {
        return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    }

    /**
     * Change layout Manager of recyclerview
     *
     * @param layoutManager
     */
    public void changeLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.recyclerview.setLayoutManager(layoutManager);
        this.recyclerview.setAdapter(fileAdapter);
    }


    @Override
    void setupDialogPosition() {
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(p);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        initView(view);

        if (fileAdapter == null) {
            fileAdapter = new FileAdapter(new ArrayList<File>(), getLayoutRowId(), dialogConfig, this);
            fileAdapter.setIconId(getImageViewIconId())
                    .setNameId(getTextViewNameId())
                    .setCheckBoxId(getCheckBoxId());
        }

        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(getInitialLayoutManager());
        recyclerview.setAdapter(fileAdapter);


        setRetainInstance(true);

        if (filePickerPresenter == null) {
            filePickerPresenter = new FilePickerPresenter(this);
            filePickerPresenter.setDialogConfig(dialogConfig);

            // load with the initial path
            String initPath = null;
            if (dialogConfig != null && !TextUtils.isEmpty(dialogConfig.getInitialDirectory())) {
                initPath = dialogConfig.getInitialDirectory();
            } else {
                initPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            }
            BaseFilePickerDialogFragmentPermissionsDispatcher.loadFolderWithCheck(this, initPath);
        }
        return view;
    }

    /**
     * Initialize and setup view
     *
     * @param view
     */
    protected void initView(View view) {
        recyclerview = (RecyclerView) view.findViewById(getRecyclerViewId());
        tvCurrentPath = (TextView) view.findViewById(getTextViewCurrentFolderId());
        toolbar = (Toolbar) view.findViewById(getToolbarId());
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
        if (view.findViewById(getDoneButtonId()) != null) {
            view.findViewById(getDoneButtonId()).setOnClickListener(this);
        }
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void loadFolder(String path) {
        boolean isSuccess = filePickerPresenter.loadFolder(path);
        if (!isSuccess)
            new AlertDialog.Builder(getContext())
                    .setTitle("Access error")
                    .setMessage("Could not access folder")
                    .setCancelable(false)
                    .setPositiveButton("OK", null).show();
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void onPermissionDenied() {
        Toast.makeText(getContext(), "Cannot load sdcard if you do not accept the required permission!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaseFilePickerDialogFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void showFolderContent(String path, List<File> files) {
        fileAdapter.clear();
        fileAdapter.addItem(files);
        fileAdapter.clearCheckedList();
        if (tvCurrentPath != null)
            tvCurrentPath.setText(path);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnDone) {
            doneSelecting();
        }
    }

    /**
     * Finished the selection and send the selected items to your view
     */
    protected void doneSelecting() {
        if (onFilesSelectedListener != null) {
            onFilesSelectedListener.onFileSelected(fileAdapter.getCheckedFiles());
        }
        dismiss();
    }


    @Override
    public void onItemClick(int position) {
        if (position == 0) {
            // click on Up folder
            filePickerPresenter.loadUpFolder();
        } else if (fileAdapter.getItem(position) != null) {
            BaseFilePickerDialogFragmentPermissionsDispatcher.loadFolderWithCheck(this, fileAdapter.getItem(position).getAbsolutePath());
        }
    }


}
