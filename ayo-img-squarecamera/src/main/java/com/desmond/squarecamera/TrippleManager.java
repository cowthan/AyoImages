package com.desmond.squarecamera;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * 连拍3张，放这里
 */
public class TrippleManager {

    public static List<Uri> photos = new ArrayList<Uri>();


    public static void reset(){
        photos = new ArrayList<Uri>();
    }

}
