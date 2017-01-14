package com.lzy.imagepicker;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Image相关工具类
 */

public class ImageLang {

    private static final String[] PROJECTION_BUCKET_IMG = {
            MediaStore.Images.ImageColumns.BUCKET_ID,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATA
    };
    private final String[] IMAGE_PROJECTION = {     //查询图片需要的数据列
            MediaStore.Images.Media.DATA,           //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
            MediaStore.Images.Media.SIZE,           //图片的大小，long型  132492
            MediaStore.Images.Media.WIDTH,          //图片的宽度，int型  1920
            MediaStore.Images.Media.HEIGHT,         //图片的高度，int型  1080
            MediaStore.Images.Media.MIME_TYPE,      //图片的类型     image/jpeg
            MediaStore.Images.Media.DATE_ADDED};    //图片被添加的时间，long型  1450518608



    private static final String[] PROJECTION_BUCKET_VEDIO = {
            MediaStore.Video.VideoColumns.BUCKET_ID,
            MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Video.VideoColumns.DATA,
    };

    private static final int INDEX_BUCKET_ID     = 0;
    private static final int INDEX_BUCKET_NAME   = 1;
    private static final int INDEX_BUCKET_URL    = 2;

    public static class Dir  implements Serializable{
        public String bucketName;
        public int bucketId;
        public String bucketUrl = null;

        public Dir(int id, String name, String url) {
            bucketId = id;
            bucketName = ensureNotNull(name);
            bucketUrl = url;
        }

        @Override
        public int hashCode() {
            return bucketId;
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Dir)) return false;
            Dir entry = (Dir) object;
            return bucketId == entry.bucketId;
        }

        public static String ensureNotNull(String value) {
            return value == null ? "" : value;
        }
    }

    public static class MediaInfo implements Serializable{

        public String url = null;
        public boolean status = false;

        public String name;       //图片的名字
        public long size;         //图片的大小
        public int width;         //图片的宽度
        public int height;        //图片的高度
        public String mimeType;   //图片的类型
        public long addTime;      //图片的创建时间

        /** 图片的路径和创建时间相同就认为是同一张图片 */
        @Override
        public boolean equals(Object o) {
            try {
                MediaInfo other = (MediaInfo) o;
                return this.url.equalsIgnoreCase(other.url) && this.addTime == other.addTime;
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
            return super.equals(o);
        }

        public MediaInfo(String url, boolean status) {
            this.url = url;
            this.status = status;
        }
        public MediaInfo() {
        }
    }

    public interface DirFilter{
        //if(entry.bucketUrl.contains("daogou") || entry.bucketUrl.contains("revoeye")){return false;}
        boolean access(Dir dir);
    }

    public interface ImageFilter{
        boolean access(MediaInfo image);
    }

    /**
     * 获取所有包含图片的目录
     * @param context
     * @param dirFilter
     * @return
     */
    public static List<Dir> getImageDirs(Context context, final DirFilter dirFilter){
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor mCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION_BUCKET_IMG, null, null, orderBy + " DESC");
        ArrayList<Dir> buffer = new ArrayList<Dir>();
        try {
            while (mCursor.moveToNext()) {
                Dir entry = new Dir(
                        mCursor.getInt(INDEX_BUCKET_ID),
                        mCursor.getString(INDEX_BUCKET_NAME),
                        mCursor.getString(INDEX_BUCKET_URL));

                if (! buffer.contains(entry)) {
                    Log.i("bucket", entry.bucketName + "--" + entry.bucketUrl);
                    if(dirFilter != null && dirFilter.access(entry)){
                        buffer.add(entry);
                    }
                }
            }

            return buffer;

        } finally {
            mCursor.close();
        }
    }

    public static List<Dir> getVedioDirs(Context context, final DirFilter dirFilter){
        final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        Cursor mCursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, PROJECTION_BUCKET_VEDIO, null, null, orderBy + " DESC");
        ArrayList<Dir> buffer = new ArrayList<Dir>();
        try {
            while (mCursor.moveToNext()) {
                Dir entry = new Dir(
                        mCursor.getInt(INDEX_BUCKET_ID),
                        mCursor.getString(INDEX_BUCKET_NAME),mCursor.getString(INDEX_BUCKET_URL));

                if (! buffer.contains(entry)) {
                    Log.i("bucket", entry.bucketName + "--" + entry.bucketUrl);
                    if(dirFilter != null && dirFilter.access(entry)){
                        buffer.add(entry);
                    }
                }
            }

            return buffer;

        } finally {
            mCursor.close();
        }
    }

    /**
     * 获取指定目录下的图片列表，按事件降序排
     * @param context
     * @param root   根目录，前后不要斜杠
     * @param imageFilter
     * @return
     */
    public static List<MediaInfo> getImages(Context context, String root, final ImageFilter imageFilter){
        Cursor mImageCursor = null;
        List<MediaInfo> list = new ArrayList<>();
        try {
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            String bucket = root;
            String searchParams = "bucket_display_name = \"" + bucket + "\"";

//            final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };

            final String[] columns = {     //查询图片需要的数据列
                    MediaStore.Images.Media.DATA,           //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
                    MediaStore.Images.Media.SIZE,           //图片的大小，long型  132492
                    MediaStore.Images.Media.WIDTH,          //图片的宽度，int型  1920
                    MediaStore.Images.Media.HEIGHT,         //图片的高度，int型  1080
                    MediaStore.Images.Media.MIME_TYPE,      //图片的类型     image/jpeg
                    MediaStore.Images.Media.DATE_ADDED};    //图片被添加的时间，long型  1450518608

            if(root == null || root.equals("")){
                mImageCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");
            }else{
                mImageCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, searchParams, null, orderBy + " DESC");
            }

            if (mImageCursor.getCount() > 0) {
                for (int i = 0; i < mImageCursor.getCount(); i++) {
                    mImageCursor.moveToPosition(i);
                    String path = mImageCursor.getString(mImageCursor.getColumnIndex(MediaStore.Images.Media.DATA)).toString();
                    MediaInfo img = new MediaInfo(path, false);

                    String imageName = mImageCursor.getString(mImageCursor.getColumnIndexOrThrow(columns[0]));
                    String imagePath = mImageCursor.getString(mImageCursor.getColumnIndexOrThrow(columns[1]));
                    long imageSize = mImageCursor.getLong(mImageCursor.getColumnIndexOrThrow(columns[2]));
                    int imageWidth = mImageCursor.getInt(mImageCursor.getColumnIndexOrThrow(columns[3]));
                    int imageHeight = mImageCursor.getInt(mImageCursor.getColumnIndexOrThrow(columns[4]));
                    String imageMimeType = mImageCursor.getString(mImageCursor.getColumnIndexOrThrow(columns[5]));
                    long imageAddTime = mImageCursor.getLong(mImageCursor.getColumnIndexOrThrow(columns[6]));
                    //封装实体
                    img.name = imageName;
                    img.size = imageSize;
                    img.width = imageWidth;
                    img.height = imageHeight;
                    img.mimeType = imageMimeType;
                    img.addTime = imageAddTime;

                    if(imageFilter != null && imageFilter.access(img)){
                        list.add(img);
                    }
                }
            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mImageCursor.close();
        }
        return list;
    }

    public static List<MediaInfo> getVedios(Context context, String root, final ImageFilter imageFilter){
        Cursor mImageCursor = null;
        List<MediaInfo> list = new ArrayList<>();
        try {
            final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
            String bucket = root;
            String searchParams = "bucket_display_name = \"" + bucket + "\"";

            final String[] columns = { MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID };
            if(root == null || root.equals("")){
                mImageCursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");
            }else{
                mImageCursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, searchParams, null, orderBy + " DESC");
            }

            if (mImageCursor.getCount() > 0) {
                for (int i = 0; i < mImageCursor.getCount(); i++) {
                    mImageCursor.moveToPosition(i);
                    int dataColumnIndex = mImageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    String path = mImageCursor.getString(dataColumnIndex).toString();
                    MediaInfo img = new MediaInfo(path, false);
                    if(imageFilter != null && imageFilter.access(img)){
                        list.add(img);
                    }
                }
            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mImageCursor.close();
        }
        return list;
    }
}
