package org.zgc.mvvmdemo.viewmodel;

import org.zgc.mvvmdemo.view.activity.iview.IPhotoView;

/**
 * Author: zgc
 * Time: 2018/4/4 下午5:08
 * Description:
 **/
public class PhotoViewVM  {
    private IPhotoView mPhotoView;

    public PhotoViewVM(IPhotoView mPhotoView) {
        this.mPhotoView = mPhotoView;
    }
}
