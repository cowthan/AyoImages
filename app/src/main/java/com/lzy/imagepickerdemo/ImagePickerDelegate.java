package com.lzy.imagepickerdemo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.desmond.squarecamera.CameraActivity;
import com.learnncode.mediachooser.MediaChooser;
import com.learnncode.mediachooser.activity.DirectoryListActivity;

import org.ayo.bitmap.BitmapUtils;
import org.ayo.file.Files;
import org.ayo.lang.Lang;
import org.ayo.notify.actionsheet.ActionSheetDialog;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


/**
 * 相机和相册选择的代理，处理：
 * 选择相机还是相册
 * 打开相机，返回结果，拿到结果
 * 打开相册，返回结果，拿到结果
 *
 * 回调：onImagesComing
 *
 */
public class ImagePickerDelegate {


    ///选择图片和相机的相关常数
    public static final int REQUEST_CODE_ALBUM = 422;
    public static final int REQUEST_CODE_CAMERA = 423;
    public static final int REQUEST_CODE_CROP = 424;

    public static final int MAX_SIZE_M = 5;

    private Activity activity;
    private String photoDir;
    private String albumDefaultDir;
    private boolean shouldCompress;
    private Callback callback;
    private List<String> imagesSelected;
    private boolean needCrop = false;

    private MediaChooser.IImageLoader imageLoader;

    //private File outFile;

    public interface Callback{
        void onImagesComing(List<String> newSelectedImages);
    }

    public static ImagePickerDelegate attach(Activity activity) {
        ImagePickerDelegate i = new ImagePickerDelegate();
        i.imagesSelected = new ArrayList<>();
        i.activity = activity;
        return i;
    }


    public ImagePickerDelegate cameraOutputDir(String dir){
        this.photoDir = dir;
        return this;
    }

    public ImagePickerDelegate imageLoader(MediaChooser.IImageLoader imageLoader){
        this.imageLoader = imageLoader;
        return this;
    }

    public ImagePickerDelegate albumDefaultDir(String dir){
        this.albumDefaultDir = dir;
        return this;
    }

    public ImagePickerDelegate shouldCompress(boolean shouldCompress){
        this.shouldCompress = shouldCompress;
        return this;
    }

//    public ImagePickerDelegate needCrop(boolean needCrop){
//        this.needCrop = needCrop;
//        return this;
//    }

//    public ImagePickerDelegate maxCount(int maxCount){
//        this.maxCount = maxCount;
//        return this;
//    }

    public ImagePickerDelegate callback(Callback callback){
        this.callback = callback;
        return this;
    }

    public void pickSingle(final boolean needCrop){
        pickSingle(needCrop, true);
    }

    public void pickSingle(final boolean needCrop, boolean cancelable){
        imagesSelected = new ArrayList<>();
        new ActionSheetDialog(activity)
                .builder()
                .setCancelable(cancelable)
                .setCanceledOnTouchOutside(cancelable)
                .addSheetItem("相机", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {

                            @Override
                            public void onClick(int which) {
                                openCameraForResult(needCrop);
                            }
                        })
                .addSheetItem("从相册选择", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                               pickSingleImageFromAlbum(needCrop);
                            }
                        })
//                .setOnCancelCallback(new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                        callback.onImagesComing(Collections.EMPTY_LIST);
//                    }
//                })
                .show();
    }

    public void startForResult(final int max, final List<String> selected){

        imagesSelected = new ArrayList<>();
        new ActionSheetDialog(activity)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("相机", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {

                            @Override
                            public void onClick(int which) {

//                                String filename = System.currentTimeMillis()
//                                        + ".jpg";
//                                outFile = new File(Files.path.getPathInRoot2(filename));
//                                Config.savePhotoPath(outFile.getAbsolutePath());
//                                SystemIntent.openCamera(activity, outFile, C.REQUEST_CODE_CAMERA);
                                Intent startCustomCameraIntent = new Intent(activity, CameraActivity.class);
                                activity.startActivityForResult(startCustomCameraIntent, REQUEST_CODE_CAMERA);
                            }
                        })
                .addSheetItem("从相册选择", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                //---
                                String p = Files.path.getCameraPath();
                                //MediaChooser.setSelectionLimit(max);
                                MediaChooser.setiImageLoader(imageLoader);
                                Intent intent = new Intent(activity, DirectoryListActivity.class);
                                intent.putExtra("name", p);
                                intent.putExtra("max", max);
                                intent.putExtra("selected", (ArrayList<String>)selected);
                                activity.startActivityForResult(intent, MediaChooser.REQ_PICK_IMAGE);
                                //---
                            }
                        })
//                .setOnCancelCallback(new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                        callback.onImagesComing(Collections.EMPTY_LIST);
//                    }
//                })
                .show();
    }

    public void pickSingleImageFromAlbum(boolean needCrop){
        imagesSelected = new ArrayList<>();
        this.needCrop = needCrop;
        String p = Files.path.getCameraPath();
        //MediaChooser.setSelectionLimit(max);

        MediaChooser.setiImageLoader(imageLoader);
        Intent intent = new Intent(activity, DirectoryListActivity.class);
        intent.putExtra("name", p);
        intent.putExtra("max", 1);
        intent.putExtra("selected", new ArrayList<String>());
        activity.startActivityForResult(intent, MediaChooser.REQ_PICK_IMAGE);
    }

    public void openCameraForResult(boolean needCrop){
        imagesSelected = new ArrayList<>();
        this.needCrop = needCrop;
        Intent startCustomCameraIntent = new Intent(activity, CameraActivity.class);
        activity.startActivityForResult(startCustomCameraIntent, REQUEST_CODE_CAMERA);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 根据选择完毕的图片返回值，直接上传文件
        if (requestCode == MediaChooser.REQ_PICK_IMAGE && data != null) {

            ArrayList<String> pathList = MediaChooser.getSelectedImages(data); //data.getStringArrayListExtra("data");
            if (pathList != null && pathList.size() > 0) {
                int intSize = pathList.size();

                for (int i = 0; i <= intSize - 1; i++) {
                    String localStrPath = pathList.get(i);

                    if(shouldCompress){
                        ///压缩
                        String pathAfterCompress = compress(localStrPath, MAX_SIZE_M);

                        if(Lang.isEmpty(pathAfterCompress)){
                            imagesSelected.add(localStrPath);
                        }else{
                            imagesSelected.add(pathAfterCompress);
                        }

                    }else{
                        imagesSelected.add(localStrPath);
                    }
                }
                if(needCrop){
                    startPhotoZoom(Uri.fromFile(new File(imagesSelected.get(0))));
                }else{
                    callback.onImagesComing(Collections.unmodifiableList(imagesSelected));
                }
            }else{
                //nothing selected
                callback.onImagesComing(Collections.EMPTY_LIST);
            }
        }

        if(requestCode == REQUEST_CODE_CAMERA){

            if(resultCode != Activity.RESULT_OK){
                //SBToast.toastShort(agent.getActivity(), "拍照失败？？");
                return;
            }

            if(data == null) return;
            Uri photoUri = data.getData();
            if(needCrop){
                startPhotoZoom(photoUri);
                return;
            }else{

            }

            String localStrPath = new File(URI.create(photoUri.toString())).getAbsolutePath();
//            if(outFile != null) localStrPath = outFile.getAbsolutePath();
//            else localStrPath = Config.getPhotoPath();
            if(Lang.isEmpty(localStrPath)){
                return;
            }
            File newPhoto = new File(localStrPath);
            if(!newPhoto.exists()){
                return;
            }else{
            }

            if(shouldCompress){
                ///压缩
                String pathAfterCompress = compress(localStrPath, MAX_SIZE_M);

                if(Lang.isEmpty(pathAfterCompress)){
                    imagesSelected.add(localStrPath);
                }else{
                    imagesSelected.add(pathAfterCompress);
                }

            }else{
                imagesSelected.add(localStrPath);
            }

            callback.onImagesComing(Collections.unmodifiableList(imagesSelected));
        }

        //剪切返回
        if(requestCode == 3){
            Bundle extras = data.getExtras();

            Bitmap bitmap = extras.getParcelable("data");
            Uri uri2 = Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, null, null));// data.getData();

            String[] pojo = { MediaStore.Images.Media.DATA };
            Cursor cursor = activity.managedQuery(uri2, pojo, null, null, null);

            if (cursor != null) {
                ContentResolver cr = activity.getContentResolver();
                int colunm_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(colunm_index);

                imagesSelected.add(path);
                callback.onImagesComing(Collections.unmodifiableList(imagesSelected));
            }
        }

    }


    public static String compress(String src, int maxMB){
        //log.i("downloader--", new File(src).length() + "---" + MAX_SIZE_M * 1024 * 1024);
        if(new File(src).length() <= maxMB * 1024 * 1024){
            //log.i("downloader--", "不用压缩");
            return src;
        }

        ////取出exif--旋转信息
        int exifOrientation = 0;
        try {
            ExifInterface exif = new ExifInterface(src);
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String suffix = src.substring(src.lastIndexOf(".")+1);
        String pathAfterCompress = "";
        byte[] compressBitmap = BitmapUtils.compressBitmap(1000*1000, src);
        if (null != compressBitmap) {
            Bitmap bmPhoto = BitmapUtils
                    .Bytes2Bimap(compressBitmap);
            if (null != bmPhoto) {
                String strTempPhotoPath;
                try {
                    strTempPhotoPath = BitmapUtils
                            .saveFile(bmPhoto,
                                    UUID.randomUUID()
                                            + "." + suffix);
                    if(bmPhoto != null){
                        bmPhoto.recycle();
                        bmPhoto = null;
                    }
                    if (null != strTempPhotoPath
                            && !"".equals(strTempPhotoPath)) {
                        pathAfterCompress = strTempPhotoPath;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
        ///~~~压缩结束

        if(pathAfterCompress != null && !pathAfterCompress.equals("")){

//            Log.i("sfsfsfsfsdfsf", "考虑了旋转--压缩完路径--" + pathAfterCompress + "--" + exifOrientation);
            if(exifOrientation != 0){
                try {
                    ExifInterface exif = new ExifInterface(pathAfterCompress);
                    exif.setAttribute(ExifInterface.TAG_ORIENTATION, exifOrientation+"");
                    exif.saveAttributes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return pathAfterCompress;
        }else{
            return "";
        }
    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++
		 * 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
		 */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, 3);
    }
}
