package com.example.admin2015.student_rating;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.ExifInterface;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
/**
 * Created by admin2015 on 23.04.2015.
 */
public class ImageHelper {

    public static final String TAG = ImageHelper.class.getName();
    public static final String IM_DIR = "facerec";
    public static final String IM_PREFIX = "RECOGNIZE_";

    public static Bitmap cropFace(Bitmap originalBitmap, FaceDetector.Face face) {

        PointF faceMidPoint = new PointF();
        face.getMidPoint(faceMidPoint);
        float faceEyesDistance = face.eyesDistance();


        int x0 = (int) (faceMidPoint.x - faceEyesDistance * 2);
        int y0 = (int) (faceMidPoint.y - faceEyesDistance * 2);
        int x1 = (int) (faceMidPoint.x + faceEyesDistance * 2);
        int y1 = (int) (faceMidPoint.y + faceEyesDistance * 2);


        x0 = Math.max(x0, 0);
        y0 = Math.max(y0, 0);
        x1 = Math.min(x1, originalBitmap.getWidth());
        y1 = Math.min(y1, originalBitmap.getHeight());

        return Bitmap.createBitmap(originalBitmap, x0, y0, x1, y1);
    }

    public static String getUniqueFileName(String fileExtension) {
        String uuid = UUID.randomUUID().toString();
        return (uuid + ".jpg");

    }

    public static Bitmap cropImage(Bitmap originalBitmap, int x0, int y0, int x1, int y1) {
        return Bitmap.createBitmap(originalBitmap, x0, y0, x1, y1);
    }
    public static Bitmap rotateBitmap(Bitmap originalBitmap, int cx, int cy, int angle) {
        Matrix rotMatrix = new Matrix();
        rotMatrix.postRotate(angle, cx, cy);
        return Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), rotMatrix, true);
    }

    public static Uri getOutputImageFileUri() {
        return Uri.fromFile(getOutputImageFile());
    }

    public static File getOutputImageFile() {
        File im_dir = getImageDirectory();
        // Create a new filename baed on the current timestamp:
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // Create the file:
        File mediaFile = new File(im_dir.getPath() + File.separator + IM_PREFIX + timeStamp + ".jpg");

        return mediaFile;
    }

    public static Bitmap readBitmapFromFile(String fileName) {
        BitmapFactory.Options bitmapFatoryOptions = new BitmapFactory.Options();
        bitmapFatoryOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(fileName, bitmapFatoryOptions);
    }

    public static boolean saveBitmapAsJpegToExternalStorage(Bitmap bitmap, String fileName) {
        File im_dir = getImageDirectory();
        File im_file = new File(im_dir, fileName);
        return saveBitmapAsJpeg(bitmap, im_file);
    }
    private static File getImageDirectory() {
        File root = Environment.getExternalStorageDirectory();
        File im_dir = new File(root, IM_DIR);
        if (!im_dir.exists()) {
            im_dir.mkdir();
        }
        return im_dir;
    }
    public static boolean saveBitmapAsJpeg(Bitmap bitmap, File file) {
        FileOutputStream out = null;
        try {
            // Overwrite the existing bitmap?
            out = new FileOutputStream(file, false);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
        } catch (IOException e) {
            Log.e(TAG, "Could not save the image!", e);
            return false;
        } finally {
            // Be sure to close the IO handle:
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // We can safely ignore this case!
                }
            }
        }
        return true;
    }
    public static Bitmap loadResizedBitmap(String fileName, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);
        // Calculate the maximum inSampleSize larger than the request width/height:
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight) + 1;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap scaledBitmap = BitmapFactory.decodeFile(fileName, options);
        if (scaledBitmap.getHeight() > reqHeight || scaledBitmap.getWidth() > reqWidth) {
            return createScaledBitmap(scaledBitmap, reqWidth, reqHeight);
        }
        return scaledBitmap;
    }

    private static Bitmap createScaledBitmap(Bitmap bitmap, int reqWidth, int reqHeight) {
        // Calculate the maximum size:
        int maxSize = Math.max(reqWidth, reqHeight);
        // Now scale the width/height accordingly:
        int outWidth;
        int outHeight;
        int inWidth = bitmap.getWidth();
        int inHeight = bitmap.getHeight();
        if (inWidth > inHeight) {
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }

        return Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, false);
    }
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static String getBase64(Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream full_stream = new ByteArrayOutputStream();
        bitmap.compress(format, quality, full_stream);
        byte[] full_bytes = full_stream.toByteArray();
        return Base64.encodeToString(full_bytes, Base64.DEFAULT);
    }
    public static String getBase64Jpeg(Bitmap bitmap, int quality) {
        return getBase64(bitmap, Bitmap.CompressFormat.JPEG, quality);
    }
    public static int getImageRotation(String fileName) throws IOException {
        ExifInterface exif = new ExifInterface(fileName);
        int rotation;
        switch (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
            case ExifInterface.ORIENTATION_NORMAL:
                rotation = 0;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotation = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotation = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotation = 270;
                break;
            default:
                rotation = 0;
                break;
        }

        return rotation;
    }
}