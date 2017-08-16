package com.wang.wphotopicker;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created on 2017/8/11.
 * Author: wang
 */

public class ImageUtil {

    @Nullable
    public static int[] grey(@NonNull int[] pixels){
        int length = pixels.length;
        if (length == 0) {
            Log.e("Error", "the source pixel is length == 0");
            return null;
        }
        int[] greyPixels = new int[length];
        for (int i = 0; i < length; i++) {
            int pixel = pixels[i];
            int grey = rgbToGrey(pixel);
            int a = pixel >>> 24;
            greyPixels[i] = (a << 24) | (grey << 16) | (grey << 8) | grey;
        }
        return greyPixels;
    }

    /**
     * 大津法 进行二值化
     * @param pixels 输入像素
     * @param greyPixels 输出的灰度像素
     * @param binaryPixels 输出的二值像素
     * @return 阈值
     */
    public static int OTSU(@NonNull int[] pixels, @Nullable int[] greyPixels, @NonNull int[] binaryPixels) {
        if (pixels.length == 0) {
            Log.e("Error", "the source pixel is length == 0");
            return -1;
        }
        /**
         * 是否获取灰度图
         */
        boolean getGreyPixel = false;
        /**
         * 总的灰度值
         */
        int sumGrey = 0;
        /**
         * 总的前景灰度值
         */
        int sumFrontGrey = 0;
        /**
         * 阈值
         */
        int thresholdValue = 0;
        /**
         * 总的像素点个数
         */
        int n = pixels.length;
        /**
         * 前景(像素小于阈值)像素点个数
         */
        int n0 = 0;
        /**
         * 背景(像素大于阈值)像素点个数
         */
        int n1;
        /**
         * 前景平均灰度值
         */
        double u0;
        /**
         * 背景平均灰度值
         */
        double u1;
        /**
         *最大类间方差
         */
        double maxG = 0;
        /**
         * 类间方差
         */
        double g;

        int[] greys = new int[n];
        int[] hist = new int[256];

        if (greyPixels != null) {
            if (greyPixels.length == n) {
                getGreyPixel = true;
            } else {
                Log.e("Error", "the grey pixel length must = width * height");
            }
        }

        if (binaryPixels.length != n) {
            Log.e("Error", "the binary pixel length must = width * height");
            return -1;
        }


        for (int i = 0; i < n; i++) {
            int pixel = pixels[i];
            int grey = rgbToGrey(pixel);
            greys[i] = grey;
            sumGrey += grey;
            hist[grey]++;
            if (getGreyPixel) {
                int a = pixel >>> 24;
                greyPixels[i] = (a << 24) | (grey << 16) | (grey << 8) | grey;
            }
        }

        for (int i = 0; i < 256; i++) {
            n0 += hist[i];
            if (n0 == 0) {
                continue;
            }
            n1 = n - n0;
            if (n1 == 0) {
                break;
            }
            sumFrontGrey += i * hist[i];
            u0 = (double)sumFrontGrey / n0;
            u1 = (double)(sumGrey - sumFrontGrey) / n1;
            g = (double) n0 * n1 * (u0 - u1) * (u0 - u1);
//            类间方差最大的分割意味着错分概率最小
            if (g > maxG) {
                maxG = g;
                thresholdValue = i;
            }
        }

        for (int i = 0; i < n; i++) {
            if (greys[i] >= thresholdValue) {
                binaryPixels[i] = Color.WHITE;
            } else {
                binaryPixels[i] = Color.BLACK;
            }
        }
        return thresholdValue;
    }

    /**
     * 加权平均法 进行灰度化
     * @param pixel 像素点
     * @return 灰度值
     */
    private static int rgbToGrey(int pixel) {
        int r = (pixel >> 16) & 0xFF;
        int g = (pixel >> 8) & 0xFF;
        int b = pixel & 0xFF;
        return (int) (0.30 * r + 0.59 * g + 0.11 * b);
    }
}
