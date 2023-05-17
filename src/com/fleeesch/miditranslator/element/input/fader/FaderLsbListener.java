package com.fleeesch.miditranslator.element.input.fader;

import com.fleeesch.miditranslator.element.input.InputElement;

public class FaderLsbListener extends InputElement {

    //************************************************************
    //      Variables
    //************************************************************

    public final Fader14Bit fader; // linked fader
    public int value; // last lsb value

    //************************************************************
    //      Constructor
    //************************************************************

    FaderLsbListener(Fader14Bit f, int m1, int m2) {

        setMidiAddress(m1, m2 + 32); // setup midi lookup address with offset

        fader = f; // store fader link

    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    @Override
    public void handleInput(double val, int[] msg) {

        value = (int) (val * 127); // calculate lsb

        fader.getLSB(value); // inform fader of incoming lsb value


    }


}
