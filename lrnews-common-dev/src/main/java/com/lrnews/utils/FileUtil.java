package com.lrnews.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class FileUtil {
    public static String fileToBase64(File file) {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] fileData = null;
        // 读取文件字节数组
        try {
            in = new FileInputStream(file);
            fileData = new byte[in.available()];
            in.read(fileData);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 对字节数组Base64编码并且返回
        return Base64.getEncoder().encodeToString(fileData);
    }
}
