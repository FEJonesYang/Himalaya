package com.example.himalaya.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

/**
 * @author JonesYang
 * @Data 2020-10-28
 * @Function 高斯模糊，实现毛玻璃效果
 */
public class ImageBlur {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void makeBlur(ImageView imageView, Context context) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        Bitmap blurred = blurRenderScript(bitmap, 10, context);
        imageView.setImageBitmap(blurred);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static Bitmap blurRenderScript(Bitmap smallBitmap, int radius, Context context) {

        smallBitmap = RGB565toARGB888(smallBitmap);
        Bitmap bitmap = Bitmap.createBitmap(smallBitmap.getWidth(), smallBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        RenderScript renderScript = RenderScript.create(context);
        Allocation blurInput = Allocation.createCubemapFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createCubemapFromBitmap(renderScript, bitmap);
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        scriptIntrinsicBlur.setInput(blurInput);
        scriptIntrinsicBlur.setRadius(radius);
        scriptIntrinsicBlur.forEach(blurOutput);
        blurOutput.copyTo(bitmap);
        renderScript.destroy();
        return null;
    }

    private static Bitmap RGB565toARGB888(Bitmap img) {

        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels. Each int is the color values for one pixels
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels
        result.setPixels(pixels, 0, img.getWidth(), 0, 0, result.getWidth(), result.getHeight());

        return result;
    }
}
