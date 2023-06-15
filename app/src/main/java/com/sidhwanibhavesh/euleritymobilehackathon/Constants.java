package com.sidhwanibhavesh.euleritymobilehackathon;

import android.graphics.Bitmap;

import java.util.ArrayList;

final public class Constants {

    // URL to get JSON containing url of image
    public static final String IMAGE_JSON_URL = "https://eulerity-hackathon.appspot.com/image";
    public static final String UPLOAD_IMAGE_URL = "https://eulerity-hackathon.appspot.com/upload";

    // Keys to parse JSON data
    public static final String JSON_URL_KEY = "url";
    public static final String APP_ID_KEY = "appid";
    public static final String ORIGINAL_KEY = "original";
    public static final String FILE_KEY = "file";

    public static final String APP_ID = "sidhwanibhavesh@gmail.com";

    public static final String INTENT_EXTRA_POS_KEY = "position";

    public static ArrayList<Image> imageArray;


    public static Bitmap testBitmap;
}
