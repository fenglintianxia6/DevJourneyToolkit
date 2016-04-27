package dev.journey.uitoolkit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.SparseIntArray;

import java.util.List;

/**
 * 图片加水印
 * Created by mwp on 16/4/22.
 */
public class WaterMark {

    public static Bitmap putWaterMark(Bitmap photo, String str, TextWaterMarkConfig textWaterMarkConfig) {
        if (photo == null || textWaterMarkConfig == null) {
            return null;
        }
        int width = photo.getWidth();
        int height = photo.getHeight();
        Bitmap icon = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); //建立一个空的BItMap
        Canvas canvas = new Canvas(icon);//初始化画布绘制的图像到icon上

        Paint photoPaint = new Paint(); //建立画笔
        photoPaint.setDither(true); //获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);//过滤一些

        Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());//创建一个指定的新矩形的坐标
        Rect dst = new Rect(0, 0, width, height);//创建一个指定的新矩形的坐标
        canvas.drawBitmap(photo, src, dst, photoPaint);//将photo 缩放或则扩大到 dst使用的填充区photoPaint

        int textSize = textWaterMarkConfig.getTextSize();
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);//设置画笔
        textPaint.setTextSize(textSize);//字体大小
        textPaint.setColor(textWaterMarkConfig.getTextColor());//采用的颜色

        Path[] paths = getPaths(textSize / 4, width, height);
        for (Path path : paths) {
            canvas.drawTextOnPath(str, path, 0, 0, textPaint);//绘制上去字，开始未知x,y采用那只笔绘制
        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return icon;
    }

    private static Path[] getPaths(int startX, int width, int height) {
        Path[] paths = new Path[4];
        paths[0] = new Path();
        paths[0].moveTo(startX, height / 10);
        paths[0].lineTo(height / 10, 0);

        paths[1] = new Path();
        paths[1].moveTo(startX, height / 2);
        paths[1].lineTo(height / 2, 0);


        paths[2] = new Path();
        paths[2].moveTo(startX, height);
        paths[2].lineTo(height, 0);


        paths[3] = new Path();
        paths[3].moveTo(width / 2, height);
        paths[3].lineTo(width, height - width / 2);
        return paths;

    }

    public static class TextWaterMarkConfig {
        private String text;
        private int textSize;
        private int textColor;

        public String getText() {
            return text;
        }

        public int getTextSize() {
            return textSize;
        }

        public int getTextColor() {
            return textColor;
        }

        public TextWaterMarkConfig text(String text) {
            this.text = text;
            return this;
        }

        public TextWaterMarkConfig textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public TextWaterMarkConfig textColor(int textColor) {
            this.textColor = textColor;
            return this;
        }
    }
}
