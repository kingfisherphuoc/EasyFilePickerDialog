package easyfilepickerdialog.kingfisher.com.library.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by kingfisher_goong on 11/15/16.
 */

public abstract class BaseRecyclerArrayAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> implements Filterable {


    public abstract BaseViewHolder<T> getViewHolder(View view);

    public boolean isMeetCondition(T data, String sequenceLowerCase) {
        return false;
    }

    protected List<T> backupList;
    protected List<T> list;
    protected int layoutId;


    public BaseRecyclerArrayAdapter(List<T> list, int layoutId) {
        this.list = list;
        this.layoutId = layoutId;
        this.backupList = new ArrayList<>(list);
    }


    @Override
    public void onBindViewHolder(BaseViewHolder<T> holder, int position) {
        holder.setData(getItem(position), position);
    }

    public T getItem(int post) {
        if (this.list == null || post < 0 || post >= this.list.size()) {
            return null;
        }
        return list.get(post);
    }

    /**
     * Thêm dữ liệu mới vào adapter + back-up dữ liệu để search
     *
     * @param data
     */
    public void addItem(T data) {
        list.add(0, data);
        backupList.add(0, data);
        notifyItemInserted(0);

    }

    /**
     * Thêm 1 list dữ liệu mới vào adapter + back-up dữ liệu để search
     *
     * @param datas
     */
    public void addItem(Collection<T> datas) {
        int count = list.size();
        list.addAll(datas);
        backupList.addAll(datas);
        notifyItemRangeInserted(count, datas.size());
    }

    /**
     * XÓa hết dữ liệu (bao gồm cả dữ liệu back-up)
     */
    public void clear() {
        int numberOfItems = list.size();
        list.clear();
        backupList.clear();
        notifyItemRangeRemoved(0, numberOfItems);
    }

    /**
     * Xóa 1 data khỏi adapter và back-up data
     *
     * @param data
     */
    public void removeItem(T data) {
        int index = this.list.indexOf(data);
        if (index == -1) {
            return;
        }
        list.remove(index);
        notifyItemRemoved(index);
        backupList.remove(data);
    }

    /**
     * Xóa 1 list data khỏi adapter và back-up data
     *
     * @param datas
     */
    public void removeItem(Collection<T> datas) {
        for (T data : datas) {
            removeItem(data);
        }
    }

    @Override
    public int getItemCount() {
        int size = list == null ? 0 : list.size();
        return size;
    }

    @Override
    public BaseViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return getViewHolder(view);
    }

    private Filter filter = new Filter() {
        List<T> resultsData = new ArrayList<>();
        FilterResults filterResults = new FilterResults();

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (TextUtils.isEmpty(charSequence)) {
                filterResults.values = new ArrayList<>(backupList);
            } else {
                filterResults.values = resultsData;
                resultsData.clear();
                String lowerCaseChar = charSequence.toString().toLowerCase();
                for (T data : backupList) {
                    if (isMeetCondition(data, lowerCaseChar)) {
                        resultsData.add(data);
                    }
                }
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (filterResults.values != null) {
                // clear all item
                int numberOfItems = list.size();
                list.clear();
                notifyItemRangeRemoved(0, numberOfItems);

                List<T> datas = (List<T>) filterResults.values;
                list.addAll(datas);
                notifyItemRangeInserted(0, datas.size());

            }
        }
    };

    @Override
    public Filter getFilter() {
        return filter;
    }
}
