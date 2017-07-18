package easyfilepickerdialog.kingfisher.com.library.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import easyfilepickerdialog.kingfisher.com.library.R;
import easyfilepickerdialog.kingfisher.com.library.model.DialogConfig;
import easyfilepickerdialog.kingfisher.com.library.model.SupportFile;
import easyfilepickerdialog.kingfisher.com.library.presenter.FilePickerPresenter;

/**
 * Created by kingfisher on 5/22/17.
 */

public class FileAdapter extends BaseRecyclerArrayAdapter<File> {

    private BaseViewHolder.OnViewHolderClickListener onViewHolderClickListener;
    private List<Integer> checkedPositions = new ArrayList<>();
    private DialogConfig dialogConfig;
    private HashMap<String, Integer> mapFileIcons;

    private int ivIconId;
    private int tvNameId;
    private int cbId;

    public FileAdapter(List<File> list,
                       int layoutId,
                       DialogConfig dialogConfig,
                       BaseViewHolder.OnViewHolderClickListener onViewHolderClickListener) {
        super(list, layoutId);
        this.onViewHolderClickListener = onViewHolderClickListener;
        this.dialogConfig = dialogConfig;

        // setup mapping extension to resource
        if (dialogConfig != null && dialogConfig.getSupportFiles() != null && !dialogConfig.getSupportFiles().isEmpty()) {
            mapFileIcons = new HashMap<>();
            for (SupportFile supportFile : dialogConfig.getSupportFiles()) {
                mapFileIcons.put(supportFile.getExt(), supportFile.getResId());
            }
        }
    }

    public FileAdapter setIconId(int iconId) {
        ivIconId = iconId;
        return this;
    }

    public FileAdapter setNameId(int id) {
        this.tvNameId = id;
        return this;
    }

    public FileAdapter setCheckBoxId(int id) {
        this.cbId = id;
        return this;
    }


    /**
     * Get supported file icon to display
     *
     * @param file
     * @return
     */
    private int getIconResource(File file) {
        if (mapFileIcons != null) {
            String ext = FilePickerPresenter.getExtension(file);
            return !TextUtils.isEmpty(ext) ? mapFileIcons.get(ext) : 0;
        }
        return 0;
    }

    /**
     * Is multiple file select enable? Default is disable.
     *
     * @return
     */
    private boolean isMultipleSelectEnable() {
        return this.dialogConfig != null ? this.dialogConfig.isEnableMultipleSelect() : DialogConfig.DEFAULT_DISABLE_MULTIPLE_FILE_SELECT;
    }

    /**
     * is folder select allowed?
     *
     * @return
     */
    private boolean isFolderSelectEnable() {
        return this.dialogConfig != null ? this.dialogConfig.isEnableFolderSelect() : DialogConfig.DEFAULT_DISABLE_FOLDER_SELECT;
    }


    @Override
    public BaseViewHolder<File> getViewHolder(View view) {
        return new ViewHolder(view, this.onViewHolderClickListener);
    }


    /**
     * Get all the selected files
     *
     * @return
     */
    public List<File> getCheckedFiles() {
        List<File> list = new ArrayList<>();
        for (Integer integer : checkedPositions) {
            list.add(getItem(integer));
        }
        return list;
    }

    public void clearCheckedList() {
        checkedPositions.clear();
    }

    class ViewHolder extends BaseViewHolder<File> {
        TextView tvName;
        ImageView imageView;
        CheckBox checkBox;

        boolean isBinding = false;

        public ViewHolder(View itemView, OnViewHolderClickListener onViewHolderClickListener) {
            super(itemView, onViewHolderClickListener);
            tvName = (TextView) itemView.findViewById(tvNameId);
            imageView = (ImageView) itemView.findViewById(ivIconId);
            checkBox = (CheckBox) itemView.findViewById(cbId);
        }

        @Override
        public void setData(File data, final int pos) {
            isBinding = true;
            // always show "Up" folder.
            if (pos == 0) {
                tvName.setText("Up...");
            } else {
                tvName.setText(data.getName());
            }
            // Show file icon based on provided File Extension
            if (data.isDirectory() || pos == 0) {
                imageView.setBackgroundResource(R.drawable.ic_folder);

            } else {
                checkBox.setVisibility(View.VISIBLE);
                int resource = getIconResource(data);
                if (resource == 0) {
                    resource = R.drawable.ic_file;
                }
                imageView.setBackgroundResource(resource);
            }
            // show folder picker or not
            if (isFolderSelectEnable()) { // always show check box
                checkBox.setVisibility(pos != 0 ? View.VISIBLE : View.GONE);
            } else { // only show check box with file
                checkBox.setVisibility(pos != 0 && !data.isDirectory() ? View.VISIBLE : View.GONE);
            }

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        // clear all previous checked position if multiple select disable
                        if (!isMultipleSelectEnable()) {
                            checkedPositions.clear();
                            if (!isBinding)
                                notifyDataSetChanged();
                        }
                        // add to the list
                        if (!checkedPositions.contains(Integer.valueOf(pos))) {
                            checkedPositions.add(Integer.valueOf(pos));
                        }
                    } else {
                        checkedPositions.remove(Integer.valueOf(pos));
                    }
                }
            });
            checkBox.setChecked(checkedPositions.contains(Integer.valueOf(pos)));
            isBinding = false;
        }
    }


}
