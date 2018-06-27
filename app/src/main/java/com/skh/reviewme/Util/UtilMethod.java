package com.skh.reviewme.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.Nullable;

/**
 * Created by Seogki on 2018. 6. 18..
 */

public class UtilMethod {

    @Nullable
    public static Activity getActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    @SuppressLint("DefaultLocale")
    public static String getMemoryUsage(int i) {
        String heapSize = String.format("%.3f", (float) (Runtime.getRuntime().totalMemory() / 1024.00 / 1024.00));
        String freeMemory = String.format("%.3f", (float) (Runtime.getRuntime().freeMemory() / 1024.00 / 1024.00));

        String allocatedMemory = String
                .format("%.3f", (float) ((Runtime.getRuntime()
                        .totalMemory() - Runtime.getRuntime()
                        .freeMemory()) / 1024.00 / 1024.00));
        String heapSizeLimit = String.format("%.3f", (float) (Runtime.getRuntime().maxMemory() / 1024.00 / 1024.00));

        String nObjects = "Objects Allocated: " + i;

        return "Current Heap Size: " + heapSize
                + "\n Free memory: "
                + freeMemory
                + "\n Allocated Memory: "
                + allocatedMemory
                + "\n Heap Size Limit:  "
                + heapSizeLimit
                + "\n" + nObjects;
    }
}
