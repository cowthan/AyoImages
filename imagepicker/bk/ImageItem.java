package com.lzy.imagepicker.bean;

import com.lzy.imagepicker.ImageLang;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧 Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：图片信息
 * 修订历史：
 * ================================================
 */
public class ImageItem extends ImageLang.MediaInfo implements Serializable {

    public ImageItem() {
        super();
    }

    public ImageItem(String url, boolean status) {
        super(url, status);
    }
}
