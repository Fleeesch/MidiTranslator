package com.fleeesch.miditranslator.functions.math;

public class Color {

    // TODO: Implement HSL conversions

    //************************************************************
    //      Method : RGB to Hex
    //************************************************************
    public static int RgbToHex(int r, int g, int b) {

        return (r << 16) + (g << 8) + b;

    }

    //************************************************************
    //      Method : RGB to Hex
    //************************************************************

    public static int RgbDoubleToHex(double r, double g, double b) {

        return ((int) (r * 127.0) << 16) + (((int) (g * 127.0)) << 8) + (int) (b * 127.0);

    }

    //************************************************************
    //      Method : Hex to RGB
    //************************************************************

    public static int[] HexToRgb(int clr) {

        return new int[]{clr >> 16, (clr >> 8) & 0xFF, clr & 0xFF};

    }

    //************************************************************
    //      Method : Hex to RGB (Double)
    //************************************************************

    public static double[] HexToRgbDouble(int clr) {

        return new double[]{(clr >> 16) / 127.0, ((clr >> 8) & 0xFF) / 127.0, (clr & 0xFF) / 127.0};

    }

}
