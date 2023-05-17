package com.fleeesch.miditranslator.element.input.fader.handler;

import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.input.fader.FaderMotorized;

//* * * * * * * * * * * * * * * * * * * * * * * *
//  Class

public class FaderTouchHandler extends InputElement {

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Static

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Public

    public final FaderMotorized fader; // fader the touch listener is linked to


    //************************************************************
    //      Constructor
    //************************************************************

    public FaderTouchHandler(FaderMotorized pFaderLink, int m1, int m2) {

        fader = pFaderLink; // store linked fader

        setMidiAddress(m1, m2); // store midi lookup address

    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    public void handleInput(double val, int[] msg) {

        // set touch state based on input value
        fader.setTouchState(val > 0);


    }

}
