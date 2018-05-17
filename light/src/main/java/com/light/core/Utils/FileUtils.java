package com.light.core.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by xiaoqi on 2018/5/17
 */
public class FileUtils {

    public static boolean copyFile(File source, File dest) throws IOException {
        boolean result = false;
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            result = true;
        } finally {
            if(inputChannel != null){
                inputChannel.close();
            }
            if(outputChannel != null){
                outputChannel.close();
            }
        }

        return result;
    }
}
