package com.uiu.helper.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

/**
 *
 *
 */
public class BitmapCache {

    public interface BitmapWorkerListener {
        void onJobDone(ImageView view, Bitmap bitmap);
    }

    private static BitmapCache ourInstance = new BitmapCache();

    public static BitmapCache getInstance() {
        return ourInstance;
    }

    private LruCache<String, Bitmap> cachedBitmap;

    private BitmapCache() {
        cachedBitmap = new LruCache<>(100);
    }

    public Bitmap getBitmap(String id) {
        return cachedBitmap.get(id);
    }

    public void setBitmap(String id, Bitmap bitmap) {
        if (bitmap != null)
            cachedBitmap.put(id, bitmap);
    }


    class BitmapHolder {
        byte[] array;
        String id;
        BitmapWorkerListener listener;
        ImageView view;
    }

    public void loadByteArrayToBitmapAndSaveInCache(BitmapWorkerListener listener, ImageView view, String id, byte[] array) {
        BitmapHolder holder = new BitmapHolder();

        holder.listener = listener;
        holder.array = array;
        holder.id = id;
        holder.view = view;
        new BitmapFromByteArrayTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, holder);
    }


    private class BitmapFromByteArrayTask extends AsyncTask<BitmapHolder, Void, Bitmap> {

        BitmapHolder holder;

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            setBitmap(holder.id, bitmap);
            if (holder.listener != null) {
                holder.listener.onJobDone(holder.view, bitmap);
            }
        }

        @Override
        protected Bitmap doInBackground(BitmapHolder... params) {
            this.holder = params[0];
            return Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(params[0].array, 0, params[0].array.length, new BitmapFactory.Options()), 100, 100, true);
        }
    }

}
