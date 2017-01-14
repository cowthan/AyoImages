package com.lzy.imagepickerdemo.pick_enter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.learnncode.mediachooser.MediaChooser;
import com.lzy.imagepicker.ImageLang;
import com.lzy.imagepickerdemo.ImagePickerDelegate;
import com.lzy.imagepickerdemo.R;
import com.lzy.imagepickerdemo.wxdemo.ImagePickerAdapter;

import org.ayo.file.Files;
import org.ayo.lang.Lang;
import org.ayo.notify.toaster.Toaster;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：ikkong （ikkong@163.com），修改 jeasonlzy（廖子尧）
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：
 * 修订历史：微信图片选择的Adapter, 感谢 ikkong 的提交
 * ================================================
 */
public class TimelineCreatePage_mediachooser_single_crop extends BaseTimelineCreatePage implements ImagePickerAdapter.OnRecyclerViewItemClickListener {

    ImagePickerDelegate imagePickerDelegate;

    protected void initImagePicker() {
    }

    protected void pick(){
        imagePickerDelegate = ImagePickerDelegate.attach(getActivity())
                .albumDefaultDir("")
                .cameraOutputDir(Files.path.getCameraPath())
                .shouldCompress(false)
                .imageLoader(new MediaChooser.IImageLoader() {
                    @Override
                    public void loadImage(Context context, ImageView imageView, String path) {
                        Glide.with(context)                             //配置上下文
                                .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                                .error(R.mipmap.default_image)           //设置错误图片
                                .placeholder(R.mipmap.default_image)     //设置占位图片
                                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                                .into(imageView);
                    }
                })
                .callback(new ImagePickerDelegate.Callback() {
                    @Override
                    public void onImagesComing(List<String> newSelectedImages) {
                        if(Lang.isNotEmpty(newSelectedImages)){
                            Toaster.toastShort("选了" + Lang.count(newSelectedImages) + "张图");
                            ArrayList<ImageLang.MediaInfo> images = new ArrayList<ImageLang.MediaInfo>();
                            for(String s: newSelectedImages){
                                ImageLang.MediaInfo imageItem = new ImageLang.MediaInfo();
                                imageItem.url = s;
                                images.add(imageItem);
                            }
                            selImageList.addAll(images);
                            adapter.setImages(selImageList);
                        }else{
                            Toaster.toastShort("没数据");
                        }
                    }
                });
        imagePickerDelegate.pickSingle(true);
    }
    protected void onActivityResult2(int requestCode, int resultCode, Intent data){
        imagePickerDelegate.onActivityResult(requestCode, resultCode, data);
    }

}
