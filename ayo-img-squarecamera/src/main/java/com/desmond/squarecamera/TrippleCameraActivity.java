package com.desmond.squarecamera;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;


public class TrippleCameraActivity extends AppCompatActivity {

    public static final String TAG = TrippleCameraActivity.class.getSimpleName();

    TrippleCameraFragment cameraFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.squarecamera__CameraFullScreenTheme);
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.squarecamera__activity_camera);

        if (savedInstanceState == null) {
            cameraFragment = (TrippleCameraFragment) TrippleCameraFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, cameraFragment, TrippleCameraFragment.TAG)
                    .commit();
        }

        TrippleManager.reset();
    }

    public void returnPhotoUri(Uri uri) {
        Intent data = new Intent();
        //data.setData(uri);
        data.putParcelableArrayListExtra("photos", (ArrayList<Uri>) TrippleManager.photos);

        if (getParent() == null) {
            setResult(RESULT_OK, data);
        } else {
            getParent().setResult(RESULT_OK, data);
        }

        finish();
    }

    public void onCancel(View view) {
        //cameraFragment.refresh();
        getSupportFragmentManager().popBackStack();
    }
}
