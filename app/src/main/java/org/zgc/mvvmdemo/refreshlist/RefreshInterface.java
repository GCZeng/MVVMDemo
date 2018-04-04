package org.zgc.mvvmdemo.refreshlist;

/**
 * Created by Nick on 2017/2/5
 */
public interface RefreshInterface {
    /**
     * 刷新
     */
    void refresh(RefreshList.Refresh refresh);
    /**
     * 刷新完成
     */
    void refreshComplete();
}
