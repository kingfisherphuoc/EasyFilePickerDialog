package easyfilepickerdialog.kingfisher.com.library.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by kingfisher on 7/21/16.
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

    private OnViewHolderClickListener onViewHolderClickListener;

    public BaseViewHolder(View itemView, OnViewHolderClickListener onViewHolderClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        this.onViewHolderClickListener = onViewHolderClickListener;
    }

    public abstract void setData(T data, int position);

    @Override
    public void onClick(View v) {
        if (onViewHolderClickListener != null) {
            onViewHolderClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnViewHolderClickListener {
        void onItemClick(int position);
    }
}
