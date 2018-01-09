package com.example.administrator.pospatrol.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigUtils {
    public static String url;
    public static String usename=null;

    public static String getHost(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences
                ("config",context.MODE_PRIVATE);
        url = sp.getString("url","");
        return url;
    }
}
