package org.zgc.mvvmdemo.util.image;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.zgc.mvvmdemo.util.image.impl.GlideApp;

/**
 * Author: zgc
 * Time: 2018/4/4 下午2:56
 * Description:
 **/
public class ImageHelper {
    /**
     * @param imageView imageView
     * @param url       图片路径
     */
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        ImageLoader.with(imageView.getContext())
                .url(url)
                .into(imageView);
    }

    /**
     * @param imageView imageView
     * @param resId     resource id
     */
    @BindingAdapter({"resId"})
    public static void loadMipmapResource(ImageView imageView, int resId) {
        imageView.setImageResource(resId);
    }

}
