package com.eagleapps.beautycam.utilsApp;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.lang.reflect.Field;

public class TypefaceUtils {
    public static void overrideFont(Context context, String str, String str2) {
        try {
            Typeface createFromAsset = Typeface.createFromAsset(context.getAssets(), str2);
//            Typeface typeface = ResourcesCompat.getFont(context, R.font.raleway_regular);

            Field declaredField = Typeface.class.getDeclaredField(str);
            declaredField.setAccessible(true);
            declaredField.set(null, createFromAsset);
//            declaredField.set(null, typeface);
        } catch (Exception unused) {
            StringBuilder sb = new StringBuilder();
            sb.append("Can not ");
            sb.append(str2);
            sb.append(" instead of ");
            sb.append(str);
            Log.e(sb.toString(), "=="+unused.getMessage());
        }
    }
}
