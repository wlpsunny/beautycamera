package com.eagleapps.beautycam.otherView;

import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings.Secure;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    public static String ALBUM = "Face Beauty";
    public static String FONT_MAIN = "fonts/rubik_regular.ttf";

    public static String md5_Hash(String str) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            messageDigest = null;
        }
        messageDigest.update(str.getBytes(), 0, str.length());
        return new BigInteger(1, messageDigest.digest()).toString(16);
    }

    public static String getDeviceID(Context context) {
        return md5_Hash(Secure.getString(context.getContentResolver(), "android_id")).toUpperCase();
    }

    public static int getRelativeLeft(View view) {
        if (view.getParent() == view.getRootView()) {
            return view.getLeft();
        }
        return view.getLeft() + getRelativeLeft((View) view.getParent());
    }

    public static int getRelativeTop(View view) {
        if (view.getParent() == view.getRootView()) {
            return view.getTop();
        }
        return view.getTop() + getRelativeTop((View) view.getParent());
    }

    public static int dpToPx(Context context, int i) {
        float f = (float) i;
        context.getResources();
        return (int) (Resources.getSystem().getDisplayMetrics().density * f);
    }

    public static int dpToPx(Context context, float f) {
        context.getResources();
        return (int) (Resources.getSystem().getDisplayMetrics().density * f);
    }

    public static void copyFile(File file, File file2) throws IOException {
        if (file.exists()) {
            FileChannel channel = new FileInputStream(file).getChannel();
            FileChannel channel2 = new FileOutputStream(file2).getChannel();
            if (!(channel2 == null || channel == null)) {
                channel2.transferFrom(channel, 0, channel.size());
            }
            if (channel != null) {
                channel.close();
            }
            if (channel2 != null) {
                channel2.close();
            }
        }
    }
}
