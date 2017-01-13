/*
 * Copyright 2013 - learnNcode (learnncode@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */


package com.learnncode.mediachooser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.learnncode.mediachooser.ImageLang;
import com.learnncode.mediachooser.R;
import com.learnncode.mediachooser.async.ImageLoadAsync;
import com.learnncode.mediachooser.async.MediaAsync;
import com.learnncode.mediachooser.async.VideoLoadAsync;
import com.learnncode.mediachooser.fragment.VideoFragment;

import java.util.List;

public class GridViewAdapter extends ArrayAdapter<ImageLang.MediaInfo> {
	public VideoFragment videoFragment;  

	private Context mContext;
	private List<ImageLang.MediaInfo> mGalleryModelList;
	private int mWidth;
	private boolean mIsFromVideo;
	LayoutInflater viewInflater;
	String type;
	

	public GridViewAdapter(Context context, int resource, List<ImageLang.MediaInfo> categories, boolean isFromVideo, String type) {
		super(context, resource, categories);
		mGalleryModelList = categories;
		mContext          = context;
		mIsFromVideo      = isFromVideo;
		viewInflater = LayoutInflater.from(mContext);
		this.type = type;
	}

	public int getCount() {
		return mGalleryModelList.size();
	}

	@Override
	public ImageLang.MediaInfo getItem(int position) {
		return mGalleryModelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {

			mWidth = mContext.getResources().getDisplayMetrics().widthPixels;  
			
			convertView = viewInflater.inflate(R.layout.view_grid_item_media_chooser, parent, false);

			holder = new ViewHolder();
			holder.checkBoxTextView   = (CheckedTextView) convertView.findViewById(R.id.checkTextViewFromMediaChooserGridItemRowView);
			holder.imageView          = (ImageView) convertView.findViewById(R.id.imageViewFromMediaChooserGridItemRowView);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		LayoutParams imageParams = (LayoutParams) holder.imageView.getLayoutParams();
		imageParams.width  = mWidth/4;
		imageParams.height = mWidth/4;

		holder.imageView.setLayoutParams(imageParams);
		if (type.equals("1"))
			holder.checkBoxTextView.setCheckMarkDrawable(R.drawable.selector_check_zhigu);

		// set the status according to this Category item

		if(mIsFromVideo){
			new VideoLoadAsync(videoFragment, holder.imageView, false, mWidth/4).executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, mGalleryModelList.get(position).url.toString());

		}else{
			ImageLoadAsync loadAsync = new ImageLoadAsync(mContext, holder.imageView, mWidth/2);
			loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, mGalleryModelList.get(position).url);
		}

		holder.checkBoxTextView.setChecked(mGalleryModelList.get(position).status);
		holder.checkBoxTextView.setTag(mGalleryModelList.get(position).url);
		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		CheckedTextView checkBoxTextView;
	}

}
