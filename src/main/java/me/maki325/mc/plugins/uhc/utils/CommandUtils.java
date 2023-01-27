package me.maki325.mc.plugins.uhc.utils;

import java.util.Arrays;

public class CommandUtils {

    public static <T> T[] popFirst(T[] src) {
        return Arrays.copyOfRange(src, 1, src.length);
    }

}
