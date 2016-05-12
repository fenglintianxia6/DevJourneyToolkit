package dev.journey.uitoolkit.util;

import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by mwp on 2016/3/2.
 */
public class AnimationUtils {
    public static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutSlowInInterpolator();

    public static int lerp(int startValue, int endValue, float fraction) {
        return startValue + Math.round(fraction * (endValue - startValue));
    }
}
