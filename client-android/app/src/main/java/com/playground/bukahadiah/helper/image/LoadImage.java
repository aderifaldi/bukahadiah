package com.playground.bukahadiah.helper.image;

import java.io.File;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class LoadImage extends AsyncTask<Void, Void, Bitmap> {
    private File fileImage;
    protected Context context;
    private ImageFinishLoad onFinish;
    private int size;
    private boolean useWidth;

    public interface ImageFinishLoad {
        public void onImageFinishLoad(Bitmap bitmap);
    }

    public LoadImage(String fileName, Context context, ImageFinishLoad onFinish) {
        super();
        this.context = context;
        this.onFinish = onFinish;
        this.fileImage = new File(context.getFilesDir(), fileName);
        size = 0;
    }

    public LoadImage(String fileName, Context context, ImageFinishLoad onFinish, int size, boolean useWidth) {
        super();
        this.context = context;
        this.onFinish = onFinish;
        this.fileImage = new File(context.getFilesDir(), fileName);
        this.size = size;
        this.useWidth = useWidth;
    }

    public LoadImage(File file, Context context, ImageFinishLoad onFinish, int size, boolean useWidth) {
        super();
        this.context = context;
        this.onFinish = onFinish;
        this.fileImage = file;
        this.size = size;
        this.useWidth = useWidth;
    }

    private Bitmap loadImage() {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        if (size > 0) {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileImage.getAbsolutePath(), options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            int inSampleSize = 1;
            if (useWidth) {
                if (imageWidth > size) {
                    // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                    // height and width larger than the requested height and width.
                    while ((imageWidth / inSampleSize) > size) {
                        inSampleSize *= 2;
                    }
                }
            } else {
                if (imageHeight > size) {
                    // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                    // height and width larger than the requested height and width.
                    while ((imageHeight / inSampleSize) > size) {
                        inSampleSize *= 2;
                    }
                }
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;
        }
        if (options.inSampleSize == 1) {
            return BitmapFactory.decodeFile(fileImage.getAbsolutePath());
        } else {
            return BitmapFactory.decodeFile(fileImage.getAbsolutePath(), options);
        }
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        return loadImage();
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        onFinish.onImageFinishLoad(result);
    }
}
