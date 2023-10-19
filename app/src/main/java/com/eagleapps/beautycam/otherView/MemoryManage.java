package com.eagleapps.beautycam.otherView;

import android.app.ActivityManager;
import android.content.Context;

public class MemoryManage {
    public static float free(Context context) {
        int memoryClass = ((ActivityManager) context.getSystemService("activity")).getMemoryClass() - 24;
        if (memoryClass < 1) {
            memoryClass = 1;
        }
        return (float) memoryClass;
    }
}
