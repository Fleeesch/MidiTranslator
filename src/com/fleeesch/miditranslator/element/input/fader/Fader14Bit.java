package com.fleeesch.miditranslator.element.input.fader;

import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.functions.math.Calculate;

public class Fader14Bit extends Fader7bit {

    //************************************************************
    //      Variables
    //************************************************************

    public int msbValue = -1; // msb value
    public final int lsbValue = -1; // lsb value

    public final FaderLsbListener lsbListener; // listener for lsb messages

    //************************************************************
    //      Constructor
    //************************************************************

    public Fader14Bit(String pName, double pSmooth, int m1, int m2) {

        super(pName, pSmooth, m1, m2);

        lsbListener = new FaderLsbListener(this, m1, m2);

    }


    //************************************************************
    //      Method : Handle Input
    //************************************************************

    @Override
    public void handleInput(double val, int[] msg) {

        msbValue = (int) (val * 127); // store current value as target value

    }

    //************************************************************
    //      Method : Get LSB Info
    //************************************************************

    public void getLSB(int val) {

        double outVal = (msbValue * 128 + val) / 16383.0;

        if (rescaleInput) outVal = Calculate.rescaleValueLimit(outVal, rescaleMin, rescaleMax, 0, 1);

        setParameterValue(outVal);

        // start smoothing routine if enabled
        if (useSmoothing) {
            startRoutine();
            return;
        }

        // set value directly if there is no smoothing to do
        for (VirtualElement e : availableTargetElements) e.handleInput(this, outVal);


    }

}
