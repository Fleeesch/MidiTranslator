package com.fleeesch.miditranslator.element.input.pad.handler;

import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.input.pad.PadPressureSensitive;

public class PadPressureHandler extends InputElement {

    //************************************************************
    //      Variables
    //************************************************************

    final PadPressureSensitive pad;

    //************************************************************
    //      Constructor
    //************************************************************

    public PadPressureHandler(PadPressureSensitive pPad, String pName, int m1, int m2) {

        name = pName;

        pad = pPad;

        setMidiAddress(m1, m2);

    }

    //************************************************************
    //      Method: Handle Input
    //************************************************************

    @Override
    public void handleInput(double val, int[] msg) {
        pad.handleZ(val);
    }

}
