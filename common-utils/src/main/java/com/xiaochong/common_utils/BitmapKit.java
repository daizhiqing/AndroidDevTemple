package com.xiaochong.common_utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.xiaochong.common_utils.transformations.CropCircleTransformation;

/**
 * 作者：daizhiqing on 2018/3/8 00:50
 * 邮箱：daizhiqing8@163.com
 * 描述：图片加载工具类
 */
public class BitmapKit {

    public static void loadImage(ImageView view, String path) {
        if (view == null) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().dontAnimate();
        Glide.with(view.getContext())
                .load(path)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view);
    }

    /**
     * @param view
     * @param path    图片地址,网络
     * @param errorId 错误图片
     */
    public static void loadImage(ImageView view, String path, int errorId) {
        if (view == null) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().dontAnimate().error(errorId);
        Glide.with(view.getContext())
                .load(path)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view);
    }

    /**
     * @param view
     * @param path    图片地址,支持本地文件/网络
     * @param errorId 错误图片
     */
    public static void loadCircleImage(ImageView view, String path, int errorId) {
        RequestOptions requestOptions = new RequestOptions().error(errorId).bitmapTransform(new CropCircleTransformation(view.getContext()));
        Glide.with(view.getContext())
                .load(path)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view);
    }
}
