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

package com.learnncode.mediachooser.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.learnncode.mediachooser.ImageLang;
import com.learnncode.mediachooser.MediaChooser;
import com.learnncode.mediachooser.R;
import com.learnncode.mediachooser.activity.HomeFragmentActivity;
import com.learnncode.mediachooser.adapter.BucketGridAdapter;

import java.util.List;
/**
 * 选择目录，并返回选择的目录
 * @author Administrator
 *
 */
public class BucketImageFragmentForDirPicker extends Fragment{


	private View mView;
	private GridView mGridView;
	private BucketGridAdapter mBucketAdapter;

	public BucketImageFragmentForDirPicker(){
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mView == null){
			mView     = inflater.inflate(R.layout.view_grid_layout_media_chooser_list, container, false);
			mGridView = (GridView)mView.findViewById(R.id.gridViewFromMediaChooser);
			
			init();
		}else{
			((ViewGroup) mView.getParent()).removeView(mView);
			if(mBucketAdapter == null){
				Toast.makeText(getActivity(), getActivity().getString(R.string.no_media_file_available), Toast.LENGTH_SHORT).show();
			}
		}
		return mView;
	}


	private void init(){
		List<ImageLang.Dir> dirs = ImageLang.getImageDirs(getActivity(), new ImageLang.DirFilter() {
			@Override
			public boolean access(ImageLang.Dir dir) {
				return true;
			}
		});
		if(dirs != null && dirs.size() > 0){
			mBucketAdapter = new BucketGridAdapter(getActivity(), 0, dirs, false);
			mGridView.setAdapter(mBucketAdapter);
		}else{
			Toast.makeText(getActivity(), getActivity().getString(R.string.no_media_file_available), Toast.LENGTH_SHORT).show();
		}

		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
									int position, long id) {

				ImageLang.Dir bucketEntry  = (ImageLang.Dir)adapter.getItemAtPosition(position);
				Intent selectImageIntent = new Intent(getActivity(),HomeFragmentActivity.class);
				selectImageIntent.putExtra("name", bucketEntry.bucketName);
				System.out.println("选中目录--" + bucketEntry.bucketName);
				selectImageIntent.putExtra("image", true);
				selectImageIntent.putExtra("isFromBucket", true);
				getActivity().startActivityForResult(selectImageIntent, MediaChooser.REQ_PICK_IMAGE);
			}
		});
	}

	public BucketGridAdapter getAdapter() {
		return	mBucketAdapter;
	}


}
