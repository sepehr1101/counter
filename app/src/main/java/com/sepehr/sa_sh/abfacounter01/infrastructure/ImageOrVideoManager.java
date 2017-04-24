package com.sepehr.sa_sh.abfacounter01.infrastructure;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.sepehr.sa_sh.abfacounter01.constants.ImageScale;
import com.sepehr.sa_sh.abfacounter01.constants.MediaType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sepehr on 3/2/2017.
 */
public class ImageOrVideoManager  implements IImageOrVideoManager{
    private String IMAGE_DIRECTORY_NAME;
    private Context appContext;
    private Uri fileUri; // file url to store image/video

    public ImageOrVideoManager(Context appContext,String imageDirectoryName) {
        this.appContext=appContext;
        IMAGE_DIRECTORY_NAME=imageDirectoryName;
    }

    public boolean isDeviceSupportCamera() {
        if (appContext.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public Intent getCaptureImageIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MediaType.IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        return intent;
    }

    public Intent getRecordVidoeIntent(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        fileUri = getOutputMediaFileUri(MediaType.VIDEO);
        // set video quality
        // 1- for high quality video
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        return intent;
    }

    public String getFilePath(){
        return fileUri.getPath();
    }

    public File downsizeImage(File file,int newScaleInPercent) {
        try {
            // The new size we want to scale to
            final int REQUIRED_SIZE = newScaleInPercent;
            final int QUALITY=100;
            final int BITMAP_OPTION_SIZE=6;

            // BitmapFactory options to downsize the image
            BitmapFactory.Options bitmapOption = new BitmapFactory.Options();
            bitmapOption.inJustDecodeBounds = true;
            bitmapOption.inSampleSize = BITMAP_OPTION_SIZE;
            // factor of downsizing the image
            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, bitmapOption);
            inputStream.close();
            int scale= calculateProperScale(REQUIRED_SIZE,bitmapOption.outWidth,
                    bitmapOption.outHeight);
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);
            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();
            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, outputStream);
            return file;
        } catch (Exception e) {
            return null;
        }
    }
    //
    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(MediaType mediaType) {
        return Uri.fromFile(getOutputMediaFile(mediaType));
    }
    //
    /**
     * returning image / video
     */
    public File getOutputMediaFile(MediaType mediaType) {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        switch (mediaType){
            case IMAGE:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "IMG_" + timeStamp + ".jpg");
                break;
            case VIDEO:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "VID_" + timeStamp + ".mp4");
                break;
            default:
                mediaFile=null;
        }
        return mediaFile;
    }
    //
    public Bitmap getBitmap(String filePath,ImageScale imageScale){
        // bimatp factory
        BitmapFactory.Options options = new BitmapFactory.Options();
        try{
            int scale=imageScale.getValue();
            options.inSampleSize = scale;
            final Bitmap bitmap = BitmapFactory.decodeFile(filePath,options);
        return bitmap;
        }
        catch (Exception e){
            e.printStackTrace();
            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            int scale=calculateProperScale(128,500,500);
            options.inSampleSize = scale;
            Log.e("image scale:",scale+"");
            final Bitmap bitmap = BitmapFactory.decodeFile(filePath,options);
            return bitmap;
        }
    }
    //
    // Find the correct scale value. It should be the power of 2.
    private int calculateProperScale(int requierdScale,int bitmapWidth,int bitmapHight){
        int scale = 1;
        while (bitmapWidth/ scale / 2 >= requierdScale &&
               bitmapHight / scale / 2 >= requierdScale) {
            scale *= 2;
        }
        return scale;
    }
}
