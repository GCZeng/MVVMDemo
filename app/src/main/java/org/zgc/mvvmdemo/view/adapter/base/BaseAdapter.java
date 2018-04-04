package org.zgc.mvvmdemo.view.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;


/**
 * Created by Nick on 2017/2/6
 */
public abstract class BaseAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    protected Context mContext = null;
    protected List mData = null;

    public BaseAdapter(Context context, List data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    protected Object getData(int position) {
        return mData == null ? null : mData.get(position);
    }

    public void refreshData(List data) {
        if (mData != null) {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void loadMoreData(List data) {
        if (mData != null) {
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }
}

