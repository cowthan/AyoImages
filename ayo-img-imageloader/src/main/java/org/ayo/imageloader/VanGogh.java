package org.ayo.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.L;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


/**
 * This class provided an interface which would be implemented by any third party image library.
 * Now the implementation is UniveralImageDownloader
 * 
 */
public class VanGogh {
	
	private static int BIG_LOADING = 0;
	private static int BIG_ERROR = 0;
	private static int BIG_EMPTY = 0;
	private static int MIDDLE_LOADING = 0;
	private static int MIDDLE_ERROR = 0;
	private static int MIDDLE_EMPTY = 0;
	private static int SMALL_LOADING = 0;
	private static int SMALL_ERROR = 0;
	private static int SMALL_EMPTY = 0;
	
	/**
	 * init the module with some default base parameters
	 * @param context
	 */
	public static void init(Context context, String cacheDir){
		DisplayImageOptions.Builder opt = new DisplayImageOptions.Builder();
		//opt.bitmapConfig(Bitmap.Config.ALPHA_8);
//		opt.cacheInMemory(true);
//		opt.cacheOnDisk(true);
		opt.considerExifParams(true);
		//opt.decodingOptions();- //?????
		//opt.delayBeforeLoading(delayInMillis);
		//opt.delayBeforeLoading(300);
		opt.displayer(new FadeInBitmapDisplayer(500));//CircleBitmapDisplayer, RoundedBitmapDisplayer, RoundedVignetteBitmapDisplayer, SimpleBitmapDisplayer
		//opt.extraForDownloader(Object);
		//opt.handler(Handler);
		//opt.imageScaleType(ImageView.ScaleType)

		//opt.preProcessor(BitmapProcessor);
		//opt.postProcessor(BitmapProcessor);
		//opt.resetViewBeforeLoading(true);
		//opt.showImageOnLoading(int|Drawable);
		//opt.showImageOnFail(int|Drawable);
		//opt.showImageForEmptyUri(int|Drawable);

		ImageLoaderConfiguration config =
				null;
		config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCache(new UnlimitedDiskCache(new File(cacheDir)))
						//.diskCacheFileNameGenerator(new SimpleFileNameGenerator())
				.memoryCacheExtraOptions(480, 800)
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
						//.diskCacheSize(50 * 1024 * 1024) // 100
						//.diskCacheFileNameGenerator(new Md5FileNameGenerator())
						//.diskCacheFileCount(500)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove
				.threadPoolSize(2)
				.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout-5s, readTimeout-30s
				.defaultDisplayImageOptions(opt.build())
				.build();
		ImageLoader.getInstance().init(config);
	}
	
	/**
	 * init the replace image for big image
	 */
	public static void initImageBig(int loading, int error, int empty){
		BIG_LOADING = loading;
		BIG_ERROR = error;
		BIG_EMPTY = empty;
	}
	
	/**
	 * init the replace image for middle image
	 */
	public static void initImageMiddle(int loading, int error, int empty){
		MIDDLE_LOADING = loading;
		MIDDLE_ERROR = error;
		MIDDLE_EMPTY = empty;
	}
	
	/**
	 * init the replace image for small image
	 */
	public static void initImageSmall(int loading, int error, int empty){
		SMALL_LOADING = loading;
		SMALL_ERROR = error;
		SMALL_EMPTY = empty;
	}
	
	/**
	 * init the replace image for big image
	 */
	public static void initImageBig(int img){
		initImageBig(img, img, img);
	}
	
	/**
	 * init the replace image for middle image
	 */
	public static void initImageMiddle(int img){
		initImageMiddle(img, img, img);
	}
	
	/**
	 * init the replace image for small image
	 */
	public static void initImageSmall(int img){
		initImageSmall(img, img, img);
	}
	
	
	DisplayImageOptions options;
	DisplayImageOptions.Builder b = new DisplayImageOptions.Builder();
	
	ImageView iv;
	private VanGogh(ImageView iv){
		this.iv = iv;
		b = new DisplayImageOptions.Builder();
		b.cacheOnDisk(true)
			.considerExifParams(true);
	}
	
	public VanGogh imageLoading(int resId){
		b.showImageOnLoading(resId);
		return this;
	}
	
	public VanGogh imageError(int resId){
		b.showImageOnFail(resId);
		return this;
	}
	
	public VanGogh imageEmpty(int resId){
		b.showImageForEmptyUri(resId);
		return this;
	}
	
	public static VanGogh paper(ImageView iv){
		return new VanGogh(iv);
	}

	public void paint(String url, ImageLoadingListener listener, ImageLoadingProgressListener progressListener){

		//Log.i("downloader--1", "显示啊--" + url);
		options = b.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(1500))
				.build();
		
		if(iv == null || b == null){
			//throw new RuntimeException("Illegal Url");
		}

		if(url == null || url.equals("")){
			//throw new RuntimeException("Url is null);
			return;
		}
		
		if(isNotLocalPathOrRemotePath(url)){
			//throw new RuntimeException("Url is either local path nor remote url, cannot load image：" + url);
			return;
		}
		
		if(isLocalPath(url)){
			url = Uri.fromFile(new File(url)).toString();
			url = URLDecoder.decode(url);  /// Uri.fromFile would encdoe the chinese charators in url, should be decoded back
		}
		//Log.i("downloader--2", "显示啊--" + url);
		ImageLoader.getInstance().displayImage(url, iv, options, listener, progressListener);
	}
	
	public void paintBigImage(String url, ImageLoadingListener listener){
		
		this.imageEmpty(BIG_EMPTY)
		   .imageError(BIG_ERROR)
		   .imageLoading(BIG_LOADING)
		   .paint(url, listener, null);
	}

	public void justPaint(String url, ImageLoadingListener listener){

		this.imageEmpty(0)
				.imageError(0)
				.imageLoading(0)
				.paint(url, listener, null);
	}
	
	public void paintMiddleImage(String url, ImageLoadingListener listener){
		
		this.imageEmpty(MIDDLE_EMPTY)
		   .imageError(MIDDLE_ERROR)
		   .imageLoading(MIDDLE_LOADING)
		   .paint(url, listener,null);
	}

	public void paintSmallImage(String url, ImageLoadingListener listener){
		
		this.imageEmpty(SMALL_EMPTY)
		   .imageError(SMALL_ERROR)
		   .imageLoading(SMALL_LOADING)
		   .paint(url, listener,null);
	}
	
	
	public static boolean isLocalPath(String url){
		if(!url.contains("http://") && !url.contains("https://") && !url.contains("file://") && !url.contains("files://")){
			return true;
		}else{
			return false;
		}
	}
	
	private static boolean isNotLocalPathOrRemotePath(String url){
		if(url == null || url.equals("")){
			return true;
		}
		if(!url.startsWith("/") && !url.startsWith("http")){
			return true;
		}else{
			return false;
		}
	}
	
	
	////---------------------------------------------------
	public static void loadImage(String url, final ImageLoadingListener listener, final ImageLoadingProgressListener progressListener){
		
//		File f = ImageLoader.getInstance().getDiskCache().get(url);
//		if(f != null && f.exists()){
//			f.renameTo(new File(savePath));
//			if(listener != null) {
//				listener.onLoadingComplete(url, null, null);
//			}
//			return;
//		}
		//loadImage(uri, targetImageSize, options, listener, progressListener)
		ImageLoader.getInstance().loadImage(url, null, null, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String imageUri, View view) {
				if (listener != null) {
					listener.onLoadingStarted(imageUri, view);
				}
			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
										FailReason failReason) {
				if (listener != null) {
					listener.onLoadingFailed(imageUri, view, failReason);
				}
			}

			@Override
			public void onLoadingComplete(final String imageUri, final View view, final Bitmap loadedImage) {
				try {
					ImageLoader.getInstance().getDiskCache().save(imageUri, loadedImage);
//					if(savePath != null && !savePath.endsWith("")){
//						File f = new File(savePath);
//						if (!f.exists()) {
//							f.createNewFile();
//						}
//						FileOutputStream out = new FileOutputStream(f);
//
//						loadedImage.compress(Bitmap.CompressFormat.PNG, 100, out);
//						out.flush();
//						out.close();
//					}

					if (listener != null) {
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								listener.onLoadingComplete(imageUri, view, loadedImage);
							}
						}, 500);
					}


				} catch (Exception e) {
					e.printStackTrace();
					if (listener != null) {
						listener.onLoadingFailed(imageUri, view, new FailReason(FailReason.FailType.IO_ERROR, e));
					}
				}
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				if (listener != null) {
					listener.onLoadingCancelled(imageUri, view);
				}
			}
		}, new ImageLoadingProgressListener() {
			@Override
			public void onProgressUpdate(String imageUri, View view, int current, int total) {
				progressListener.onProgressUpdate(imageUri, view, current, total);
			}
		});
	}
	
	
	///--------------------------
	
	public static int getExifOrientation(String filepath) {  
	    int degree = 0;  
	    ExifInterface exif = null;  
	    try {  
	        exif = new ExifInterface(filepath);  
	    } catch (IOException ex) {  
	        ex.printStackTrace();
	    }  
	    if (exif != null) {  
		    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);  
	        if (orientation != -1) {  
	            switch(orientation) {  
	                case ExifInterface.ORIENTATION_ROTATE_90:  
	                    degree = 90;  
	                    break;  
	                case ExifInterface.ORIENTATION_ROTATE_180:  
	                    degree = 180;  
	                    break;  
	                case ExifInterface.ORIENTATION_ROTATE_270:  
	                    degree = 270;  
	                    break;  
	            }  
	        }  
	    }  
	    return degree;  
	}  
	
	/**
	 * rotate bitmap
	 *
	 * @param bm
	 * @param degree
	 * @return new bitmap which is rotate from bm
	 */
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		if(degree == 0 || degree == -1){
			return bm;
		}
	    Bitmap returnBm = null;
	  
	    // generate the matrix
	    Matrix matrix = new Matrix();
	    matrix.postRotate(degree);
	    try {
	        returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
	    } catch (OutOfMemoryError e) {
	    	e.printStackTrace();
	    }
	    if (returnBm == null) {
	        returnBm = bm;
	    }
	    if (bm != returnBm) {
	        bm.recycle();
	    }
	    return returnBm;
	}
	
	/**
	 * get the local cache path of the url
	 * @param url
	 * @return
	 */
	public static String getLocalCachePath(String url){
		try {
			return DiskCacheUtils.findInCache(url, ImageLoader.getInstance().getDiskCache()).getAbsolutePath();
			//return ImageLoader.getInstance().getDiskCache().get(url).getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getUri(String path){
		File f = new File(path);
		if(f.exists()){
			String s = Uri.fromFile(f).toString();
			try {
				s = URLDecoder.decode(s, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return s;
		}else{
			return null;
		}
	}

	public static Uri toUri(String uri){
		if(uri == null || uri.equals("")){
			return null;
		}else{
			if(isLocalPath(uri)){
				String s = Uri.fromFile(new File(uri)).toString();
				uri = s;
			}
			return Uri.parse(uri);
		}
	}

	public static boolean isHttpUrl(String uri){
		if(uri != null && uri.startsWith("http")){
			return true;
		}else{
			return false;
		}
	}

	public static boolean isValidUri(String uri){
		return isHttpUrl(uri) || isLocalPath(uri) || isLocalUri(uri);
	}
	public static boolean isLocalUri(String uri){
		if(uri != null && uri.startsWith("file")){
			return true;
		}else{
			return false;
		}
	}
	public static boolean isValidResourceId(int id){
		return id > 0;
	}


	public static int[] getImageSize(String path){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, options);
        /* 这里返回的bmp是null *
        这段代码之后，options.outWidth 和 options.outHeight就是我们想要的宽和高了。+ q,
        */

		return new int[]{options.outWidth, options.outHeight};

	}

	public static int getRotate(String path){
		int rotation = 0;
		boolean flip = false;
		try {
			ExifInterface exif = new ExifInterface(path);
			int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (exifOrientation) {
				case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
					flip = true;
				case ExifInterface.ORIENTATION_NORMAL:
					rotation = 0;
					break;
				case ExifInterface.ORIENTATION_TRANSVERSE:
					flip = true;
				case ExifInterface.ORIENTATION_ROTATE_90:
					rotation = 90;
					break;
				case ExifInterface.ORIENTATION_FLIP_VERTICAL:
					flip = true;
				case ExifInterface.ORIENTATION_ROTATE_180:
					rotation = 180;
					break;
				case ExifInterface.ORIENTATION_TRANSPOSE:
					flip = true;
				case ExifInterface.ORIENTATION_ROTATE_270:
					rotation = 270;
					break;
			}
		} catch (IOException e) {
			L.w("Can't read EXIF tags from file [%s]", path);
		}
		return rotation;
	}

	public static int[] getImageSizeConsiderExif(String path){
		int[] imageSize = getImageSize(path);

		int rotation = 0;
		boolean flip = false;
		try {
			ExifInterface exif = new ExifInterface(path);
			int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (exifOrientation) {
				case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
					flip = true;
				case ExifInterface.ORIENTATION_NORMAL:
					rotation = 0;
					break;
				case ExifInterface.ORIENTATION_TRANSVERSE:
					flip = true;
				case ExifInterface.ORIENTATION_ROTATE_90:
					rotation = 90;
					break;
				case ExifInterface.ORIENTATION_FLIP_VERTICAL:
					flip = true;
				case ExifInterface.ORIENTATION_ROTATE_180:
					rotation = 180;
					break;
				case ExifInterface.ORIENTATION_TRANSPOSE:
					flip = true;
				case ExifInterface.ORIENTATION_ROTATE_270:
					rotation = 270;
					break;
			}
		} catch (IOException e) {
			L.w("Can't read EXIF tags from file [%s]", path);
		}

		if(rotation == 90 || rotation == 270){
			int x = imageSize[0];
			imageSize[0] = imageSize[1];
			imageSize[1] = x;
		}
		return imageSize;
	}

}
