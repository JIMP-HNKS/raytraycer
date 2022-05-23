package com.hnks.raytraycer.util;

import com.hnks.raytraycer.math.Vector;

import java.awt.*;

public class ColorUtil {
    public static double channelTosRGB(double channel) {
        if (channel > 0.0031308) {
            return 1.055 * Math.pow(Math.min(1.0, channel), 1 / 2.4) - 0.055;
        } else {
            return 12.92 * channel;
        }
    }

    public static Color linearVectorToColor(Vector linearColor) {
        return new Color(
                (float)channelTosRGB(linearColor.x()),
                (float)channelTosRGB(linearColor.y()),
                (float)channelTosRGB(linearColor.z())
        );
    }
}
