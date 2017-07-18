package kingfisher.com.easyfilepickerdialog;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import easyfilepickerdialog.kingfisher.com.library.model.DialogConfig;
import easyfilepickerdialog.kingfisher.com.library.view.BaseFilePickerDialogFragment;
import easyfilepickerdialog.kingfisher.com.library.view.FilePickerDialogFragment;

/**
 * Created by kingfisher_goong on 7/18/17.
 */

public class MyExampleCustomDialog extends BaseFilePickerDialogFragment {
    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.dialog_custom;
    }

    @NonNull
    @Override
    protected int getRecyclerViewId() {
        return R.id.recyclerview;
    }

    @Nullable
    @Override
    protected int getTextViewCurrentFolderId() {
        return 0;
    }

    @Override
    protected int getToolbarId() {
        return R.id.myToolBar;
    }

    @Nullable
    @Override
    protected int getDoneButtonId() {
        return 0;
    }

    @NonNull
    @Override
    protected int getLayoutRowId() {
        return R.layout.item_row;
    }

    @NonNull
    @Override
    protected int getImageViewIconId() {
        return R.id.ivImage;
    }

    @NonNull
    @Override
    protected int getTextViewNameId() {
        return R.id.tvName;
    }

    @NonNull
    @Override
    protected int getCheckBoxId() {
        return R.id.checkbox;
    }


    @Override
    protected RecyclerView.LayoutManager getInitialLayoutManager() {
        return new GridLayoutManager(getContext(), 3);
    }


    @Override
    protected void initView(View view) {
        super.initView(view);
        toolbar.inflateMenu(R.menu.menu_custom_dialog);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.actionDone:
                        doneSelecting();//supported function to passed result to listener
                        break;
                    case R.id.actionList:
                        changeLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        break;
                    case R.id.actionGrid:
                        changeLayoutManager(new GridLayoutManager(getContext(), 3));
                        break;
                }

                return false;
            }
        });
    }

    public static MyExampleCustomDialog newInstance(FilePickerDialogFragment.OnFilesSelectedListener onFilesSelectedListener, DialogConfig dialogConfig) {
        MyExampleCustomDialog myExampleCustomDialog = new MyExampleCustomDialog();
        myExampleCustomDialog.onFilesSelectedListener = onFilesSelectedListener;
        myExampleCustomDialog.dialogConfig = dialogConfig;
        return myExampleCustomDialog;
    }
}
