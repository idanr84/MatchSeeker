package com.example.idanr.tinderforjavaclass.NetworkManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.LruCache;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by idanr on 10/31/15.
 */
public class NetworkManager {

    private ImageLoader mImageLoader;
    private Context mCtx;
    private RequestQueue mRequestQueue;
    private static NetworkManager mSharedInstace;


    private static class LruBitmapCache extends LruCache<String, Bitmap>
            implements ImageLoader.ImageCache {

        public LruBitmapCache(int maxSize) {
            super(maxSize);
        }

        public LruBitmapCache(Context ctx) {
            this(getCacheSize(ctx));
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        }

        @Override
        public Bitmap getBitmap(String url) {
            return get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            put(url, bitmap);
        }

        // Returns a cache size equal to approximately three screens worth of images.
        public static int getCacheSize(Context ctx) {
            final DisplayMetrics displayMetrics = ctx.getResources().
                    getDisplayMetrics();
            final int screenWidth = displayMetrics.widthPixels;
            final int screenHeight = displayMetrics.heightPixels;
            // 4 bytes per pixel
            final int screenBytes = screenWidth * screenHeight * 4;

            return screenBytes * 3;
        }
    }


    public static NetworkManager  sharedInstance() {
     if (mSharedInstace == null) {
            mSharedInstace = new NetworkManager ();
        }
        return mSharedInstace;
    }

    public void initWithContext(Context context){
        mCtx = context;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(
                    LruBitmapCache.getCacheSize(mCtx)));
        }
        return mImageLoader;
    }
}
