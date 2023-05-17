package com.fleeesch.miditranslator.functions.math;

public class
Calculate {

    //************************************************************
    //      Method : Rescale Value (With Limiter)
    //************************************************************

    public static double rescaleValueLimit(double val, double in_l, double in_h, double out_l, double out_h) {

        if (in_l == in_h || out_l == out_h) return out_l; //input or output not a range? set to out l

        double rtn = (val - in_l) * (out_h - out_l) / (in_h - in_l) + out_l;

        if(rtn < out_l) return out_l;
        return Math.min(rtn, out_h);

        // return rescaled value

    }

    //************************************************************
    //      Method : Rescale Value
    //************************************************************

    public static double rescaleValue(double val, double in_l, double in_h, double out_l, double out_h) {

        if (in_l == in_h || out_l == out_h) return out_l; //input or output not a range? set to out l

        return (val - in_l) * (out_h - out_l) / (in_h - in_l) + out_l; // return rescaled value

    }

    //************************************************************
    //      Method : Interpolate Value
    //************************************************************

    public static double interpolateValue(double pValNow, double pValDest, double pSmooth) {

        if (pValNow == pValDest) return pValNow; // value is already reached? just return the destination number

        double rtn = pValNow + (pValDest - pValNow) * pSmooth;

        if (Math.abs(pValDest - pValNow) < 0.01) {
            return pValDest;
        }

        return rtn;

    }


}
