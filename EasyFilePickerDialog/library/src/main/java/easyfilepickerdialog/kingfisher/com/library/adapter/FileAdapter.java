package easyfilepickerdialog.kingfisher.com.library.adapter;

import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import easyfilepickerdialog.kingfisher.com.library.R;

/**
 * Created by kingfisher on 5/22/17.
 */

public class FileAdapter extends BaseRecyclerArrayAdapter<File> {

    private BaseViewHolder.OnViewHolderClickListener onViewHolderClickListener;

    public FileAdapter(List<File> list, BaseViewHolder.OnViewHolderClickListener onViewHolderClickListener) {
        super(list, R.layout.item_file_picker);
        this.onViewHolderClickListener = onViewHolderClickListener;
    }


    @Override
    public BaseViewHolder<File> getViewHolder(View view) {
        return new ViewHolder(view, this.onViewHolderClickListener);
    }

    class ViewHolder extends BaseViewHolder<File> {
        TextView tvName;

        public ViewHolder(View itemView, OnViewHolderClickListener onViewHolderClickListener) {
            super(itemView, onViewHolderClickListener);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
        }

        @Override
        public void setData(File data, int pos) {
            if (pos == 0) {
                tvName.setText("Up...");
            } else {
                tvName.setText(data.isDirectory() ? "Folder: " + data.getName() : data.getName());
            }
        }
    }
}
