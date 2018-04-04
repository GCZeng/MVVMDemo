package org.zgc.mvvmdemo.refreshlist;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by Nick on 2017/2/5
 */
public class RefreshList extends SwipeRefreshLayout implements RefreshInterface {


    public RefreshList(Context context) {
        super(context);
    }

    public RefreshList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void refresh(Refresh refresh) {
        if (refresh != null) {
            setOnRefreshListener(() -> refresh.refreh());
        }
    }

    @Override
    public void refreshComplete() {
        setRefreshing(false);
    }

    public interface Refresh {
        void refreh();
    }

}
