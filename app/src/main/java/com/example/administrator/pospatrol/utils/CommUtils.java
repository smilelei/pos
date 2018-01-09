package com.example.administrator.pospatrol.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;

public class CommUtils {
    public static File basePath;//应用程序sdcard目录
    public static Typeface typeface;

    static {
        File sdcard = Environment.getExternalStorageDirectory();
        basePath = new File(sdcard, "/pos_patrol");
        if (!basePath.exists()) {
            basePath.mkdirs();
        }

    }

    /**
     * 自定义字体
     * @param context
     * @return
     */
    public static Typeface getTypeface(Context context) {
        File typefaceDir = new File(basePath, "fonts/");

        if (!typefaceDir.exists()) {//目录不存在就创建
            typefaceDir.mkdirs();
        }

        if (typeface == null) {
            File typefaceFile = new File(typefaceDir, "stxingka.ttf");
            if (!typefaceFile.exists()) {//字体文件不存在
                try {
                    InputStream in = context.getAssets().open(
                            "fonts/STXINGKA.TTF");
                    OutputStream out = new FileOutputStream(typefaceFile);

                    byte[] buffer = new byte[1024 * 100];
                    int len = -1;
                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }

                    out.flush();
                    in.close();
                    out.close();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //获取字体
            typeface = Typeface.createFromFile(typefaceFile);
        }

        return typeface;
    }
    public static String locationToString(double attribute,int len)
    {
        String att = String.valueOf(Math.abs((attribute*100000000)));
        return att.substring(0,len);
    }
}
