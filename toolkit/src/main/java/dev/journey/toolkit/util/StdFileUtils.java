package dev.journey.toolkit.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StdFileUtils {


//下面几个方法是规范创建SD文件夹
//start-------------------------------------------------------------------------------------------------------

    /**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * @param context    The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() :
                        context.getCacheDir().getPath();
        File dir = new File(cachePath + File.separator + uniqueName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        L.d("StdFileUtils getDiskCacheDir", "dir=" + dir.getAbsolutePath() + ";exists=" + dir.exists());
        return dir;
    }

    /**
     * Check if external storage is built-in or removable.
     *
     * @return True if external storage is removable (like an SD card), false
     * otherwise.
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    private static File getExternalCacheDir(Context context) {
        File f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            f = context.getExternalCacheDir();
            if (f != null) {
                return f;
            }
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        f = new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
        return f;
    }
//end---------------------------------------------------------------------------------------------------------------------

    public static File getSdCardFile(Context context, String dirName, String fileName) {
        File dir = getSdCardDir(context, dirName);
        File file = new File(dir, fileName);
        L.d("StdFileUtils", "getSdCardFile: file=" + file.getAbsolutePath());
        return file;
    }

    public static File getSdCardDir(Context context, String uniqueName) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                !isExternalStorageRemovable()) {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + uniqueName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            L.d("StdFileUtils", "getSdCardDir: dir=" + dir.getAbsolutePath());
            return dir;
        } else {
            File dir = getDiskCacheDir(context, uniqueName);
            L.d("StdFileUtils", "getSdCardDir: dir=" + dir.getAbsolutePath());
            return dir;
        }
    }

    /**
     * 根据byte数组，生成文件
     */
    public static boolean bytesToFile(byte[] bfile, File file) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return false;
    }

    public static String getFileNameFromUrl(String url) {
        if (TextUtils.isEmpty(url) || !url.contains("/")) {
            return url;
        }
        return url.substring(url.lastIndexOf("/"), url.length());
    }

    /**
     * 存在且是文件(而非文件夹)
     *
     * @param file
     * @return
     */
    public static boolean isFileExists(File file) {
        return file != null && file.isFile() && file.exists();
    }

    /**
     * 存在且是文件(而非文件夹)
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExists(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            return isFileExists(file);
        } else {
            return false;
        }
    }

    /**
     * 文件或者文件夹存在
     *
     * @param file
     * @return
     */
    public static boolean isFileOrDirExists(File file) {
        return file != null && file.exists();
    }

    /**
     * 调起系统安装APK
     *
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file) {
        if (context != null && isFileExists(file)) {
            Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
            Intent installIntent = new Intent();
            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            installIntent.setAction(Intent.ACTION_VIEW);
            String type = "application/vnd.android.package-archive";
            installIntent.putExtra("loadapk", "loadapk");
            installIntent.setDataAndType(uri, type);
            context.startActivity(installIntent);
        }
    }

    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }

        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return file.delete();
        }

        for (File f : files) {
            if (isFileExists(f)) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    /**
     * 获取文件长度或者目录下所有文件长度总和
     *
     * @param dir
     * @return
     */
    public static long getFileDirectorySize(File dir) {
        long size = 0;
        if (!isFileOrDirExists(dir)) {
            return 0;
        } else if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (isFileExists(file)) {
                        size += file.length();
                    } else if (isFileOrDirExists(file)) {
                        size += getFileDirectorySize(file);
                    }
                }
            }
        } else if (isFileExists(dir)) {
            size += dir.length();
        }
        return size;
    }

    public static double toKB(long bytesCount) {
        return bytesCount * 1.0d / 1024;
    }

    public static double toMB(long bytesCount) {
        return toKB(bytesCount) / 1024;
    }

    public static double toGB(long bytesCount) {
        return toMB(bytesCount) / 1024;
    }
}
