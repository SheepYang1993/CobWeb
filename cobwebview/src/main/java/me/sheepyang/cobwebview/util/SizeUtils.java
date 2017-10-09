package me.sheepyang.cobwebview.util;

import android.content.Context;

/**
 * Created by SheepYang on 2017-10-09.
 */

public class SizeUtils {
    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
