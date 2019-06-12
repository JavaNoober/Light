package com.light.core.Utils.http;

import android.net.Uri;
import android.text.TextUtils;

import com.light.body.LightCache;
import com.light.core.Utils.UriParser;
import com.light.core.listener.OnCompressFinishListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by xiaoqi on 2017/12/20
 */
public class HttpDownLoader {

    private static final int MAX_REDIRECTS = 5;

    private static final int HTTP_TEMPORARY_REDIRECT = 307;

    private static final int HTTP_PERMANENT_REDIRECT = 308;

    private static final int TIMEOUT = 30000;

    public static void downloadImage(boolean openDiskCache, String url, OnCompressFinishListener callback) {
        if (TextUtils.isEmpty(url))
            return;
        Uri uri = Uri.parse(url);
        downloadImage(openDiskCache, uri, callback);
    }

    public static void downloadImage(boolean openDiskCache, Uri uri, OnCompressFinishListener callback) {
        if (!UriParser.isNetworkUri(uri)){
            callback.onError(new Exception("uri is not net uri"));
            return;
        }

        HttpURLConnection connection = obtainHttpURLConnection(uri, MAX_REDIRECTS);
        InputStream is = null;
        if (connection == null){
            callback.onError(new Exception("get connection error"));
            return;
        }
        try {
            is = connection.getInputStream();
            if (callback != null){
                byte[] bytes = transformToByteArray(is);
                if(openDiskCache){
                    LightCache.getInstance().save(UriParser.getAbsPath(uri), bytes);
                }
                callback.onFinish(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
            callback.onError(e);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static byte[] downloadImage(String url) {
        if (TextUtils.isEmpty(url)){
            return null;
        }
        Uri uri = Uri.parse(url);
        return downloadImage(uri);
    }

    public static byte[] downloadImage(Uri uri) {
        if (!UriParser.isNetworkUri(uri)){
            return null;
        }

        HttpURLConnection connection = obtainHttpURLConnection(uri, MAX_REDIRECTS);
        InputStream is = null;

        if (connection == null){
            return null;
        }
        try {
            is = connection.getInputStream();
            return transformToByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    private static HttpURLConnection obtainHttpURLConnection(Uri uri, int maxRedirects) {
        if (uri == null){
            return null;
        }
        HttpURLConnection connection = null;
        try {
            URL url = new URL(uri.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode >= HttpURLConnection.HTTP_OK && responseCode < HttpURLConnection.HTTP_MULT_CHOICE){
                return connection;
            }

            if (isHttpRedirect(responseCode)) {
                String nextUriString = connection.getHeaderField("Location");
                connection.disconnect();
                Uri nextUri = nextUriString == null ? null : Uri.parse(nextUriString);
                String originalScheme = uri.getScheme();
                if (maxRedirects > 0 && nextUri != null && !nextUri.getScheme().equals(originalScheme)) {
                    return obtainHttpURLConnection(nextUri, maxRedirects - 1);
                } else {
                    String message = maxRedirects == 0
                            ? "URL %s follows too many redirects, uri:" + uri.toString()
                            : "URL %s returned %d without a valid redirect, uri:" + uri.toString() + ", responseCode:" + responseCode;
                    throw new RuntimeException(message);
                }
            } else {
                connection.disconnect();
                connection = null;
            }

        } catch (MalformedURLException e) {
            connection = null;
            e.printStackTrace();
        } catch (IOException e) {
            connection = null;
            e.printStackTrace();
        } catch (Exception e) {
            connection = null;
            e.printStackTrace();
        }
        return connection;
    }

    private static boolean isHttpRedirect(int responseCode) {
        switch (responseCode) {
            case HttpURLConnection.HTTP_MULT_CHOICE:
            case HttpURLConnection.HTTP_MOVED_PERM:
            case HttpURLConnection.HTTP_MOVED_TEMP:
            case HttpURLConnection.HTTP_SEE_OTHER:
            case HTTP_TEMPORARY_REDIRECT:
            case HTTP_PERMANENT_REDIRECT:
                return true;
            default:
                return false;
        }
    }


    public static byte[] transformToByteArray(InputStream is) {
        if (is == null)
            return new byte[0];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int len = -1;
        try {
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bos.toByteArray();
    }

}
