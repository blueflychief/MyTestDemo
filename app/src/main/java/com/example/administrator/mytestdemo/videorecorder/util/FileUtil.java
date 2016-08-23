package com.example.administrator.mytestdemo.videorecorder.util;

import android.os.Environment;

import com.example.administrator.mytestdemo.videorecorder.recorder.Constants;

import java.io.File;


/**
 * Created by Administrator on 2015/9/11.
 */
public class FileUtil {

    public static final String APP_SD_ROOT_DIR = "/recordvideodemo";
    public static final String MEDIA_FILE_DIR = APP_SD_ROOT_DIR + "/video";

    /**
     * 判断SD卡是否挂载
     *
     * @return
     */
    public static boolean isSDCardMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists())
            return file.delete();
        return true;
    }

    public static String createFilePath(String folder, String subfolder, String uniqueId) {
        File dir = new File(Environment.getExternalStorageDirectory(), folder);
        if (subfolder != null) {
            dir = new File(dir, subfolder);
        }
        dir.mkdirs();
        String fileName = Constants.FILE_START_NAME + uniqueId + Constants.VIDEO_EXTENSION;
        return new File(dir, fileName).getAbsolutePath();
    }

}
