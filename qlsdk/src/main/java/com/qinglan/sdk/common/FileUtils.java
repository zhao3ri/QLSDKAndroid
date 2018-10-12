package com.qinglan.sdk.common;

import android.os.Environment;

import java.io.File;

public class FileUtils {
    private static final String FILE_NAME = "qinglan";

    private static String createFileDir(String dirName) {
        String fileDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            fileDir = Environment.getExternalStorageDirectory() + File.separator + dirName + File.separator;
        } else {
        }
        File file = new File(fileDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return fileDir;
    }
}
