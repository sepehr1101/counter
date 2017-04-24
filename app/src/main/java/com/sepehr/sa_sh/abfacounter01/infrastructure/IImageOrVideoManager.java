package com.sepehr.sa_sh.abfacounter01.infrastructure;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.sepehr.sa_sh.abfacounter01.constants.ImageScale;
import com.sepehr.sa_sh.abfacounter01.constants.MediaType;

import java.io.File;

/**
 * Created by saeid on 3/2/2017.
 */
public interface IImageOrVideoManager {
    boolean isDeviceSupportCamera();
    Intent getCaptureImageIntent();
    Intent getRecordVidoeIntent();
    String getFilePath();
    File downsizeImage(File file,int newScaleInPercent);
    Uri getOutputMediaFileUri(MediaType mediaType);
    File getOutputMediaFile(MediaType mediaType);
    Bitmap getBitmap(String filePath,ImageScale imageScale);
}
