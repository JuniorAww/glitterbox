package com.junioraww.glitterbox.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class Animation {
    public static String glowing(String string) {
        var frame = (int) Math.floor(System.currentTimeMillis() % 2160 / 120);
        frame = Math.abs(frame - 9);
        var part = string.split("\\|");
        var color = part[0].replaceAll("R|G|B", Integer.toString(frame));
        var result = "§x";
        for(var c : color.split("")) result += "§" + c;
        return result + part[1];
    }
    public static String transit(String string) {
        var part = string.split("\\|");
        var color = part[0].split(">");
        var text = part[1];
        var length = text.length() * 2;
        var frame = (int) Math.floor(System.currentTimeMillis() % (50 * length) / 50);
        ArrayList<String> result = new ArrayList<>(Arrays.asList((text).split("")));
        if(frame > text.length()) {
            result.add(0, color[0]);
            result.add(frame - text.length(), color[1]);
        } else {
            result.add(frame, color[0]);
        }

        return color[1] + String.join("", result);
    }
}
