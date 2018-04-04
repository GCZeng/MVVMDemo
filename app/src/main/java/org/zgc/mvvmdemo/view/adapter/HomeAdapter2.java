package org.zgc.mvvmdemo.view.adapter;

import android.databinding.DataBindingUtil;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.zgc.mvvmdemo.BR;
import org.zgc.mvvmdemo.R;
import org.zgc.mvvmdemo.bean.entity.GankData;
import org.zgc.mvvmdemo.databinding.RvHomeItemBinding;
import org.zgc.mvvmdemo.util.image.ImageLoader;
import org.zgc.mvvmdemo.util.image.ImageManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Nick on 2017/2/6
 */
public class HomeAdapter2 extends BaseQuickAdapter<GankData, BaseViewHolder> {

    public HomeAdapter2(List<GankData> data) {
        super(R.layout.rv_home_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GankData gankData) {

        RvHomeItemBinding binding = DataBindingUtil.bind(helper.itemView);
        binding.setVariable(BR.gankData, gankData);
        binding.setVariable(BR.position, helper.getAdapterPosition());
        binding.setVariable(BR.adapter, this);
        binding.executePendingBindings(); //防止闪烁

        helper.addOnClickListener(R.id.iv_pic);

    }


}
