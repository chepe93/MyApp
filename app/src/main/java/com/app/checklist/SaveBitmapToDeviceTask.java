package com.app.checklist;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.io.OutputStream;

/**
 * Created by GRojas on 1/8/2017.
 */

public class SaveBitmapToDeviceTask extends AsyncTask<Bitmap, Void, Uri> {

    private final Context mContext;
    private final String mTitle;
    private final String mDescription;

    public SaveBitmapToDeviceTask(Context pContext, String title, String description){
        this.mContext = pContext;
        this.mTitle=title;
        this.mDescription=description;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Uri doInBackground(Bitmap... cards) {
        return insertImageIntoGallery(mContext.getContentResolver(), cards[0], mTitle,
                mDescription);
    }

    /**
     * A copy of the Android internals insertImage method, this method populates the
     * meta data with DATE_ADDED and DATE_TAKEN. This fixes a common problem where media
     * that is inserted manually gets saved at the end of the gallery (because date is not populated).
     * @see MediaStore.Images.Media#insertImage(ContentResolver, Bitmap, String, String).
     * If the MediaStore not available, we will redirect the file to our alternative source, the SD card.
     */
    public Uri insertImageIntoGallery(ContentResolver cr, Bitmap source, String title, String description) {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
                } finally {
                    imageOut.close();
                }

                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        if(title.equals("Croquis")){
            return null;
        }else{
            return url;
        }

    }

    @Override
    public void onPostExecute(Uri url){
        if(url!=null){
            FirmaModal.accept(url);
        }
    }
}