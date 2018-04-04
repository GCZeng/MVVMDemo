package org.zgc.mvvmdemo.viewmodel.base;

/**
 * Author: zgc
 * Time: 2018/4/4 下午2:28
 * Description:
 **/
public interface IBaseViewModel<V> {

    /**
     * Binds presenter with a view when resumed. The Presenter will perform initialization here.
     *
     * @param view the view associated with this presenter
     */
    void takeView(V view);

    /**
     * Drops the reference to the view when destroyed
     */
    void dropView();

    void init();

}
