package org.zgc.mvvmdemo.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.zgc.mvvmdemo.R;
import org.zgc.mvvmdemo.bean.entity.GankData;
import org.zgc.mvvmdemo.view.adapter.base.BaseAdapter;
import org.zgc.mvvmdemo.view.adapter.base.BaseViewHolder;

import java.util.List;

import org.zgc.mvvmdemo.BR;

/**
 * Created by Nick on 2017/2/6
 */
public class HomeAdapter extends BaseAdapter<BaseViewHolder> {

    public HomeAdapter(Context context, List<GankData> data) {
        super(context, data);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.rv_home_item, parent, false);
        return new BaseViewHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        ViewDataBinding binding = holder.getBinding();
        binding.setVariable(BR.gankData, getData(position));
        binding.setVariable(BR.position, position);
        binding.setVariable(BR.adapter, this);
        binding.executePendingBindings(); //防止闪烁
    }

}
