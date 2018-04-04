package org.zgc.mvvmdemo.view.adapter.base;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Author: zgc
 * Time: 2018/4/4 下午2:20
 * Description:
 **/
public class BaseViewHolder<B extends ViewDataBinding> extends RecyclerView.ViewHolder {
    /**
     * ViewDataBinding
     */
    private B mBinding;


    public BaseViewHolder(B binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    /**
     * @return viewDataBinding
     */
    public B getBinding() {
        return mBinding;
    }

}
