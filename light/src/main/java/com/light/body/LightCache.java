package com.light.body;

import com.light.core.Utils.ContextUtil;
import com.light.core.cache.CacheKey;
import com.light.core.cache.DiskLruCache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LightCache {

    private static LightCache lightCache;

    private static volatile DiskLruCache diskCache;

    public static LightCache getInstance(){
        if(diskCache == null){
            synchronized (LightCache.class){
                if(diskCache == null){
                    lightCache = new LightCache();
                }
            }
        }
        return lightCache;
    }

    private LightCache(){
        try {
            diskCache = DiskLruCache.open(ContextUtil.get().getCacheDir(), 1, 1, 128 * 1024 * 50);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void save(String key, byte[] bytes){
        try {
            DiskLruCache.Editor editor = diskCache.edit(CacheKey.hashKeyForDisk(key));
            OutputStream outputStream = editor.newOutputStream(0);
            outputStream.write(bytes);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] get(String key){
        try {
            DiskLruCache.Snapshot snapshot = diskCache.get(CacheKey.hashKeyForDisk(key));
            if (snapshot != null) {
                InputStream is = snapshot.getInputStream(0);
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                byte[] buff = new byte[128];
                int rc = 0;
                while ((rc = is.read(buff, 0, 128)) > 0) {
                    swapStream.write(buff, 0, rc);
                }
                return swapStream.toByteArray();
            }else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
